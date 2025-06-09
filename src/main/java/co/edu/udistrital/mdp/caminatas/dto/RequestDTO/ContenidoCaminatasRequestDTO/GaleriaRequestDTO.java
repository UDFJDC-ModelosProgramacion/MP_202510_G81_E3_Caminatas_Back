package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GaleriaRequestDTO {

    @NotBlank(message = "La imagen principal no puede estar vacía")
    private String imagenPrincipal;

    @NotNull(message = "Debe proporcionar al menos una imagen")
    @Size(min = 1, max = 20, message = "Debe incluir entre 1 y 20 imágenes.")
    private List<@NotBlank(message = "Las imágenes de la galería no pueden estar vacías") String> imagenesGaleria;
}

