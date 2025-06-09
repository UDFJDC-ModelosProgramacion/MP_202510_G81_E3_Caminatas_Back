package co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TiposSegurosEntities;

import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.SeguroEntity;
import co.edu.udistrital.mdp.caminatas.entities.SegurosEntities.TipoSeguro;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "seguros_adicionales")
@EqualsAndHashCode(callSuper = true)
public class SeguroAdicionalEntity extends SeguroEntity {

    @Override
    public TipoSeguro getTipoSeguro() {
        return TipoSeguro.ADICIONAL;
    }

    // Opcional: solo si deseas tener una descripción distinta
    @Column(length = 500)
    private String detalleAdicional;
}

