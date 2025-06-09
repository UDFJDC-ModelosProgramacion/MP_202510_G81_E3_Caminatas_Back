package co.edu.udistrital.mdp.caminatas.repositories.SegurosRepositories.TiposSegurosRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TiposSegurosEntities.SeguroAdicionalEntity;

@Repository
public interface I_SeguroAdicionalRepository extends JpaRepository<SeguroAdicionalEntity, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    
}
