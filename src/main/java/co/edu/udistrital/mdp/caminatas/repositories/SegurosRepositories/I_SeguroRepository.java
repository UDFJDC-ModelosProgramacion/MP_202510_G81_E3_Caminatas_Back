package co.edu.udistrital.mdp.caminatas.repositories.SegurosRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.SeguroEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface I_SeguroRepository extends JpaRepository <SeguroEntity, Long>{
    
}
