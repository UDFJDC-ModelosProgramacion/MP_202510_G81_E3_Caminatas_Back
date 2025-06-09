package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.ContenidoCaminatasDTO;

import lombok.Data;
import java.util.List;

@Data
public class GaleriaResponseDTO {
    private Long id;
    private String imagenPrincipal;
    private List<String> imagenesGaleria;
}
