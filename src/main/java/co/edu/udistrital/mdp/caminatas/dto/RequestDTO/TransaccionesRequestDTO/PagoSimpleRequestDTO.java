package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.TransaccionesRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoSimpleRequestDTO {
    private Long id;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String metodo;
    private String estado;
}
