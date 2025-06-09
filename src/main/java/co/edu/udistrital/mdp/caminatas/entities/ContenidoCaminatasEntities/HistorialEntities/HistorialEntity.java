package co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.HistorialEntities;

import java.time.LocalDate;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.EstadoCaminata;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.PagoEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "historial")
@EqualsAndHashCode(callSuper = true)
public class HistorialEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caminata_id")
    private CaminataEntity caminata;

    @Column(nullable = false)
    private LocalDate fechaParticipacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCaminata estado; // Ejemplo: REALIZADA, CANCELADA, NO_ASISTIO

    @Column
    private Integer calificacion; // Opcional, del 1 al 5

    @Column(length = 500)
    private String observaciones; // Notas sobre la experiencia

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id")
    private PagoEntity pago;

    @PrePersist
    public void asignarFechaSiNoExiste() {
        if (fechaParticipacion == null) {
            this.fechaParticipacion = LocalDate.now();
        }
    }
}
