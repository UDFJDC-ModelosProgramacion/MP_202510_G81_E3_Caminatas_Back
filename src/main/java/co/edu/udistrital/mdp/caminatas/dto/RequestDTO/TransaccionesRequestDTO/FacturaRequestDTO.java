package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.TransaccionesRequestDTO;

import java.util.List;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FacturaRequestDTO {
    @NotNull
    private Long idInscripcion;

    // Uno de estos es obligatorio dependiendo del tipo de usuario
    private Long seguroBasicoId;       // Solo para NATURAL
    private Long seguroJuridicoId;     // Solo para JURIDICO

    private List<Long> segurosAdicionalesIds; // Opcionales para ambos
}
