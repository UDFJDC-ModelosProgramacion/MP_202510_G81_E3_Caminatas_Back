package co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.TiposUsuariosRepositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioJuridicoEntity;

@Repository
public interface I_UsuarioJuridicoRepository extends JpaRepository<UsuarioJuridicoEntity, Long> {
    Optional<UsuarioJuridicoEntity> findByCorreoIgnoreCase(String correo);
    Optional<UsuarioJuridicoEntity> findByNombreUsuarioIgnoreCase(String nombreUsuario);
    boolean existsByCorreoIgnoreCase(String correo);
    boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);
    boolean existsByCedula(Long cedula);
    List<UsuarioJuridicoEntity> findAll();
}
