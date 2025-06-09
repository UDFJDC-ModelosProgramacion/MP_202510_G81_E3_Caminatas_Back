package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.NotificacionRequestDTO;

import lombok.Data;

@Data
public class NotificacionRequestDTO {
    private Long usuarioId;
    private String titulo;
    private String mensaje;

}
