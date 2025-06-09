package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.SegurosRequestDTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeguroBasicoRequestDTO {

    @NotBlank(message = "La descripción del seguro básico es obligatoria")
    private String descripcionSeguro;

    @NotNull(message = "El costo del seguro básico es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal costoSeguro;
}
