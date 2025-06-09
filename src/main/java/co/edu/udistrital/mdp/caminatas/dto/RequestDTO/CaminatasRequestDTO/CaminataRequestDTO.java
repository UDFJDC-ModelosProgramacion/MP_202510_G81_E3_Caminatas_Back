package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.CaminatasRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.GaleriaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.MapaRequestDTO;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.DificultadCaminata;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CaminataRequestDTO {

    @Schema(description = "Nombre de la caminata", example = "Vuelo del Cóndor")
    @NotBlank(message = "El nombre de la caminata es obligatorio.")
    private String nombreCaminata;

    @Schema(description = "Costo de la caminata", example = "100000.00")
    @NotNull(message = "El costo es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo no puede ser negativo.")
    private BigDecimal costoCaminata;

    @Schema(description = "Nombre del patrocinador", example = "Juan")
    private String patrocinador; // Puede ser opcional

    @Schema(description = "Fecha de la caminata", example = "2025-04-10")
    @NotNull(message = "La fecha es obligatoria.")
    private LocalDate fecha;

    @Schema(description = "Hora de la caminata", example = "07:00")
    @NotNull(message = "La hora es obligatoria.")
    private LocalTime hora;

    @Schema(description = "Lugar de la caminata", example = "Páramo de Sumapaz")
    @NotBlank(message = "El lugar es obligatorio.")
    private String lugar;

    @Schema(description = "Duración estimada", example = "6 horas")
    @NotBlank(message = "La duración es obligatoria.")
    private String duracion;

    @Schema(description = "Descripción general", example = "Observación de flora y fauna endémica")
    @NotBlank(message = "La descripción es obligatoria.")
    private String descripcion;

    @Schema(description = "Nivel de dificultad", example = "FACIL")
    @NotNull(message = "La dificultad es obligatoria.")
    private DificultadCaminata dificultad;

    @Schema(description = "Itinerario propuesto", example = "Inicio 7am, almuerzo 12pm, regreso 3pm")
    @NotBlank(message = "El itinerario es obligatorio.")
    private String itinerario;

    @Schema(description = "Recomendaciones para los asistentes", example = "Llevar hidratación, botas y sombrero")
    @NotBlank(message = "Las recomendaciones son obligatorias.")
    private String recomendaciones;

    // Relaciones (opcionales o requeridas según tu lógica)
    private MapaRequestDTO mapa;
    private GaleriaRequestDTO galeria;

}
