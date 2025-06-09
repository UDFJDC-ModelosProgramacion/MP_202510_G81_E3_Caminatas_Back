package co.edu.udistrital.mdp.caminatas.services.UsuariosServices;

import co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration.JwtUtil;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.AdministradorComentariosRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.ComentarioResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InicioSesionResponseDTO.LoginResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.UsuariosResponsesDTO.UsuarioAdministradorComentariosResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.ComentariosEntities.ComentariosEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioAdministradorComentariosEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.BadRequestException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.ConflictException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.UnauthorizedException;
import co.edu.udistrital.mdp.caminatas.repositories.ContenidoCaminatasRepositories.I_ComentariosRepository;
import co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.TiposUsuariosRepositories.I_UsuarioAdministradorComentariosRepository;
import co.edu.udistrital.mdp.caminatas.services.NotificationServices.NotificationWaysServices.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdministradorComentariosService {

    private final I_UsuarioAdministradorComentariosRepository usuarioAdministradorComentariosRepository;
    private final I_ComentariosRepository comentariosRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public UsuarioAdministradorComentariosResponseDTO crearAdministradorComentarios(AdministradorComentariosRequestDTO dto) {
        if (dto.getRol() == null) {
            throw new BadRequestException("El campo 'rol' no puede ser nulo");
        }
        if (dto.getRol() != RolUsuario.ADMIN_COMENTARIOS) {
            throw new BadRequestException("Este endpoint solo permite crear usuarios con rol ADMIN_COMENTARIOS");
        }
        if (usuarioAdministradorComentariosRepository.existsByCorreoIgnoreCase(dto.getCorreo())) {
            throw new ConflictException("Correo ya registrado");
        }
        if (usuarioAdministradorComentariosRepository.existsByNombreUsuarioIgnoreCase(dto.getNombreUsuario())) {
            throw new ConflictException("Nombre de usuario ya registrado");
        }
        if (usuarioAdministradorComentariosRepository.existsByCedula(dto.getCedula())) {
            throw new ConflictException("Cédula ya registrada");
        }

        UsuarioAdministradorComentariosEntity admin = new UsuarioAdministradorComentariosEntity();
        admin.setRol(dto.getRol());
        admin.setNombreUsuario(dto.getNombreUsuario());
        admin.setCorreo(dto.getCorreo());
        admin.setCedula(dto.getCedula());
        admin.setTelefono(dto.getTelefono());
        admin.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        admin.setVerificado(false);
        admin.setCodigoVerificacion(UUID.randomUUID().toString());

        usuarioAdministradorComentariosRepository.save(admin);
        enviarCorreoVerificacion(admin);

        return toDTO(admin);
    }

    private void enviarCorreoVerificacion(UsuarioAdministradorComentariosEntity admin) {
        String asunto = "Verifica tu cuenta de Administrador de Comentarios - Caminatas";
        String enlace = "http://caminatascolombia.com/verificar?codigo=" + admin.getCodigoVerificacion();
        String cuerpo ="""
                Hola %s,

                Has sido registrado como administrador de comentarios en Caminatas Colombia.

                Por favor verifica tu cuenta haciendo clic en el siguiente enlace seguro:

                🔗 %s

                Este enlace es válido por un tiempo limitado. Si tú no solicitaste esta cuenta, ignora este mensaje.

                Fecha de registro: %s

                Equipo Caminatas Colombia.
                """.formatted(
                admin.getNombreUsuario(),
                enlace,
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
        emailService.enviarCorreo(admin.getCorreo(), asunto, cuerpo);
    }

    // 🔹 MÉTODO NUEVO para obtener el código por correo
    public String obtenerCodigoVerificacionPorCorreo(String correo) {
        return usuarioAdministradorComentariosRepository.findByCorreoIgnoreCase(correo)
                .map(UsuarioAdministradorComentariosEntity::getCodigoVerificacion)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado o sin código de verificación."));
    }

    public void confirmarVerificacion(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El código de verificación es obligatorio.");
        }
        UsuarioAdministradorComentariosEntity usuario = usuarioAdministradorComentariosRepository.findAll().stream()
            .filter(u -> codigo.equals(u.getCodigoVerificacion()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Código de verificación inválido"));

        usuario.setVerificado(true);
        usuario.setCodigoVerificacion(null);
        usuarioAdministradorComentariosRepository.save(usuario);
    }

    public LoginResponseDTO login(String identificador, String password) {
        Optional<UsuarioAdministradorComentariosEntity> usuarioOpt = usuarioAdministradorComentariosRepository.findAll().stream()
            .filter(u -> u.getCorreo().equalsIgnoreCase(identificador)
                    || u.getNombreUsuario().equalsIgnoreCase(identificador))
            .findFirst();

        if (usuarioOpt.isEmpty()) {
            throw new UnauthorizedException("Usuario o contraseña inválidos");
        }

        UsuarioAdministradorComentariosEntity usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new UnauthorizedException("Usuario o contraseña inválidos");
        }
        if (usuario.getRol() != RolUsuario.ADMIN_COMENTARIOS) {
            throw new UnauthorizedException("Acceso denegado: rol incorrecto");
        }
        if (!Boolean.TRUE.equals(usuario.getVerificado())) {
            throw new UnauthorizedException("Tu cuenta aún no ha sido verificada.");
        }

        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol());
        return new LoginResponseDTO(usuario.getCorreo(), token, usuario.getRol());
    }

    public List<ComentarioResponseDTO> findAllComentariosDTO() {
        return comentariosRepository.findAll().stream().map(this::toComentarioDTO).toList();
    }

    public ComentarioResponseDTO findComentarioDTOById(Long id) {
        ComentariosEntity comentario = comentariosRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Comentario con ID " + id + " no encontrado"));
        return toComentarioDTO(comentario);
    }

    public List<ComentarioResponseDTO> findByCaminataIdDTO(Long caminataId) {
        return comentariosRepository.findByCaminata_Id(caminataId).stream()
                .map(this::toComentarioDTO)
                .toList();
    }

    public ComentarioResponseDTO findComentarioByIdAndCaminataIdDTO(Long comentarioId, Long caminataId) {
        ComentariosEntity comentario = comentariosRepository.findByIdAndCaminata_Id(comentarioId, caminataId)
                .orElseThrow(() -> new NoSuchElementException("Comentario no encontrado para esa caminata"));
        return toComentarioDTO(comentario);
    }

    public void deleteById(Long id) {
        if (!comentariosRepository.existsById(id)) {
            throw new NoSuchElementException("Comentario no encontrado");
        }
        comentariosRepository.deleteById(id);
    }

    public UsuarioAdministradorComentariosEntity buscarPorCorreo(String correo) {
        return usuarioAdministradorComentariosRepository.findAll().stream()
            .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Administrador no encontrado"));
    }

    public void eliminarPorCorreo(String correo) {
        UsuarioAdministradorComentariosEntity usuario = buscarPorCorreo(correo);
        usuarioAdministradorComentariosRepository.deleteById(usuario.getId());
    }

    public UsuarioAdministradorComentariosResponseDTO toDTO(UsuarioEntity entity) {
        if (!(entity instanceof UsuarioAdministradorComentariosEntity admin)) {
            throw new IllegalArgumentException("El usuario no es de tipo ADMIN_COMENTARIOS");
        }
        return new UsuarioAdministradorComentariosResponseDTO(
            admin.getId(),
            admin.getRol(),
            admin.getNombreUsuario(),
            admin.getCorreo(),
            admin.getCedula(),
            admin.getTelefono()
        );
    }

    public UsuarioAdministradorComentariosResponseDTO actualizarPerfil(String correo, AdministradorComentariosRequestDTO dto) {
        UsuarioAdministradorComentariosEntity entity = buscarPorCorreo(correo);

        entity.setNombreUsuario(dto.getNombreUsuario());
        entity.setCorreo(dto.getCorreo());
        entity.setTelefono(dto.getTelefono());

        usuarioAdministradorComentariosRepository.save(entity);

        return toDTO(entity);
    }

    private ComentarioResponseDTO toComentarioDTO(ComentariosEntity comentario) {
        ComentarioResponseDTO dto = new ComentarioResponseDTO();
        dto.setId(comentario.getId());
        dto.setDescripcionComentario(comentario.getDescripcionComentario());
        dto.setCalificacion(comentario.getCalificacion());
        dto.setEstadoComentario(comentario.getEstadoComentario() != null ? comentario.getEstadoComentario().name() : null);
        dto.setNombreAutor(comentario.getAutor() != null ? comentario.getAutor().getNombreUsuario() : null);
        dto.setNombreCaminata(comentario.getCaminata() != null ? comentario.getCaminata().getNombreCaminata() : null);
        return dto;
    }
}
