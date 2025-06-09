package co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities;

import java.time.LocalDate;
import java.time.LocalTime;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;
import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.EstadoCaminata;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "calendarios")
@EqualsAndHashCode(callSuper = true)
public class CalendarioEntity extends BaseEntity {

    @Column(nullable = false)
    private LocalDate fechaCaminata;
    
    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column
    private LocalTime horaFin;

    @Column(nullable = false)
    private Integer cupoMaximo;

    @Column(nullable = false)
    private Integer cupoDisponible;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCaminata estado; // Ej: "PROGRAMADA", "CANCELADA", "REALIZADA"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caminata_id")
    private CaminataEntity caminata; // Opcional, si quieres vincular la caminata directamente

    @PrePersist
    public void inicializarCupoDisponible() {
        if (cupoDisponible == null && cupoMaximo != null) {
            this.cupoDisponible = this.cupoMaximo;
        }
    }
}


