package co.edu.udistrital.mdp.caminatas.services.UsuariosServices;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.I_UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final I_UsuarioRepository usuarioRepository;

    public Optional<UsuarioEntity> findByCorreo(String correo) {
        return usuarioRepository.findByCorreoIgnoreCase(correo);
    }
}
