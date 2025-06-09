package co.edu.udistrital.mdp.caminatas.repositories.TransaccionesRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.EstadoPago;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.PagoEntity;

import org.springframework.stereotype.Repository;

@Repository
public interface I_PagoRepository extends JpaRepository <PagoEntity, Long> {
    List<PagoEntity> findByActivoTrue(); // ✅ Solo los activos
    List<PagoEntity> findByFactura_IdAndEstado(Long facturaId, EstadoPago estado);
}
