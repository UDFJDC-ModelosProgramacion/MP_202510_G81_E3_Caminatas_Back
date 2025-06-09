package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO;

import lombok.Data;
import java.util.List;

@Data
public class RutaResponseDTO {
    private Long id;
    private String nombreRuta;
    private String descripcionRuta;
    private List<CoordenadaResponseDTO> coordenadas;
}
