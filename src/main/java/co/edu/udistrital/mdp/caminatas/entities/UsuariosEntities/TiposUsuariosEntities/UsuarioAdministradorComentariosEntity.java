package co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.TiposUsuariosEntities;

import co.edu.udistrital.mdp.caminatas.entities.CaminatasEntities.CaminataEntity;
import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.UsuarioEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import java.util.List;

@Entity
@Data

public class UsuarioAdministradorComentariosEntity extends UsuarioEntity {
    
    @OneToMany
    private List<CaminataEntity> caminatasGestionadas;
}
