package co.edu.udistrital.mdp.caminatas.repositories.SegurosRepositories.TiposSegurosRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TiposSegurosEntities.SeguroJuridicoEntity;

@Repository
public interface I_SeguroJuridicoRepository extends JpaRepository<SeguroJuridicoEntity, Long>{
    Optional<SeguroJuridicoEntity> findFirstByOrderByIdAsc();
}
