package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InicioSesionResponseDTO;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    @NotBlank
    private String correo;

    @NotBlank
    private String token;

    @NotBlank
    private RolUsuario rol;
}
