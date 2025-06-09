package co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.GaleriaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface I_GaleriaRepository extends JpaRepository <GaleriaEntity, Long> {
    
}
