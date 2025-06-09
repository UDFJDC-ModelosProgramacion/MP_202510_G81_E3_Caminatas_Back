package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordenadaRequestDTO {

    @DecimalMin(value = "-90.0", message = "Latitud mínima válida es -90.0")
    @DecimalMax(value = "90.0", message = "Latitud máxima válida es 90.0")
    private double latitud;

    @DecimalMin(value = "-180.0", message = "Longitud mínima válida es -180.0")
    @DecimalMax(value = "180.0", message = "Longitud máxima válida es 180.0")
    private double longitud;
}


