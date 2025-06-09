package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.SegurosResponsesDTO;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SeguroResponseDTO {
    private Long id;
    private String descripcionSeguro;
    private BigDecimal costoSeguro;
    private String tipoSeguro; // BASICO, JURIDICO, ADICIONAL
    private String detalleAdicional; // Solo para ADICIONAL, puede ser null para otros
}
