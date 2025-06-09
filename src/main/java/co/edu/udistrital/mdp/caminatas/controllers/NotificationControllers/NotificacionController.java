package co.edu.udistrital.mdp.caminatas.controllers.NotificationControllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.caminatas.config.SecurityConfiguration.CustomUserDetails;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.NotificacionResponsesDTO.NotificacionResponseDTO;
import co.edu.udistrital.mdp.caminatas.services.NotificationServices.NotificacionInternaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionInternaService service;

    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> listar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Boolean noLeidas) {

        String correo = userDetails.getCorreo();
        List<NotificacionResponseDTO> notificaciones = service.listarPorFiltro(correo, noLeidas);
        return ResponseEntity.ok(notificaciones);
    }

    @PatchMapping("/{id}/leida")
    public ResponseEntity<Void> marcarComoLeida(@PathVariable Long id) {
        service.marcarComoLeida(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}