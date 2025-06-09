package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.InicioSesionRequestDTO;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank
    private String identificador; // Puede ser correo o nombreUsuario

    @NotBlank
    private String password;
    
    @NotBlank
    private RolUsuario rol;
}
