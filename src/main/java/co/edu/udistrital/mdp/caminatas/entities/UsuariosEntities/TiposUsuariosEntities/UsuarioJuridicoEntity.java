package co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuarios_juridicos")
@EqualsAndHashCode(callSuper = true)
public class UsuarioJuridicoEntity extends UsuarioEntity {

    @Column(nullable = false)
    private String nombreEmpresa;

    @Column(nullable = false)
    private int numParticipantes;

    @ElementCollection
    @CollectionTable(name = "participantes_juridicos", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "nombre_participante", nullable = false)
    private List<String> nombresParticipantes = new ArrayList<>();
    
}
