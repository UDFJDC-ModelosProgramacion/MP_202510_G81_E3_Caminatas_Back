package co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.TiposCaminatasEntites.CaminatasDeportivasEntities;

import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "caminatas_deportivas")
@EqualsAndHashCode(callSuper = true)
public class CaminataDeportivaEntity extends CaminataEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "modalidadCaminataDeportiva_id")
    private ModalidadCaminataDeportivaEntity modalidadCaminataDeportiva;
}