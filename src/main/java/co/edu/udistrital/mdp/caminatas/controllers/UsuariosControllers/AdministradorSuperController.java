package co.edu.udistrital.mdp.caminatas.controllers.UsuariosControllers;

import co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration.CustomUserDetails;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.InicioSesionRequestDTO.LoginRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.AdministradorComentariosRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.UsuarioJuridicoRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO.UsuarioNaturalRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InicioSesionResponseDTO.LoginResponseDTO;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.UsuariosResponsesDTO.UsuarioResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import co.edu.udistrital.mdp.caminatas.services.UsuariosServices.AdministradorSuperService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Administrador Super", description = "Operaciones restringidas al SuperAdministrador del sistema")
@RestController
@RequestMapping("/admin/super")
@RequiredArgsConstructor
public class AdministradorSuperController {

    private final AdministradorSuperService adminSuperService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(adminSuperService.login(dto.getIdentificador(), dto.getPassword()));
    }

    @Operation(summary = "Ver perfil del SuperAdmin autenticado")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponseDTO> perfil(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UsuarioEntity user = adminSuperService.buscarPorCorreo(userDetails.getCorreo());
        return ResponseEntity.ok(adminSuperService.toDTO(user));
    }

    @Operation(summary = "Listar todos los usuarios del sistema")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> getAllUsuarios() {
        List<UsuarioResponseDTO> lista = adminSuperService.listarTodos().stream()
            .map(adminSuperService::toDTO)
            .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener usuarios por rol", description = "Filtra los usuarios del sistema por su rol")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/usuarios/rol/{rol}")
    public ResponseEntity<List<UsuarioResponseDTO>> getUsuariosPorRol(@PathVariable RolUsuario rol) {
        List<UsuarioResponseDTO> usuarios = adminSuperService.buscarPorRol(rol).stream()
            .map(adminSuperService::toDTO)
            .toList();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Obtener un usuario por ID")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Long id) {
        UsuarioEntity usuario = adminSuperService.buscarPorId(id);
        return ResponseEntity.ok(adminSuperService.toDTO(usuario));
    }

    @Operation(summary = "Actualizar perfil del SuperAdministrador")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/perfil")
    public ResponseEntity<UsuarioResponseDTO> actualizarPerfil(@Valid @RequestBody AdministradorComentariosRequestDTO dto) {
        UsuarioResponseDTO actualizado = adminSuperService.actualizarSuperAdmin(dto);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Actualizar usuario natural por ID")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/usuarios/natural/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarNatural(@PathVariable Long id, @RequestBody UsuarioNaturalRequestDTO dto) {
        return ResponseEntity.ok(adminSuperService.actualizarUsuarioNatural(id, dto));
    }

    @Operation(summary = "Actualizar usuario jurídico por ID")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/usuarios/juridico/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarJuridico(@PathVariable Long id, @RequestBody UsuarioJuridicoRequestDTO dto) {
        return ResponseEntity.ok(adminSuperService.actualizarUsuarioJuridico(id, dto));
    }

    @Operation(summary = "Actualizar administrador de comentarios por ID")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/usuarios/admin-comentarios/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarAdminComentarios(@PathVariable Long id, @RequestBody AdministradorComentariosRequestDTO dto) {
        return ResponseEntity.ok(adminSuperService.actualizarUsuarioAdministradorComentarios(id, dto));
    }

    @Operation(summary = "Eliminar cualquier usuario por ID", description = "No se puede eliminar al SuperAdmin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        adminSuperService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }


    //METODO TEMPORAL PARA VALIDAR TODOS LOS USUARIOS, QUITAR EN PRODUCCIÓN
    @Operation(summary = "Verificar todos los usuarios del sistema (uso temporal)")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/usuarios/verificar-todos")
    public ResponseEntity<String> verificarTodosLosUsuarios() {
        int total = adminSuperService.verificarTodosLosUsuariosDelSistema();
        return ResponseEntity.ok("✅ Total de usuarios verificados automáticamente: " + total);
    }



    /*

    // Este método se puede usar para mayor seguridad en el login del SuperAdmin
    //En este caso, lo voy a dejar comentado para que no interfiera con el funcionamiento normal de la aplicación
    // pero se puede descomentar si se desea implementar una seguridad adicional.
    // Pasar lógica al servicio
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (!ip.equals("127.0.0.1") && !ip.equals("tu-ip-autorizada")) {
            logger.warn("❌ Intento de acceso no autorizado al SuperAdmin desde IP: " + ip);
            throw new UnauthorizedException("IP no autorizada");
        }
        return ResponseEntity.ok(adminSuperService.login(dto.getIdentificador(), dto.getPassword()));
    }
        
    */


}

