package co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TiposSegurosEntities;

import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.SeguroEntity;
import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TipoSeguro;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;


@Entity
@Table(name = "seguros_juridicos")
@EqualsAndHashCode(callSuper = true)
public class SeguroJuridicoEntity extends SeguroEntity {

    @Override
    public TipoSeguro getTipoSeguro() {
        return TipoSeguro.JURIDICO;
    }

}

