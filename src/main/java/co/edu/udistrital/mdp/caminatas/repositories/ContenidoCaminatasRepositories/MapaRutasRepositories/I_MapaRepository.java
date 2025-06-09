package co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories.MapaRutasRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities.MapaEntity;

@Repository
public interface I_MapaRepository extends JpaRepository<MapaEntity, Long> {
    // Aquí puedes agregar métodos específicos para el repositorio de Mapa si es necesario.
    
}
