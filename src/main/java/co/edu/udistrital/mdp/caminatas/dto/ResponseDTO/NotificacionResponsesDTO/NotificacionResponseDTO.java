package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.NotificacionResponsesDTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacionResponseDTO {
    private Long id;
    private String titulo;
    private String mensaje;
    private boolean leida;
    private LocalDateTime fechaEnvio;
}