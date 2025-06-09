package co.edu.udistrital.mdp.caminatas.entities.ContenidoCaminatasEntities.HistorialEntities;

import java.time.LocalDateTime;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.FacturaEntity;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.PagoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "historial_factura_pago")
@EqualsAndHashCode(callSuper = true)
public class HistorialFacturaPagoEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private FacturaEntity factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id", nullable = false)
    private PagoEntity pago;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }
}

