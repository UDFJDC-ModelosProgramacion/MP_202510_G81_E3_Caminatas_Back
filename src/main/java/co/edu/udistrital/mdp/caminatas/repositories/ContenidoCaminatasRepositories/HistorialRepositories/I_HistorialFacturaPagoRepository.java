package co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories.HistorialRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.HistorialEntities.HistorialFacturaPagoEntity;

@Repository
public interface I_HistorialFacturaPagoRepository extends JpaRepository<HistorialFacturaPagoEntity, Long> {
    List<HistorialFacturaPagoEntity> findByFactura_Id(Long facturaId);
    List<HistorialFacturaPagoEntity> findByPago_Id(Long pagoId);
}

