package co.edu.udistrital.mdp.caminatas.entities.InscripcionesEntities;

import java.time.LocalDate;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "inscripciones", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "caminata_id"})
})
@EqualsAndHashCode(callSuper = true)
public class InscripcionUsuarioEntity extends BaseEntity {

    @Column(nullable = false)
    private LocalDate fechaInscripcion;

    @Column(nullable = false)
    private Boolean estadoPago = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioNaturalEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caminata_id", nullable = false)
    private CaminataEntity caminata;
}

