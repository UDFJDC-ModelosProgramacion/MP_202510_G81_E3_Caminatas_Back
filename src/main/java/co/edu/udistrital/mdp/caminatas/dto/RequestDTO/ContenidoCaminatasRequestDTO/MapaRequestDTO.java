package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MapaRequestDTO {

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    private List<@Valid CoordenadaRequestDTO> coordenadasGenerales;

    @NotNull(message = "Debe incluir una lista de rutas")
    @Size(min = 1, max = 5, message = "Debe tener entre 1 y 5 rutas.")
    private List<@Valid RutaRequestDTO> rutas;
}


