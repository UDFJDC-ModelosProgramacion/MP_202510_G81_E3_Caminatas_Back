package co.edu.udistrital.mdp.caminatas.services.UsuariosServices;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration.JwtUtil;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.UsuarioNaturalRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InicioSesionResponseDTO.LoginResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.UsuariosResponsesDTO.UsuarioNaturalResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.BadRequestException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.ConflictException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.IllegalOperationException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.UnauthorizedException;
import co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.TiposUsuariosRepositories.I_UsuarioNaturalRepository;
import co.edu.udistrital.mdp.caminatas.services.NotificationServices.NotificationWaysServices.EmailService;

@Service
public class UsuarioNaturalService {

    private final EmailService emailService;
    private final I_UsuarioNaturalRepository usuarioNaturalRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioNaturalService(I_UsuarioNaturalRepository usuarioNaturalRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailService emailService) {
        this.usuarioNaturalRepository = usuarioNaturalRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    public UsuarioNaturalResponseDTO crearUsuarioNatural(UsuarioNaturalRequestDTO dto) {
        // Validaciones de unicidad y rol
        if (usuarioNaturalRepository.existsByCorreoIgnoreCase(dto.getCorreo())) {
            throw new ConflictException("Ya existe un usuario con el correo: " + dto.getCorreo());
        }
        if (usuarioNaturalRepository.existsByNombreUsuarioIgnoreCase(dto.getNombreUsuario())) {
            throw new ConflictException("Ya existe un usuario con el nombre: " + dto.getNombreUsuario());
        }
        if (usuarioNaturalRepository.existsByCedula(dto.getCedula())) {
            throw new ConflictException("Ya existe un usuario con la cédula: " + dto.getCedula());
        }

        if (dto.getRol() == null || dto.getRol() != RolUsuario.NATURAL) {
            throw new IllegalArgumentException("Solo se permite crear usuarios con rol NATURAL");
        }


        UsuarioNaturalEntity usuario = new UsuarioNaturalEntity();
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setRol(RolUsuario.NATURAL);
        usuario.setCorreo(dto.getCorreo());
        usuario.setCedula(dto.getCedula());
        usuario.setTelefono(dto.getTelefono());
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        usuario.setVerificado(false);
        usuario.setCodigoVerificacion(UUID.randomUUID().toString());

        UsuarioNaturalEntity guardado = usuarioNaturalRepository.save(usuario);

        enviarCorreoVerificacion(guardado);
        return toResponseDTO(guardado);
    }

    public void confirmarVerificacion(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El código de verificación es obligatorio.");
        }
        UsuarioNaturalEntity usuario = usuarioNaturalRepository.findAll().stream()
            .filter(u -> codigo.equals(u.getCodigoVerificacion()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Código de verificación inválido"));

        usuario.setVerificado(true);
        usuario.setCodigoVerificacion(null);
        usuarioNaturalRepository.save(usuario);
    }

    public LoginResponseDTO login(String identificador, String password) {
        Optional<UsuarioNaturalEntity> usuarioOpt = usuarioNaturalRepository.findAll()
            .stream()
            .filter(u -> u instanceof UsuarioNaturalEntity)
            .map(u -> (UsuarioNaturalEntity) u)
            .filter(u -> u.getCorreo().equalsIgnoreCase(identificador)
                    || u.getNombreUsuario().equalsIgnoreCase(identificador))
            .findFirst();

        if (usuarioOpt.isEmpty()) {
            throw new UnauthorizedException("Usuario o contraseña inválidos");
        }

        UsuarioNaturalEntity usuario = usuarioOpt.get();

        if (!Boolean.TRUE.equals(usuario.getVerificado())) {
            throw new UnauthorizedException("Tu cuenta aún no ha sido verificada.");
        }

        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new UnauthorizedException("Usuario o contraseña inválidos");
        }



        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol());
        return new LoginResponseDTO(usuario.getCorreo(), token, usuario.getRol());
    }

    public UsuarioNaturalResponseDTO obtenerPorCorreo(String correo) {
        UsuarioEntity usuario = usuarioNaturalRepository.findByCorreoIgnoreCase(correo)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (!(usuario instanceof UsuarioNaturalEntity natural)) {
            throw new NotFoundException("El usuario no es de tipo NATURAL");
        }

        return toResponseDTO(natural);
    }

    public UsuarioNaturalResponseDTO actualizarUsuarioNatural(String correo, UsuarioNaturalRequestDTO dto) {
        UsuarioNaturalEntity usuario = usuarioNaturalRepository.findByCorreoIgnoreCase(correo)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        if (!(usuario instanceof UsuarioNaturalEntity natural)) {
            throw new NotFoundException("El usuario no es de tipo NATURAL");
        }
        if (!usuario.getCorreo().equalsIgnoreCase(dto.getCorreo())) {
            throw new IllegalOperationException("No se permite cambiar el correo electrónico.");
        }

        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setRol(RolUsuario.NATURAL);
        usuario.setCorreo(dto.getCorreo());
        usuario.setCedula(dto.getCedula());
        usuario.setTelefono(dto.getTelefono());
        // Si quieres permitir actualizar contraseña, agrega aquí el hash

        return toResponseDTO(natural);
    }

    public void eliminarUsuarioNatural(String correo) {
        UsuarioNaturalEntity usuario = usuarioNaturalRepository.findByCorreoIgnoreCase(correo)
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (!(usuario instanceof UsuarioNaturalEntity natural)) {
            throw new NotFoundException("El usuario no es de tipo NATURAL");
        }

        usuarioNaturalRepository.deleteById(natural.getId());
    }


    public List<UsuarioNaturalResponseDTO> findAllDTO() {
        return usuarioNaturalRepository.findAll().stream()
            .filter(u -> u instanceof UsuarioNaturalEntity)
            .map(u -> (UsuarioNaturalEntity) u)
            .map(this::toResponseDTO)
            .toList();
    }

    public Optional<UsuarioNaturalEntity> findById(Long id) {
        return usuarioNaturalRepository.findById(id)
                .filter(u -> u instanceof UsuarioNaturalEntity)
                .map(u -> (UsuarioNaturalEntity) u);
    }

    public UsuarioNaturalResponseDTO toResponseDTO(UsuarioNaturalEntity entity) {
        return new UsuarioNaturalResponseDTO(
            entity.getId(),
            entity.getRol(),
            entity.getNombreUsuario(),
            entity.getCorreo(),
            entity.getCedula(),
            entity.getTelefono()
        );
    }

    private void enviarCorreoVerificacion(UsuarioNaturalEntity usuario) {
        String asunto = "Verifica tu cuenta en Caminatas";
        String enlace = "http://CaminatasColombia.com/verificar?codigo=" + usuario.getCodigoVerificacion();
        String cuerpo ="""
                Hola %s,

                Gracias por registrarte en Caminatas. Por favor verifica tu cuenta haciendo clic en el siguiente enlace:

                👉 %s

                Si no te registraste, ignora este correo.

                Equipo Caminatas
                """.formatted(usuario.getNombreUsuario(), enlace);
        emailService.enviarCorreo(usuario.getCorreo(), asunto, cuerpo);
    }
    // 🔹 MÉTODO NUEVO para obtener el código por correo
    public String obtenerCodigoVerificacionPorCorreo(String correo) {
        return usuarioNaturalRepository.findByCorreoIgnoreCase(correo)
                .map(UsuarioNaturalEntity::getCodigoVerificacion)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado o sin código de verificación."));
    }

}