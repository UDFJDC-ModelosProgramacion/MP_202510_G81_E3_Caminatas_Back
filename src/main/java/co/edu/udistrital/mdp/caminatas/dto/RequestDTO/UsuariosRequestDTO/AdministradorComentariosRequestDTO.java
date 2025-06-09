package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdministradorComentariosRequestDTO {
    
    @NotNull(message = "El rol del usuario es obligatorio.")
    @NotBlank
    private RolUsuario rol;

    @NotBlank
    private String nombreUsuario;

    @NotBlank
    private String correo;

    @NotBlank
    private String password;

    @NotNull
    private Long cedula;

    @NotNull
    private Long telefono;
}

