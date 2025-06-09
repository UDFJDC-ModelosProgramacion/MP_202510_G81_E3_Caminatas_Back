package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO;

import java.util.List;

import lombok.Data;

@Data
public class MapaResponseDTO {
    private Long id;
    private String descripcion;
    private List<CoordenadaResponseDTO> coordenadasGenerales;
    private List<RutaResponseDTO> rutas;
}
