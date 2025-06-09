package co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "usuarios_naturales")
@EqualsAndHashCode(callSuper = true)
public class UsuarioNaturalEntity extends UsuarioEntity {

}

