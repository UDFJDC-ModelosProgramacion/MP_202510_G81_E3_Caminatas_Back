package co.edu.udistrital.mdp.caminatas.entities.SegurosEntities;

import java.math.BigDecimal;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "seguros")
@EqualsAndHashCode(callSuper = true)
public abstract class SeguroEntity extends BaseEntity {

    @NotBlank(message = "La descripción del seguro es obligatoria")
    @Column(nullable = false)
    private String descripcionSeguro;

    @NotNull(message = "El costo del seguro es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo debe ser mayor que cero")
    @Column(nullable = false)
    private BigDecimal costoSeguro;

    public abstract TipoSeguro getTipoSeguro();
}


