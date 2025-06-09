package co.edu.udistrital.mdp.caminatas.repositories.TransaccionesRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.FacturaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface I_FacturaRepository extends JpaRepository<FacturaEntity, Long> {
    boolean existsByInscripcion_Id(Long inscripcionId);
    
}
