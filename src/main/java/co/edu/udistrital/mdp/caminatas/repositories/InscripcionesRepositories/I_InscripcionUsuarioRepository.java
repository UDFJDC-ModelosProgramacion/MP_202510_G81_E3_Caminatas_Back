package co.edu.udistrital.mdp.caminatas.repositories.InscripcionesRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.InscripcionesEntities.InscripcionUsuarioEntity;

@Repository
public interface I_InscripcionUsuarioRepository extends JpaRepository<InscripcionUsuarioEntity, Long> {
    boolean existsByUsuario_IdAndCaminata_Id(Long usuarioId, Long caminataId);
    boolean existsByUsuario_IdAndCaminata_IdAndEstadoPagoTrue(Long usuarioId, Long caminataId);
    
}
