package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.TransaccionesResponsesDTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoSimpleResponseDTO {
    private Long id;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String metodo;
    private String estado;
}