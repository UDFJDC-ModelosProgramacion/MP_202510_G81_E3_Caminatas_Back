package co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import co.edu.udistrital.mdp.caminatas.entities.InscripcionesEntities.InscripcionUsuarioEntity;
import co.edu.udistrital.mdp.caminatas.entities.TransaccionesEntities.PagoEntities.PagoEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "facturas")
@EqualsAndHashCode(callSuper = true)
public class FacturaEntity extends BaseEntity {

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PagoEntity> pagos = new ArrayList<>();


    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @Column
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscripcion_id", nullable = false, unique = true)
    private InscripcionUsuarioEntity inscripcion;

    @Column(nullable = false)
    private Boolean pagada = false;
}

