package co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.MapaRutasEntities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordenadas {

    private double latitud;
    private double longitud;
}

