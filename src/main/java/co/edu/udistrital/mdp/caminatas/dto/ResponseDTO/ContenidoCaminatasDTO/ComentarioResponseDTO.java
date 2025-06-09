package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO;

import lombok.Data;

@Data
public class ComentarioResponseDTO {
    private Long id;
    private String descripcionComentario;
    private Integer calificacion;
    private String estadoComentario;
    private String nombreAutor;
    private String nombreCaminata;
}