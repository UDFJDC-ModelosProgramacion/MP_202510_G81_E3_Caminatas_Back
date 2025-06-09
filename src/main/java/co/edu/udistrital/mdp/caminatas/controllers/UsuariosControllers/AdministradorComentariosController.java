package co.edu.udistrital.mdp.caminatas.controllers.UsuariosControllers;

import co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration.CustomUserDetails;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.InicioSesionRequestDTO.LoginRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.AdministradorComentariosRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO.ComentarioResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InicioSesionResponseDTO.LoginResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.UsuariosResponsesDTO.UsuarioAdministradorComentariosResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import co.edu.udistrital.mdp.caminatas.services.UsuariosServices.AdministradorComentariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Administrador Comentarios de la App", description = "Operaciones disponibles para administradores (comentarios)")
@RestController
@RequestMapping("/usuarios/admin-comentarios")
@RequiredArgsConstructor
public class AdministradorComentariosController {

    private final AdministradorComentariosService adminComentariosService;

    @Operation(summary = "Crear un nuevo administrador de comentarios de la app", description = "Solo accesible para SuperAdmin")
    @ApiResponse(responseCode = "201", description = "Administrador creado correctamente")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<UsuarioAdministradorComentariosResponseDTO> crearAdmin(@Valid @RequestBody AdministradorComentariosRequestDTO dto) {
        UsuarioAdministradorComentariosResponseDTO creado = adminComentariosService.crearAdministradorComentarios(dto);
        return ResponseEntity.status(201).body(creado);
    }

    @Operation(summary = "Login administrador de comentarios")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = adminComentariosService.login(request.getIdentificador(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    //Método temporal para pruebas, borrar para producción
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/codigo-verificacion/{correo}")
    public ResponseEntity<String> obtenerCodigoVerificacion(@PathVariable String correo) {
        String codigo = adminComentariosService.obtenerCodigoVerificacionPorCorreo(correo);
        return ResponseEntity.ok(codigo);
    }

    @Operation(summary = "Verificar cuenta de usuario administrador de comentarios")
    @ApiResponse(responseCode = "200", description = "Cuenta verificada exitosamente")
    @ApiResponse(responseCode = "404", description = "Código de verificación no encontrado")
    @ApiResponse(responseCode = "400", description = "Código de verificación inválido")
    @PostMapping("/verificacion/confirmar")
    public ResponseEntity<String> confirmar(@RequestBody Map<String, String> body) {
        adminComentariosService.confirmarVerificacion(body.get("codigo"));
        return ResponseEntity.ok("✅ Cuenta verificada exitosamente.");
    }

    @Operation(summary = "Listar todos los comentarios del sistema")
    @PreAuthorize("hasAnyRole('ADMIN_COMENTARIOS', 'SUPER_ADMIN')")
    @GetMapping("/comentarios")
    public ResponseEntity<List<ComentarioResponseDTO>> listarComentarios() {
        return ResponseEntity.ok(adminComentariosService.findAllComentariosDTO());
    }

    @Operation(summary = "Obtener un comentario por su ID")
    @PreAuthorize("hasAnyRole('ADMIN_COMENTARIOS', 'SUPER_ADMIN')")
    @GetMapping("/comentarios/{id}")
    public ResponseEntity<ComentarioResponseDTO> obtenerComentarioPorId(@PathVariable Long id) {
        ComentarioResponseDTO comentario = adminComentariosService.findComentarioDTOById(id);
        return ResponseEntity.ok(comentario);
    }

    @Operation(summary = "Obtener comentarios por ID de caminata")
    @PreAuthorize("hasAnyRole('ADMIN_COMENTARIOS', 'SUPER_ADMIN')")
    @GetMapping("/comentarios/caminata/{caminataId}")
    public ResponseEntity<List<ComentarioResponseDTO>> comentariosPorCaminata(@PathVariable Long caminataId) {
        return ResponseEntity.ok(adminComentariosService.findByCaminataIdDTO(caminataId));
    }

    @Operation(summary = "Buscar un comentario específico dentro de una caminata")
    @PreAuthorize("hasAnyRole('ADMIN_COMENTARIOS', 'SUPER_ADMIN')")
    @GetMapping("/comentarios/caminata/{caminataId}/comentario/{comentarioId}")
    public ResponseEntity<ComentarioResponseDTO> obtenerComentarioEspecifico(
            @PathVariable Long caminataId,
            @PathVariable Long comentarioId) {
        ComentarioResponseDTO comentario = adminComentariosService.findComentarioByIdAndCaminataIdDTO(comentarioId, caminataId);
        return ResponseEntity.ok(comentario);
    }

    @Operation(summary = "Obtener perfil del administrador de comentarios autenticado")
    @PreAuthorize("hasRole('ADMIN_COMENTARIOS')")
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioAdministradorComentariosResponseDTO> obtenerPerfil(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UsuarioEntity usuario = adminComentariosService.buscarPorCorreo(userDetails.getCorreo());
        return ResponseEntity.ok(adminComentariosService.toDTO(usuario));
    }

    @Operation(summary = "Actualizar perfil del administrador de comentarios")
    @PreAuthorize("hasRole('ADMIN_COMENTARIOS')")
    @PutMapping("/actualizar-perfil")
    public ResponseEntity<UsuarioAdministradorComentariosResponseDTO> actualizarPerfil(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AdministradorComentariosRequestDTO dto) {
        UsuarioAdministradorComentariosResponseDTO actualizado = adminComentariosService.actualizarPerfil(userDetails.getCorreo(), dto);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar cuenta del administrador de comentarios")
    @PreAuthorize("hasRole('ADMIN_COMENTARIOS')")
    @DeleteMapping("/eliminar-cuenta")
    public ResponseEntity<Void> eliminarMiCuenta(@AuthenticationPrincipal CustomUserDetails userDetails) {
        adminComentariosService.eliminarPorCorreo(userDetails.getCorreo());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar un comentario por ID")
    @PreAuthorize("hasAnyRole('ADMIN_COMENTARIOS', 'SUPER_ADMIN')")
    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
        adminComentariosService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}