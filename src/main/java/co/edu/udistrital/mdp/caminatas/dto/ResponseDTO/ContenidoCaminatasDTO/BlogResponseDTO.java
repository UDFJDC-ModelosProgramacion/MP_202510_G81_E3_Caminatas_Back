package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO;

import lombok.Data;
import java.util.List;

@Data
public class BlogResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String contenidoTextoBlog;
    private List<String> imagenes;
    private List<String> videos;

    private AutorDTO autor;

    @Data
    public static class AutorDTO {
        private Long id;
        private String nombreUsuario;
        private String correo;
    }
}
