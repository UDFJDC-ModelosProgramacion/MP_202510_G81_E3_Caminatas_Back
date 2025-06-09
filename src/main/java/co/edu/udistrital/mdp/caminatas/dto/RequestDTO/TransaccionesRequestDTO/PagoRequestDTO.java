package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.TransaccionesRequestDTO;

import java.math.BigDecimal;

import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.MetodoPago;
import lombok.Data;

@Data
public class PagoRequestDTO {
    private Long facturaId;
    private MetodoPago metodo;
    private BigDecimal monto;
}

