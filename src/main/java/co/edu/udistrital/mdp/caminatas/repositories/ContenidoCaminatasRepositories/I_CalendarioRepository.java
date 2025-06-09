package co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.CalendarioEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface  I_CalendarioRepository extends JpaRepository <CalendarioEntity, Long> {
    // Aquí puedes agregar métodos específicos para el repositorio de Calendario si es necesario
    
}
