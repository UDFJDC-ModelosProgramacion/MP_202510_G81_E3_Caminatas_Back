package co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.FacturaEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "pagos")
@EqualsAndHashCode(callSuper = true)
public class PagoEntity extends BaseEntity {

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDateTime fechaPago;
    
    @Column(nullable = false)
    private boolean activo = true; // ✅ Indica si el pago está activo

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "factura_id")
    @JsonBackReference
    private FacturaEntity factura;

}
