package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.InscripcionesRequestDTO;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class InscripcionUsuarioRequestDTO {

    @Schema(description = "Fecha de la inscripción", example = "2025-05-10")
    @NotBlank(message = "La fecha de inscripción es obligatoria.")
    @NotNull(message = "La fecha de inscripción es obligatoria.")
    private LocalDate fechaInscripcion;

    //@Schema(description = "Estado del pago", example = "false")
    //@NotNull(message = "El estado de pago es obligatorio.")
    //private Boolean estadoPago;

    @Schema(description = "ID del usuario", example = "1")
    @NotNull(message = "El ID del usuario es obligatorio.")
    private Long idUsuario;

    @Schema(description = "ID de la caminata", example = "1")
    @NotNull(message = "El ID de la caminata es obligatorio.")
    private Long idCaminata;
}

