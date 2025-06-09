package co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.TiposUsuariosRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;

@Repository
public interface I_UsuarioNaturalRepository extends JpaRepository<UsuarioNaturalEntity, Long> {
    Optional<UsuarioNaturalEntity> findByCorreoIgnoreCase(String correo);
    Optional<UsuarioNaturalEntity> findByNombreUsuarioIgnoreCase(String nombreUsuario);
    boolean existsByCorreoIgnoreCase(String correo);
    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);
    boolean existsByCedula(Long cedula);
}
