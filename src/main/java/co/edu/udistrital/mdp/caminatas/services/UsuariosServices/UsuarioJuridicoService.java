package co.edu.udistrital.mdp.caminatas.services.UsuariosServices;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration.JwtUtil;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.UsuarioJuridicoRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InicioSesionResponseDTO.LoginResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.UsuariosResponsesDTO.UsuarioJuridicoResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioJuridicoEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.ConflictException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.UnauthorizedException;
import co.edu.udistrital.mdp.caminatas.repositories.UsuariosRepositories.TiposUsuariosRepositories.I_UsuarioJuridicoRepository;
import co.edu.udistrital.mdp.caminatas.services.NotificationServices.NotificationWaysServices.EmailService;
import co.edu.udistrital.mdp.caminatas.exceptions.http.BadRequestException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioJuridicoService {

    private final EmailService emailService;
    private final I_UsuarioJuridicoRepository usuarioJuridicoRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Crear usuario jurídico y enviar correo de verificación
    public UsuarioJuridicoResponseDTO crearUsuarioJuridico(UsuarioJuridicoRequestDTO dto) {
        if (usuarioJuridicoRepository.existsByCorreoIgnoreCase(dto.getCorreo())) {
            throw new ConflictException("Correo ya registrado");
        }
        if (usuarioJuridicoRepository.existsByNombreUsuarioIgnoreCase(dto.getNombreUsuario())) {
            throw new ConflictException("Nombre de usuario ya registrado");
        }
        if (usuarioJuridicoRepository.existsByCedula(dto.getCedula())) {
            throw new ConflictException("Cédula ya registrada");
        }
        if (dto.getRol() == null) {
            throw new IllegalArgumentException("El campo 'rol' no puede ser nulo");
        }
        if (dto.getRol() == RolUsuario.SUPER_ADMIN) {
            throw new IllegalArgumentException("No está permitido crear usuarios con rol SUPER_ADMIN");
        }
        if (dto.getRol() != RolUsuario.JURIDICO) {
            throw new IllegalArgumentException("Este endpoint solo permite crear usuarios con rol JURIDICO");
        }
        if (dto.getNombresParticipantes() == null || dto.getNombresParticipantes().isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos un participante");
        }

        UsuarioJuridicoEntity usuario = new UsuarioJuridicoEntity();
        usuario.setRol(dto.getRol());
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setCorreo(dto.getCorreo());
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        usuario.setCedula(dto.getCedula());
        usuario.setTelefono(dto.getTelefono());
        usuario.setNombreEmpresa(dto.getNombreEmpresa());
        usuario.setNombresParticipantes(dto.getNombresParticipantes());
        usuario.setNumParticipantes(dto.getNombresParticipantes().size());
        usuario.setVerificado(false);
        usuario.setCodigoVerificacion(UUID.randomUUID().toString());

        UsuarioJuridicoEntity guardado = usuarioJuridicoRepository.save(usuario);
        enviarCorreoVerificacion(guardado);
        return toResponseDTO(guardado);
    }

    // Enviar correo de verificación
    private void enviarCorreoVerificacion(UsuarioJuridicoEntity usuario) {
        String asunto = "Verifica tu cuenta jurídica en Caminatas";
        String enlace = "http://caminatascolombia.com/verificar?codigo=" + usuario.getCodigoVerificacion();
        String cuerpo ="""
            Hola %s,

            Gracias por registrarte como usuario jurídico en Caminatas.

            Verifica tu cuenta haciendo clic en el siguiente enlace:

            👉 %s

            Si no fuiste tú, ignora este correo.

            Equipo Caminatas
            """.formatted(usuario.getNombreUsuario(), enlace);

        emailService.enviarCorreo(usuario.getCorreo(), asunto, cuerpo);
    }
    // 🔹 MÉTODO NUEVO para obtener el código por correo
    public String obtenerCodigoVerificacionPorCorreo(String correo) {
        return usuarioJuridicoRepository.findByCorreoIgnoreCase(correo)
                .map(UsuarioJuridicoEntity::getCodigoVerificacion)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado o sin código de verificación."));
    }

    // Confirmar verificación
    public void confirmarVerificacion(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new BadRequestException("El código de verificación es obligatorio.");
        }
        UsuarioJuridicoEntity usuario = usuarioJuridicoRepository.findAll().stream()
            .filter(u -> codigo.equals(u.getCodigoVerificacion()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Código de verificación inválido"));

        usuario.setVerificado(true);
        usuario.setCodigoVerificacion(null);
        usuarioJuridicoRepository.save(usuario);
    }

    // Login
    public LoginResponseDTO login(String identificador, String password) {
        Optional<UsuarioJuridicoEntity> usuarioOpt = usuarioJuridicoRepository.findAll().stream()
            .filter(u -> u instanceof UsuarioJuridicoEntity)
            .map(u -> (UsuarioJuridicoEntity) u)
            .filter(u -> u.getCorreo().equalsIgnoreCase(identificador)
                    || u.getNombreUsuario().equalsIgnoreCase(identificador))
            .findFirst();

        if (usuarioOpt.isEmpty()) {
            throw new UnauthorizedException("Usuario o contraseña inválidos");
        }

        UsuarioJuridicoEntity usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new UnauthorizedException("Usuario o contraseña inválidos");
        }

        if (!Boolean.TRUE.equals(usuario.getVerificado())) {
            throw new UnauthorizedException("Tu cuenta aún no ha sido verificada.");
        }

        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol());
        return new LoginResponseDTO(usuario.getCorreo(), token, usuario.getRol());
    }

    // Obtener perfil por correo
    public UsuarioJuridicoResponseDTO obtenerPorCorreo(String correo) {
        UsuarioJuridicoEntity usuario = usuarioJuridicoRepository.findAll().stream()
            .filter(u -> u instanceof UsuarioJuridicoEntity)
            .map(u -> (UsuarioJuridicoEntity) u)
            .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return toResponseDTO(usuario);
    }

    // Actualizar perfil desde token
    public UsuarioJuridicoResponseDTO updateFromToken(String correoToken, UsuarioJuridicoRequestDTO dto) {
        UsuarioJuridicoEntity usuario = usuarioJuridicoRepository.findAll().stream()
            .filter(u -> u instanceof UsuarioJuridicoEntity)
            .map(u -> (UsuarioJuridicoEntity) u)
            .filter(u -> u.getCorreo().equalsIgnoreCase(correoToken))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (dto.getNombresParticipantes() == null || dto.getNombresParticipantes().isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos un participante");
        }
        if (dto.getCedula() == null || dto.getCedula() <= 0) {
            throw new IllegalArgumentException("La cédula debe ser un número válido.");
        }
        if (dto.getTelefono() == null || dto.getTelefono() <= 0) {
            throw new IllegalArgumentException("El teléfono debe ser un número válido.");
        }
        if (dto.getCorreo() == null || dto.getCorreo().isBlank()) {
            throw new IllegalArgumentException("El correo no puede estar vacío.");
        }

        // Validaciones solo si cambia el valor
        if (!usuario.getCorreo().equalsIgnoreCase(dto.getCorreo()) &&
            usuarioJuridicoRepository.existsByCorreoIgnoreCase(dto.getCorreo())) {
            throw new ConflictException("Correo ya registrado");
        }
        if (!usuario.getNombreUsuario().equalsIgnoreCase(dto.getNombreUsuario()) &&
            usuarioJuridicoRepository.existsByNombreUsuarioIgnoreCase(dto.getNombreUsuario())) {
            throw new ConflictException("Nombre de usuario ya registrado");
        }
        if (!usuario.getCedula().equals(dto.getCedula()) &&
            usuarioJuridicoRepository.existsByCedula(dto.getCedula())) {
            throw new ConflictException("Cédula ya registrada");
        }

        // Actualización de campos
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setCorreo(dto.getCorreo());
        usuario.setCedula(dto.getCedula());
        usuario.setTelefono(dto.getTelefono());
        usuario.setNombreEmpresa(dto.getNombreEmpresa());
        usuario.setNombresParticipantes(dto.getNombresParticipantes());
        usuario.setNumParticipantes(dto.getNombresParticipantes().size());

        // Si viene una nueva contraseña
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        UsuarioJuridicoEntity actualizado = usuarioJuridicoRepository.save(usuario);
        return toResponseDTO(actualizado);
    }

    // Eliminar usuario jurídico por correo
    public void eliminarUsuarioJuridico(String correo) {
        UsuarioJuridicoEntity usuario = usuarioJuridicoRepository.findAll().stream()
            .filter(u -> u instanceof UsuarioJuridicoEntity)
            .map(u -> (UsuarioJuridicoEntity) u)
            .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        usuarioJuridicoRepository.deleteById(usuario.getId());
    }

    // Obtener todos los usuarios jurídicos como DTO
    public List<UsuarioJuridicoResponseDTO> findAllDTO() {
        return usuarioJuridicoRepository.findAll().stream()
            .filter(u -> u instanceof UsuarioJuridicoEntity)
            .map(u -> (UsuarioJuridicoEntity) u)
            .map(this::toResponseDTO)
            .toList();
    }

    // Mapear entidad a DTO
    public UsuarioJuridicoResponseDTO toResponseDTO(UsuarioJuridicoEntity e) {
        UsuarioJuridicoResponseDTO dto = new UsuarioJuridicoResponseDTO();
        dto.setId(e.getId());
        dto.setRol(e.getRol());
        dto.setNombreUsuario(e.getNombreUsuario());
        dto.setCorreo(e.getCorreo());
        dto.setCedula(e.getCedula());
        dto.setTelefono(e.getTelefono());
        dto.setNombreEmpresa(e.getNombreEmpresa());
        dto.setNumParticipantes(e.getNumParticipantes());
        dto.setNombresParticipantes(e.getNombresParticipantes());
        return dto;
    }

}