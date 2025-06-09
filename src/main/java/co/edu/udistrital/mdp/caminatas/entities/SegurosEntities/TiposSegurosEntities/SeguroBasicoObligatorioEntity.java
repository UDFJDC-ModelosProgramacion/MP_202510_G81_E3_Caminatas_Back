package co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TiposSegurosEntities;

import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.SeguroEntity;
import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TipoSeguro;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "seguros_basicos")
@EqualsAndHashCode(callSuper = true)
public class SeguroBasicoObligatorioEntity extends SeguroEntity {

    @Override
    public TipoSeguro getTipoSeguro() {
        return TipoSeguro.BASICO;
    }

}
