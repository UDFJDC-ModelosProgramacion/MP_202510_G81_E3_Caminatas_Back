package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;

@Data
public class UsuarioJuridicoRequestDTO {
    
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

    @NotBlank
    private String nombreEmpresa;

    @NotBlank
    private int numParticipantes;

    @Size(min = 1, message = "Debe registrar al menos un participante")
    private List<@NotBlank String> nombresParticipantes;
}

