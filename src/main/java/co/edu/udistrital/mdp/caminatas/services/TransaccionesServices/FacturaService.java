package co.edu.udistrital.mdp.caminatas.services.TransaccionesServices;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.TransaccionesRequestDTO.FacturaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.TransaccionesResponsesDTO.FacturaResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.TransaccionesResponsesDTO.PagoSimpleResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.InscripcionesEntities.InscripcionUsuarioEntity;
import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TiposSegurosEntities.SeguroAdicionalEntity;
import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TiposSegurosEntities.SeguroBasicoObligatorioEntity;
import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TiposSegurosEntities.SeguroJuridicoEntity;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.FacturaEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioJuridicoEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.ConflictException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.UnauthorizedException;
import co.edu.udistrital.mdp.caminatas.repositories.InscripcionesRepositories.I_InscripcionUsuarioRepository;
import co.edu.udistrital.mdp.caminatas.repositories.SegurosRepositories.TiposSegurosRepositories.I_SeguroAdicionalRepository;
import co.edu.udistrital.mdp.caminatas.repositories.SegurosRepositories.TiposSegurosRepositories.I_SeguroBasicoObligatorioRepository;
import co.edu.udistrital.mdp.caminatas.repositories.SegurosRepositories.TiposSegurosRepositories.I_SeguroJuridicoRepository;
import co.edu.udistrital.mdp.caminatas.repositories.TransaccionesRepositories.I_FacturaRepository;
import co.edu.udistrital.mdp.caminatas.services.UsuariosServices.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final I_FacturaRepository facturaRepository;
    private final I_InscripcionUsuarioRepository inscripcionRepository;
    private final I_SeguroBasicoObligatorioRepository seguroBasicoRepository;
    private final I_SeguroAdicionalRepository seguroAdicionalRepository;
    private final I_SeguroJuridicoRepository seguroJuridicoRepository;
    private final UsuarioService usuarioService;

    /**
     * Método principal para el controller: busca el usuario por correo y genera la factura.
     */
    public FacturaResponseDTO generarFactura(FacturaRequestDTO dto, String correoUsuario) {
        UsuarioEntity usuario = usuarioService.findByCorreo(correoUsuario)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        FacturaEntity factura = generarFacturaDesdeDTO(dto, usuario);
        return toResponseDTO(factura);
    }

    /**
     * Lógica de generación de factura a partir del DTO y el usuario.
     */
    public FacturaEntity generarFacturaDesdeDTO(FacturaRequestDTO dto, UsuarioEntity usuario) {
        // Validar inscripción y propiedad del usuario
        InscripcionUsuarioEntity inscripcion = inscripcionRepository.findById(dto.getIdInscripcion())
            .orElseThrow(() -> new NotFoundException("Inscripción no encontrada"));

        if (!inscripcion.getUsuario().getId().equals(usuario.getId())) {
            throw new UnauthorizedException("No puedes generar una factura para una inscripción que no es tuya.");
        }

        if (facturaRepository.existsByInscripcion_Id(dto.getIdInscripcion())) {
            throw new ConflictException("Ya existe una factura para esta inscripción.");
        }

        BigDecimal total = inscripcion.getCaminata().getCostoCaminata();
        List<String> detalles = new ArrayList<>();
        detalles.add("Factura para caminata: " + inscripcion.getCaminata().getNombreCaminata());

        // Seguro obligatorio y adicionales según tipo de usuario
        if (usuario instanceof UsuarioNaturalEntity) {
            SeguroBasicoObligatorioEntity seguro = (dto.getSeguroBasicoId() != null)
                ? seguroBasicoRepository.findById(dto.getSeguroBasicoId())
                    .orElseThrow(() -> new NotFoundException("Seguro básico no encontrado"))
                : seguroBasicoRepository.findFirstByOrderByIdAsc()
                    .orElseThrow(() -> new NotFoundException("Seguro básico por defecto no configurado"));

            total = total.add(seguro.getCostoSeguro());
            detalles.add("Seguro básico: " + seguro.getDescripcionSeguro());

        } else if (usuario instanceof UsuarioJuridicoEntity) {
            SeguroJuridicoEntity seguro;
            if (dto.getSeguroJuridicoId() != null) {
                seguro = seguroJuridicoRepository.findById(dto.getSeguroJuridicoId())
                        .orElseThrow(() -> new NotFoundException("Seguro jurídico no encontrado"));
            } else {
                throw new IllegalArgumentException("El seguro jurídico es obligatorio para usuarios jurídicos");
            }

            total = total.add(seguro.getCostoSeguro());
            detalles.add("Seguro jurídico: " + seguro.getDescripcionSeguro());

        } else {
            throw new IllegalArgumentException("Tipo de usuario no válido para inscripción");
        }

        // Seguros adicionales
        if (dto.getSegurosAdicionalesIds() != null && !dto.getSegurosAdicionalesIds().isEmpty()) {
            List<SeguroAdicionalEntity> adicionales = seguroAdicionalRepository.findAllById(dto.getSegurosAdicionalesIds());

            BigDecimal totalAdicional = adicionales.stream()
                .map(SeguroAdicionalEntity::getCostoSeguro)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            total = total.add(totalAdicional);
            detalles.add(adicionales.size() + " seguros adicionales");
        }

        // Crear y guardar la factura
        FacturaEntity factura = new FacturaEntity();
        factura.setUsuario(usuario);
        factura.setInscripcion(inscripcion);
        factura.setFechaCreacion(LocalDate.now());
        factura.setDescripcion(String.join(" | ", detalles));
        factura.setTotal(total);
        factura.setPagada(false);

        return facturaRepository.save(factura);
    }

    public List<FacturaEntity> findAll() {
        return facturaRepository.findAll();
    }

    public Optional<FacturaEntity> findById(Long id) {
        return facturaRepository.findById(id);
    }

    public void delete(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new NotFoundException("Factura no encontrada con ID: " + id);
        }
        facturaRepository.deleteById(id);
    }

    public FacturaResponseDTO toResponseDTO(FacturaEntity factura) {
        FacturaResponseDTO dto = new FacturaResponseDTO();
        dto.setId(factura.getId());
        dto.setTotal(factura.getTotal());
        dto.setFechaCreacion(factura.getFechaCreacion());
        dto.setDescripcion(factura.getDescripcion());
        dto.setPagada(factura.getPagada());
        dto.setInscripcionId(factura.getInscripcion().getId());

        if (factura.getInscripcion().getUsuario() != null) {
            dto.setUsuarioId(factura.getInscripcion().getUsuario().getId());
        }

        if (factura.getPagos() != null && !factura.getPagos().isEmpty()) {
            List<PagoSimpleResponseDTO> pagos = factura.getPagos().stream()
                .map(p -> {
                    PagoSimpleResponseDTO pagoDto = new PagoSimpleResponseDTO();
                    pagoDto.setId(p.getId());
                    pagoDto.setMonto(p.getMonto());
                    pagoDto.setFechaPago(p.getFechaPago());
                    pagoDto.setMetodo(p.getMetodo().name());
                    pagoDto.setEstado(p.getEstado().name());
                    return pagoDto;
                })
                .toList();
            dto.setPagos(pagos);
        }

        return dto;
    }

    public List<FacturaResponseDTO> findAllDTO() {
        return findAll().stream()
            .map(this::toResponseDTO)
            .toList();
    }

    public Optional<FacturaResponseDTO> findByIdDTO(Long id) {
        return findById(id).map(this::toResponseDTO);
    }
}