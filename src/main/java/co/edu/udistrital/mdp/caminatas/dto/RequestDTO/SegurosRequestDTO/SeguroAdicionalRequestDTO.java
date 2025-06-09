package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.SegurosRequestDTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeguroAdicionalRequestDTO {

    @NotBlank(message = "La descripción del seguro adicional es obligatoria")
    private String descripcionSeguro;

    @NotNull(message = "El costo del seguro adicional es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal costoSeguro;

    private String detalleAdicional;
}
