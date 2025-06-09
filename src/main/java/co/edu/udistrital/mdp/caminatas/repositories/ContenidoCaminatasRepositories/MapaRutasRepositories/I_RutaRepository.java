package co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories.MapaRutasRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities.RutaEntity;

@Repository
public interface I_RutaRepository extends JpaRepository<RutaEntity, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar rutas por nombre o ubicación
    
}
