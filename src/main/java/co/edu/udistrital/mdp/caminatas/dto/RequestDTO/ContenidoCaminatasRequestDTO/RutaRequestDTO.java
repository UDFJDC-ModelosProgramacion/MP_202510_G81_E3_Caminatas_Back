package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RutaRequestDTO {

    @NotBlank(message = "El nombre de la ruta no puede estar vacío")
    private String nombreRuta;

    @Size(max = 500, message = "La descripción debe tener máximo 500 caracteres")
    private String descripcionRuta;

    @NotNull(message = "Debe proporcionar al menos 2 coordenadas")
    @Size(min = 2, message = "Debe tener al menos 2 coordenadas")
    private List<@Valid CoordenadaRequestDTO> coordenadas;
}
