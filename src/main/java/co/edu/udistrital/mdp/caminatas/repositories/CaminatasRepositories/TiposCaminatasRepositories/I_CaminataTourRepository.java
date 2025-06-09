package co.edu.udistrital.mdp.caminatas.repositories.CaminatasRepositories.TiposCaminatasRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.TiposCaminatasEntites.CaminataTourEntity;

@Repository
public interface I_CaminataTourRepository extends JpaRepository<CaminataTourEntity, Long> {

}
