package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComentarioRequestDTO {

    @Schema(description = "Descripción del comentario", example = "Fue emocionante y la vista preciosa")
    @NotBlank(message = "La descripción del comentario es obligatoria.")
    private String descripcionComentario;

    @Schema(description = "Calificación de la caminata (1-5) asociada al comentario", example = "5")
    @NotNull(message = "La calificación es obligatoria.")
    @Min(value = 1, message = "La calificación mínima es 1.")
    @Max(value = 5, message = "La calificación máxima es 5.")
    private Integer calificacion;

    @Schema(description = "ID del autor asociada al comentario", example = "1")
    @NotNull(message = "El ID del autor es obligatorio.")
    private Long idAutor;

    @Schema(description = "ID de la caminata asociada al comentario", example = "1")
    @NotNull(message = "El ID de la caminata es obligatorio.")
    private Long idCaminata;
}

