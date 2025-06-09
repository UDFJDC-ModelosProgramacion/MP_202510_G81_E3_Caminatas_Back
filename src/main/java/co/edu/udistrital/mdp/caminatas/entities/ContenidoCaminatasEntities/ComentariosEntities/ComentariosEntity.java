package co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.ComentariosEntities;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities.UsuarioNaturalEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "comentarios")
@EqualsAndHashCode(callSuper = true)
public class ComentariosEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private EstadoComentario estadoComentario; // PENDIENTE, APROBADO, RECHAZADO

    @Column(length = 500, nullable = false)
    private String descripcionComentario;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Integer calificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private UsuarioNaturalEntity autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caminata_id", nullable = false)
    private CaminataEntity caminata;
}

