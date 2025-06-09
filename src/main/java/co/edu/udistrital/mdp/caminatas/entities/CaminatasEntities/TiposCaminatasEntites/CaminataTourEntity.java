package co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.TiposCaminatasEntites;

import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "caminatas_tour")
@EqualsAndHashCode(callSuper = true)
public class CaminataTourEntity extends CaminataEntity {
    
}
