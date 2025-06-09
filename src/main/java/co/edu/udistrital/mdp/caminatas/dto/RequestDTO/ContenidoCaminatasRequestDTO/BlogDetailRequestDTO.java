package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BlogDetailRequestDTO {
    
    @Size(max = 10, message = "Máximo 10 imágenes por blog")
    private List<@NotBlank(message = "Las URLs de imágenes no pueden estar vacías") String> imagenes;

    @Size(max = 5, message = "Máximo 5 videos por blog")
    private List<@NotBlank(message = "Las URLs de videos no pueden estar vacías") String> videos;
}
