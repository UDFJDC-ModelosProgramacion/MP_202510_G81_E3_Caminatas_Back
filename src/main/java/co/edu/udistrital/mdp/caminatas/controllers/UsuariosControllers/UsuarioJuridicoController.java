package co.edu.udistrital.mdp.caminatas.controllers.UsuariosControllers;

import java.util.List;
import java.util.Map;

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

import co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration.CustomUserDetails;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.InicioSesionRequestDTO.LoginRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.UsuarioJuridicoRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InicioSesionResponseDTO.LoginResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.UsuariosResponsesDTO.UsuarioJuridicoResponseDTO;
import co.edu.udistrital.mdp.caminatas.services.UsuariosServices.UsuarioJuridicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios/juridicos")
@Tag(name = "Usuarios Jurídicos", description = "Operaciones para usuarios tipo jurídico")
@RequiredArgsConstructor
public class UsuarioJuridicoController {

    private final UsuarioJuridicoService usuarioService;

    @Operation(summary = "Registrar un nuevo usuario jurídico")
    @PostMapping
    public ResponseEntity<UsuarioJuridicoResponseDTO> create(@Valid @RequestBody UsuarioJuridicoRequestDTO dto) {
        UsuarioJuridicoResponseDTO response = usuarioService.crearUsuarioJuridico(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Método temporal para pruebas, borrar para producción
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/codigo-verificacion/{correo}")
    public ResponseEntity<String> obtenerCodigoVerificacion(@PathVariable String correo) {
        String codigo = usuarioService.obtenerCodigoVerificacionPorCorreo(correo);
        return ResponseEntity.ok(codigo);
    }

    @Operation(summary = "Verificar cuenta de usuario jurídico")
    @PostMapping("/verificacion/confirmar")
    public ResponseEntity<String> confirmar(@RequestBody Map<String, String> body) {
        usuarioService.confirmarVerificacion(body.get("codigo"));
        return ResponseEntity.ok("✅ Cuenta verificada exitosamente.");
    }

    @Operation(summary = "Iniciar sesión como usuario jurídico")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = usuarioService.login(request.getIdentificador(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener perfil del usuario jurídico autenticado")
    @PreAuthorize("hasRole('JURIDICO')")
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioJuridicoResponseDTO> perfil(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(usuarioService.obtenerPorCorreo(user.getCorreo()));
    }

    @Operation(summary = "Actualizar datos del usuario jurídico autenticado")
    @PreAuthorize("hasRole('JURIDICO')")
    @PutMapping("/actualizar")
    public ResponseEntity<UsuarioJuridicoResponseDTO> actualizarMiPerfil(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody UsuarioJuridicoRequestDTO dto) {
        UsuarioJuridicoResponseDTO actualizado = usuarioService.updateFromToken(user.getCorreo(), dto);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar cuenta del usuario jurídico autenticado")
    @PreAuthorize("hasRole('JURIDICO')")
    @DeleteMapping("/eliminar-cuenta")
    public ResponseEntity<Void> eliminarMiCuenta(@AuthenticationPrincipal CustomUserDetails user) {
        usuarioService.eliminarUsuarioJuridico(user.getCorreo());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener todos los usuarios jurídicos")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioJuridicoResponseDTO>> getAll() {
        return ResponseEntity.ok(usuarioService.findAllDTO());
    }
}
