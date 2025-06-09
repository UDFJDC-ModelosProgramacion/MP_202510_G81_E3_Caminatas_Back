package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class BlogRequestDTO {

    @NotBlank(message = "El título no puede estar vacío")
    private String title;

    @NotBlank(message = "El contenido no puede estar vacío")
    private String content;

    @NotNull(message = "Debe especificar el autor del blog")
    private Long autorId;

    @Size(max = 10, message = "Máximo 10 imágenes por blog")
    private List<@NotBlank(message = "Las URLs de imágenes no pueden estar vacías") String> imagenes;

    @Size(max = 5, message = "Máximo 5 videos por blog")
    private List<@NotBlank(message = "Las URLs de videos no pueden estar vacías") String> videos;

    private String contenidoTextoBlog;
}

