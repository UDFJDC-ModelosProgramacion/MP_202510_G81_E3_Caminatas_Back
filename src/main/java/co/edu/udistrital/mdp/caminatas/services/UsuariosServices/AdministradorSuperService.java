package co.edu.udistrital.mdp.caminatas.services.UsuariosServices;

import co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration.JwtUtil;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.AdministradorComentariosRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.UsuarioJuridicoRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.UsuarioNaturalRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InicioSesionResponseDTO.LoginResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.UsuariosResponsesDTO.UsuarioResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioAdministradorComentariosEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioJuridicoEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.ConflictException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.IllegalOperationException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.UnauthorizedException;
import co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.I_UsuarioRepository;
import co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.TiposUsuariosRepositories.I_UsuarioAdministradorComentariosRepository;
import co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.TiposUsuariosRepositories.I_UsuarioJuridicoRepository;
import co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.TiposUsuariosRepositories.I_UsuarioNaturalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdministradorSuperService {


    private final I_UsuarioNaturalRepository usuarioNaturalRepository;
    private final I_UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final I_UsuarioJuridicoRepository usuarioJuridicoRepository;
    private final I_UsuarioAdministradorComentariosRepository usuarioAdminComentariosRepository;
    private final JwtUtil jwtUtil;
    
    @Value("${superadmin.email}")
    private String correoSuperAdmin;

    public List<UsuarioEntity> listarTodos() {
        return usuarioRepository.findAll();
    }
    // Login para SuperAdministrador
    public LoginResponseDTO login(String identificador, String password) {
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findAll().stream()
            .filter(u -> u.getRol() == RolUsuario.SUPER_ADMIN)
            .filter(u -> u.getCorreo().equalsIgnoreCase(identificador)
                    || u.getNombreUsuario().equalsIgnoreCase(identificador))
            .findFirst();

        if (usuarioOpt.isEmpty()) {
            throw new UnauthorizedException("Usuario o contraseña inválidos");
        }

        UsuarioEntity usuario = usuarioOpt.get();


        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new UnauthorizedException("Usuario o contraseña inválidos");
        }

        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol());

        return new LoginResponseDTO(usuario.getCorreo(), token, usuario.getRol());
    }

    // Buscar usuario por ID
    public UsuarioEntity buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    // Actualizar perfil SuperAdministrador
    public UsuarioResponseDTO actualizarSuperAdmin(AdministradorComentariosRequestDTO dto) {
        UsuarioEntity superAdmin = usuarioRepository.findAll().stream()
            .filter(u -> RolUsuario.SUPER_ADMIN.equals(u.getRol()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("SuperAdministrador no encontrado"));

        if (!superAdmin.getCorreo().equalsIgnoreCase(correoSuperAdmin)) {
            throw new ConflictException("No se puede modificar un usuario que no es el SuperAdministrador principal.");
        }
        

        // Actualiza solo información permitida
        superAdmin.setNombreUsuario(dto.getNombreUsuario());
        superAdmin.setCorreo(dto.getCorreo());  // Se permite actualizar correo
        superAdmin.setCedula(dto.getCedula());
        superAdmin.setTelefono(dto.getTelefono());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            superAdmin.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        if (!dto.getCorreo().equalsIgnoreCase(superAdmin.getCorreo())) {
            throw new IllegalOperationException("No se permite cambiar el correo del SuperAdministrador principal.");
        }


        usuarioRepository.save(superAdmin);
        return toDTO(superAdmin);
    }
    // Convertir UsuarioEntity a UsuarioResponseDTO
    public UsuarioResponseDTO toDTO(UsuarioEntity entity) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(entity.getId());
        dto.setRol(entity.getRol());
        dto.setNombreUsuario(entity.getNombreUsuario());
        dto.setCorreo(entity.getCorreo());
        dto.setCedula(entity.getCedula());
        dto.setTelefono(entity.getTelefono());
        return dto;
    }
    
    // Buscar usuarios por rol
    public List<UsuarioEntity> buscarPorRol(RolUsuario rol) {
        return usuarioRepository.findAll().stream()
            .filter(u -> u.getRol() == rol)
            .toList();
    }

    // Buscar usuario por correo
    public UsuarioEntity buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreoIgnoreCase(correo)
        .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    // Actualizar usuario por ID
    public UsuarioResponseDTO actualizarUsuarioPorId(Long id, AdministradorComentariosRequestDTO dto) {
        UsuarioEntity usuario = buscarPorId(id);

        if (usuario.getRol() == RolUsuario.SUPER_ADMIN) {
            throw new ConflictException("No puedes modificar al SuperAdministrador desde este endpoint.");
        }

        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setCorreo(dto.getCorreo());
        usuario.setCedula(dto.getCedula());
        usuario.setTelefono(dto.getTelefono());

        return toDTO(usuarioRepository.save(usuario));
    }

    // Actualizar perfil Usuario Nutural
    public UsuarioResponseDTO actualizarUsuarioNatural(Long id, UsuarioNaturalRequestDTO dto) {
        UsuarioNaturalEntity usuario = (UsuarioNaturalEntity) usuarioRepository.findById(id)
            .filter(u -> u instanceof UsuarioNaturalEntity)
            .orElseThrow(() -> new NotFoundException("Usuario natural no encontrado"));

        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());
        usuario.setCedula(dto.getCedula());
        return toDTO(usuarioRepository.save(usuario));
    }

    // Actualizar perfil Usuario Juridico
    public UsuarioResponseDTO actualizarUsuarioJuridico(Long id, UsuarioJuridicoRequestDTO dto) {
        UsuarioJuridicoEntity usuario = (UsuarioJuridicoEntity) usuarioRepository.findById(id)
            .filter(u -> u instanceof UsuarioJuridicoEntity)
            .orElseThrow(() -> new NotFoundException("Usuario jurídico no encontrado"));

        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());
        usuario.setCedula(dto.getCedula());
        usuario.setNombreEmpresa(dto.getNombreEmpresa());
        usuario.setNombresParticipantes(dto.getNombresParticipantes());
        usuario.setNumParticipantes(dto.getNombresParticipantes().size());
        return toDTO(usuarioRepository.save(usuario));
    }

    // Actualizar perfil Administrador de Comentarios
    public UsuarioResponseDTO actualizarUsuarioAdministradorComentarios(Long id, AdministradorComentariosRequestDTO dto) {
        UsuarioAdministradorComentariosEntity usuario = (UsuarioAdministradorComentariosEntity) usuarioRepository.findById(id)
            .filter(u -> u instanceof UsuarioAdministradorComentariosEntity)
            .orElseThrow(() -> new NotFoundException("Administrador de comentarios no encontrado"));

        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());
        usuario.setCedula(dto.getCedula());
        return toDTO(usuarioRepository.save(usuario));
    }
    // Eliminar usuario por ID
    public void eliminarUsuario(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (usuario.getRol() == RolUsuario.SUPER_ADMIN) {
            throw new ConflictException("❌ No se puede eliminar al SuperAdministrador.");
        }

        usuarioRepository.deleteById(id);
    }

    public int verificarTodosLosUsuariosDelSistema() {
        int total = 0;

        // 🔹 Naturales
        List<UsuarioNaturalEntity> naturales = usuarioNaturalRepository.findAll().stream()
            .filter(u -> !Boolean.TRUE.equals(u.getVerificado()))
            .toList();
        naturales.forEach(u -> {
            u.setVerificado(true);
            u.setCodigoVerificacion(null);
        });
        usuarioNaturalRepository.saveAll(naturales);
        log.info("✔️ Naturales verificados: {}", naturales.size());
        total += naturales.size();

        // 🔹 Jurídicos
        List<UsuarioJuridicoEntity> juridicos = usuarioJuridicoRepository.findAll().stream()
            .filter(u -> !Boolean.TRUE.equals(u.getVerificado()))
            .toList();
        juridicos.forEach(u -> {
            u.setVerificado(true);
            u.setCodigoVerificacion(null);
        });
        usuarioJuridicoRepository.saveAll(juridicos);
        log.info("✔️ Jurídicos verificados: {}", juridicos.size());
        total += juridicos.size();

        // 🔹 Admin de comentarios
        List<UsuarioAdministradorComentariosEntity> admins = usuarioAdminComentariosRepository.findAll().stream()
            .filter(u -> !Boolean.TRUE.equals(u.getVerificado()))
            .toList();
        admins.forEach(u -> {
            u.setVerificado(true);
            u.setCodigoVerificacion(null);
        });
        usuarioAdminComentariosRepository.saveAll(admins);
        log.info("✔️ Admins de comentarios verificados: {}", admins.size());
        total += admins.size();

        log.warn("🛡️ Verificación masiva completada. Total de usuarios verificados: {}", total);
        return total;
    }


}

