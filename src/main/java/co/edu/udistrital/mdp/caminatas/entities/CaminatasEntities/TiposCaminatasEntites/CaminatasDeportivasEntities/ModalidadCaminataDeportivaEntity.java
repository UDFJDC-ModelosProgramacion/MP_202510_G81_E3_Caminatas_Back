package co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.TiposCaminatasEntites.CaminatasDeportivasEntities;

import co.edu.udistrital.mdp.caminatas.entities.BaseEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "modalidades_deportivas")
@EqualsAndHashCode(callSuper = true)
public class ModalidadCaminataDeportivaEntity extends BaseEntity {

    public enum Tipo { ENTRENAMIENTO, COMPETENCIA }

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    private String condiciones;
    private String requisitosEspeciales;
    private String premios;
    private String patrocinador;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ganador_id")
    private UsuarioEntity ganador;
}

