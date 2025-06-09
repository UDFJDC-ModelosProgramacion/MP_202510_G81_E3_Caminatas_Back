package co.edu.udistrital.mdp.caminatas.services.ContenidoCaminatasServices;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.HistorialEntities.HistorialFacturaPagoEntity;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.FacturaEntity;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.PagoEntity;
import co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories.HistorialRepositories.I_HistorialFacturaPagoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistorialFacturaPagoService {

    private final I_HistorialFacturaPagoRepository historialRepo;

    public HistorialFacturaPagoEntity registrar(FacturaEntity factura, PagoEntity pago) {
        HistorialFacturaPagoEntity historial = new HistorialFacturaPagoEntity();
        historial.setFactura(factura);
        historial.setPago(pago);
        return historialRepo.save(historial);
    }

    public List<HistorialFacturaPagoEntity> obtenerPorFactura(Long facturaId) {
        return historialRepo.findByFactura_Id(facturaId);
    }

    public List<HistorialFacturaPagoEntity> obtenerPorPago(Long pagoId) {
        return historialRepo.findByPago_Id(pagoId);
    }
}
