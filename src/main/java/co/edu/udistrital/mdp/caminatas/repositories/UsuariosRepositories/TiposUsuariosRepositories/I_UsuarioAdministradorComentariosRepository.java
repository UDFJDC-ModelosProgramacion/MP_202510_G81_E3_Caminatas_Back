package co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.TiposUsuariosRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioAdministradorComentariosEntity;

@Repository
public interface I_UsuarioAdministradorComentariosRepository extends JpaRepository<UsuarioAdministradorComentariosEntity, Long> {
    Optional<UsuarioAdministradorComentariosEntity> findByCorreoIgnoreCase(String correo);
    Optional<UsuarioAdministradorComentariosEntity> findByNombreUsuarioIgnoreCase(String nombreUsuario);
    boolean existsByCorreoIgnoreCase(String correo);
    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);
    boolean existsByCedula(Long cedula);
    
}
