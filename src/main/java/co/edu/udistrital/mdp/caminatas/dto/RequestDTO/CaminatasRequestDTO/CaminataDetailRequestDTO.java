package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.CaminatasRequestDTO;

import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.GaleriaRequestDTO;
import co.edu.udistrital.mdp.caminatas.dto.RequestDTO.ContenidoCaminatasRequestDTO.MapaRequestDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CaminataDetailRequestDTO extends CaminataRequestDTO {

    @NotNull(message = "El mapa es obligatorio en el detalle.")
    private MapaRequestDTO mapa;

    @NotNull(message = "La galería es obligatoria en el detalle.")
    private GaleriaRequestDTO galeria;
}
