package co.edu.udistrital.mdp.caminatas.services.TransaccionesServices;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.TransaccionesRequestDTO.PagoRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.TransaccionesResponsesDTO.PagoResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.InscripcionesEntities.InscripcionUsuarioEntity;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.FacturaEntity;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.EstadoPago;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.MetodoPago;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.PagoEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.ConflictException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.repositories.InscripcionesRepositories.I_InscripcionUsuarioRepository;
import co.edu.udistrital.mdp.caminatas.repositories.TransaccionesRepositories.I_FacturaRepository;
import co.edu.udistrital.mdp.caminatas.repositories.TransaccionesRepositories.I_PagoRepository;
import co.edu.udistrital.mdp.caminatas.services.ContenidoCaminatasServices.HistorialFacturaPagoService;
import co.edu.udistrital.mdp.caminatas.services.NotificationServices.NotificationManager;
import co.edu.udistrital.mdp.caminatas.services.NotificationServices.NotificationWaysServices.EmailService;
import co.edu.udistrital.mdp.caminatas.services.NotificationServices.NotificationWaysServices.SmsService;
import co.edu.udistrital.mdp.caminatas.services.NotificationServices.Observers.NotificadorCorreoObserver;
import co.edu.udistrital.mdp.caminatas.services.NotificationServices.Observers.NotificadorSmsObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagoService {

    private final I_PagoRepository pagoRepository;
    private final I_FacturaRepository facturaRepository;
    private final I_InscripcionUsuarioRepository inscripcionRepository;
    private final HistorialFacturaPagoService historialService;

    // Servicios para las notificaciones
    private final EmailService emailService;
    private final SmsService smsService;

    /**
     * Convierte una entidad Pago a su DTO de respuesta.
     */
    public PagoResponseDTO toResponseDTO(PagoEntity pago) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(pago.getId());
        dto.setMonto(pago.getMonto());
        dto.setMetodo(pago.getMetodo());
        dto.setEstado(pago.getEstado());
        dto.setFechaPago(pago.getFechaPago());
        dto.setFacturaId(pago.getFactura() != null ? pago.getFactura().getId() : null);
        return dto;
    }

    /**
     * Simula o registra un pago real.
     */
    public PagoEntity simularPago(PagoRequestDTO dto) {
        return procesarPago(dto.getFacturaId(), dto.getMonto(), dto.getMetodo());
    }
    /**
     * Método de atajo para registrar un pago mock (aprobado automáticamente).
     * Útil para pruebas rápidas.
     */
    public PagoEntity registrarPagoMock(Long facturaId, BigDecimal monto, MetodoPago metodo) {
        return procesarPago(facturaId, monto, metodo);
    }


    /**
     * Lógica compartida para simular o registrar pagos.
     */
    private PagoEntity procesarPago(Long facturaId, BigDecimal monto, MetodoPago metodo) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor que cero.");
        }

        FacturaEntity factura = obtenerFactura(facturaId);

        BigDecimal totalPagado = factura.getPagos().stream()
            .filter(p -> p.getEstado() == EstadoPago.APROBADO)
            .map(PagoEntity::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Validación contra sobrepago
        if (totalPagado.add(monto).compareTo(factura.getTotal()) > 0) {
            throw new ConflictException("❌ El monto excede el total de la factura");
        }

        // Crear pago
        PagoEntity pago = new PagoEntity();
        pago.setFactura(factura);
        pago.setMonto(monto);
        pago.setMetodo(metodo);
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado(metodo == MetodoPago.MOCK ? EstadoPago.APROBADO : EstadoPago.PENDIENTE);
        pago.setActivo(true);

        PagoEntity guardado = pagoRepository.save(pago);
        historialService.registrar(factura, guardado);
        FacturaEntity facturaActualizada = facturaRepository.findById(factura.getId()).orElseThrow(() -> new NotFoundException("Factura no encontrada"));

        // Si fue aprobado, verificar si factura queda saldada
        if (guardado.getEstado() == EstadoPago.APROBADO) {
            verificarYMarcarFacturaPagada(facturaActualizada);
            // Recarga la factura para obtener el estado actualizado
            FacturaEntity facturaFinal = facturaRepository.findById(factura.getId())
                .orElseThrow(() -> new NotFoundException("Factura no encontrada"));

            if (Boolean.TRUE.equals(facturaFinal.getPagada())) {
                InscripcionUsuarioEntity inscripcion = factura.getInscripcion();
                inscripcion.setEstadoPago(true);
                inscripcionRepository.save(inscripcion);
                log.info("✅ Factura pagada completamente | facturaId={} | usuario={}",
                        factura.getId(), inscripcion.getUsuario().getCorreo());
            }
        }

        return guardado;
    }

    /**
     * Marca la factura como pagada si el total de pagos aprobados iguala o supera el total.
     */
    private void verificarYMarcarFacturaPagada(FacturaEntity factura) {

        factura = facturaRepository.findById(factura.getId())
        .orElseThrow(() -> new NotFoundException("Factura no encontrada (al verificar total pagado)"));


        // Consulta directa de pagos aprobados
        BigDecimal totalPagado = pagoRepository.findByFactura_IdAndEstado(factura.getId(), EstadoPago.APROBADO)
            .stream()
            .map(PagoEntity::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPagado.compareTo(factura.getTotal()) >= 0 && !Boolean.TRUE.equals(factura.getPagada())) {
            factura.setPagada(true);
            facturaRepository.save(factura);

            InscripcionUsuarioEntity inscripcion = factura.getInscripcion();
            inscripcion.setEstadoPago(true);
            inscripcionRepository.save(inscripcion);

            log.info("✅ Factura pagada completamente | facturaId={} | usuario={}",
                    factura.getId(), inscripcion.getUsuario().getCorreo());

            // 🧠 Notificar usando patrón Observer
            String mensaje = """
                ✅ ¡Hola %s! Tu pago para la caminata "%s" ha sido aprobado.
                📅 %s 🕒 %s
                Lugar: %s
                Total: $%,.2f

                ¡Nos vemos pronto!
                """
                .formatted(
                    inscripcion.getUsuario().getNombreUsuario(),
                    inscripcion.getCaminata().getNombreCaminata(),
                    inscripcion.getCaminata().getFecha(),
                    inscripcion.getCaminata().getHora(),
                    inscripcion.getCaminata().getLugar(),
                    factura.getTotal()
                );

            NotificationManager manager = new NotificationManager();

            // Registrar observadores
            manager.registrar(new NotificadorCorreoObserver(
                inscripcion.getUsuario().getCorreo(), emailService
            ));

            manager.registrar(new NotificadorSmsObserver(
                inscripcion.getUsuario().getTelefono().toString(), smsService
            ));

            // Ejecutar notificaciones
            manager.notificar(mensaje);
        }
    }


    private FacturaEntity obtenerFactura(Long id) {
        return facturaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Factura no encontrada"));
    }

    public List<PagoEntity> findAll() {
        return pagoRepository.findByActivoTrue();
    }

    public Optional<PagoEntity> findById(Long id) {
        return pagoRepository.findById(id)
            .filter(PagoEntity::isActivo);
    }

    public PagoEntity save(PagoEntity pago) {
        return pagoRepository.save(pago);
    }

    public void delete(Long id) {
        PagoEntity pago = pagoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Pago no encontrado"));

        pago.setActivo(false);
        pagoRepository.save(pago);
    }
}
