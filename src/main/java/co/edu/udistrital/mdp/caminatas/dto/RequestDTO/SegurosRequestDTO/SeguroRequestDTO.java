package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.SegurosRequestDTO;

import java.math.BigDecimal;

import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TipoSeguro;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeguroRequestDTO {

    @NotBlank(message = "La descripción del seguro es obligatoria")
    private String descripcionSeguro;

    @NotNull(message = "El costo del seguro es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo debe ser mayor que cero")
    private BigDecimal costoSeguro;

    @NotBlank(message = "El tipo de seguro es obligatorio (BASICO, JURIDICO o ADICIONAL)")
    private TipoSeguro tipoSeguro; // ADICIONAL, BASICO, JURIDICO

    private String detalleAdicional; // Solo para ADICIONAL
}

