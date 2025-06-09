package co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.TiposCaminatasEntites.CaminatasDeportivasEntities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "caminatas_senderismo")
@EqualsAndHashCode(callSuper = true)
public class SenderismoAvanzadoEntity extends ModalidadCaminataDeportivaEntity {

}
