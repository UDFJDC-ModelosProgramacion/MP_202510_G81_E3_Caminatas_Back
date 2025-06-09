package co.edu.udistrital.mdp.caminatas.services.NotificationServices;

import java.util.List;

import org.springframework.stereotype.Service;
import co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.NotificacionResponsesDTO.NotificacionResponseDTO;
import co.edu.udistrital.mdp.caminatas.entities.NotificationEntities.NotificacionEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.repositories.NotificationRepositories.I_NotificacionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificacionInternaService {

    private final I_NotificacionRepository notificacionRepository;

    public void enviarNotificacion(UsuarioEntity usuario, String titulo, String mensaje) {
        NotificacionEntity noti = new NotificacionEntity();
        noti.setUsuario(usuario);
        noti.setTitulo(titulo);
        noti.setMensaje(mensaje);
        notificacionRepository.save(noti);
    }

    public List<NotificacionResponseDTO> listarPorFiltro(String correo, Boolean noLeidas) {
    if (Boolean.TRUE.equals(noLeidas)) {
        return listarNoLeidas(correo);
    } else {
        return listarTodas(correo);
    }
}

    public List<NotificacionResponseDTO> listarTodas(String correo) {
        return notificacionRepository.findByUsuarioCorreoOrderByFechaEnvioDesc(correo)
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }

    public List<NotificacionResponseDTO> listarNoLeidas(String correo) {
        return notificacionRepository.findByUsuarioCorreoAndLeidaFalseOrderByFechaEnvioDesc(correo)
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }

    public void marcarComoLeida(Long id) {
        NotificacionEntity notificacion = notificacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Notificación no encontrada"));
        notificacion.setLeida(true);
        notificacionRepository.save(notificacion);
    }

    public void eliminar(Long id) {
        if (!notificacionRepository.existsById(id)) {
            throw new NotFoundException("Notificación no encontrada");
        }
        notificacionRepository.deleteById(id);
    }

    private NotificacionResponseDTO toResponseDTO(NotificacionEntity entity) {
        NotificacionResponseDTO dto = new NotificacionResponseDTO();
        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        dto.setMensaje(entity.getMensaje());
        dto.setLeida(entity.getLeida());
        dto.setFechaEnvio(entity.getFechaEnvio());
        return dto;
    }
}