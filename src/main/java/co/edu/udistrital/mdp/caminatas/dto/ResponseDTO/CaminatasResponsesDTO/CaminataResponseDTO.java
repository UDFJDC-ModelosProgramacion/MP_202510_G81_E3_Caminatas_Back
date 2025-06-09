package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.CaminatasResponsesDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.GaleriaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.MapaRequestDTO;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.DificultadCaminata;
import lombok.Data;

@Data
public class CaminataResponseDTO {

    private Long id;

    private String nombreCaminata;

    private BigDecimal costoCaminata;

    private String patrocinador;

    private LocalDate fecha;

    private LocalTime hora;

    private String lugar;

    private String duracion;

    private String descripcion;

    private DificultadCaminata dificultad;

    private String itinerario;

    private String recomendaciones;

    private MapaRequestDTO mapa;

    private GaleriaRequestDTO galeria;

    // Puedes agregar un campo adicional si necesitas ver cuántos comentarios tiene
    private int totalComentarios;
}

