
package co.edu.udistrital.mdp.caminatas.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity

public class CalendarioEntity extends BaseEntity{
    @Temporal(TemporalType.DATE)
    private Date fecha;
}