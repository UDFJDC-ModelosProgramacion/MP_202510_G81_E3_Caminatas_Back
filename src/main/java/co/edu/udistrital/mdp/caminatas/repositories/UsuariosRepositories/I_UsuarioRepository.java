package co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;

@Repository
public interface I_UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCase(String correo);
    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);
    boolean existsByCedula(Long cedula);
}



