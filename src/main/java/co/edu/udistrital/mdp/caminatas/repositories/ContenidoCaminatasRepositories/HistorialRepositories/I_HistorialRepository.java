package co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories.HistorialRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.HistorialEntities.HistorialEntity;

@Repository
public interface I_HistorialRepository extends JpaRepository <HistorialEntity, Long> {
    
}
