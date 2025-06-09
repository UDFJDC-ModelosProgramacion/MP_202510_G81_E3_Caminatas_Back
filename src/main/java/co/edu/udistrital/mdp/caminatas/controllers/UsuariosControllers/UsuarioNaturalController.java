package co.edu.udistrital.mdp.caminatas.controllers.UsuariosControllers;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.InicioSesionRequestDTO.LoginRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.UsuarioNaturalRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InicioSesionResponseDTO.LoginResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.UsuariosResponsesDTO.UsuarioNaturalResponseDTO;
import co.edu.udistrital.mdp.caminatas.services.UsuariosServices.UsuarioNaturalService;
import co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration.CustomUserDetails;

@Tag(name = "Usuarios Naturales", description = "Operaciones relacionadas con usuarios tipo natural")
@RestController
@RequestMapping("/usuarios/naturales")
public class UsuarioNaturalController {

    private final UsuarioNaturalService usuarioService;

    public UsuarioNaturalController(UsuarioNaturalService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Crear un usuario natural")
    @PostMapping
    public ResponseEntity<UsuarioNaturalResponseDTO> create(@Valid @RequestBody UsuarioNaturalRequestDTO dto) {
        UsuarioNaturalResponseDTO creado = usuarioService.crearUsuarioNatural(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Confirmar verificación de cuenta")
    @PostMapping("/verificacion/confirmar")
    public ResponseEntity<String> confirmar(@RequestBody Map<String, String> body) {
        usuarioService.confirmarVerificacion(body.get("codigo"));
        return ResponseEntity.ok("Cuenta verificada exitosamente.");
    }

    @Operation(summary = "Login usuario natural")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(usuarioService.login(request.getIdentificador(), request.getPassword()));
    }

    //Método temporal para pruebas, borrar para producción
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/codigo-verificacion/{correo}")
    public ResponseEntity<String> obtenerCodigoVerificacion(@PathVariable String correo) {
        String codigo = usuarioService.obtenerCodigoVerificacionPorCorreo(correo);
        return ResponseEntity.ok(codigo);
    }

    @Operation(summary = "Obtener perfil del usuario autenticado")
    @GetMapping("/perfil")
    @PreAuthorize("hasRole('NATURAL') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UsuarioNaturalResponseDTO> perfil(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(usuarioService.obtenerPorCorreo(user.getCorreo()));
    }

    @Operation(summary = "Actualizar perfil del usuario autenticado")
    @PutMapping("/actualizar-perfil")
    @PreAuthorize("hasRole('NATURAL')")
    public ResponseEntity<UsuarioNaturalResponseDTO> actualizarPerfil(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody UsuarioNaturalRequestDTO dto) {
        UsuarioNaturalResponseDTO actualizado = usuarioService.actualizarUsuarioNatural(user.getCorreo(), dto);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar cuenta del usuario autenticado")
    @DeleteMapping("/eliminar-cuenta")
    @PreAuthorize("hasRole('NATURAL')")
    public ResponseEntity<Void> eliminarCuenta(@AuthenticationPrincipal CustomUserDetails user) {
        usuarioService.eliminarUsuarioNatural(user.getCorreo());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos los usuarios naturales")
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<UsuarioNaturalResponseDTO>> getAll() {
        return ResponseEntity.ok(usuarioService.findAllDTO());
    }

    @Operation(summary = "Obtener un usuario natural por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UsuarioNaturalResponseDTO> getById(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(usuarioService::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
