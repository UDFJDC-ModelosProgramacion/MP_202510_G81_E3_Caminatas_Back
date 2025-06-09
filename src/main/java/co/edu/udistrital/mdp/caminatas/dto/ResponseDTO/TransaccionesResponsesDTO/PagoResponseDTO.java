package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.TransaccionesResponsesDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.EstadoPago;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.MetodoPago;

@Data
public class PagoResponseDTO {
    private Long id;
    private BigDecimal monto;
    private MetodoPago metodo;
    private EstadoPago estado;
    private LocalDateTime fechaPago;
    private Long facturaId;
}
