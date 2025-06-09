package co.edu.udistrital.mdp.caminatas.repositories.CaminatasRepositories.TiposCaminatasRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.TiposCaminatasEntites.CaminatasDeportivasEntities.MontanaEntity;

@Repository
public interface I_MontanaRepository extends JpaRepository<MontanaEntity, Long> {
    
    // Aquí puedes agregar métodos personalizados si es necesario
    
}
