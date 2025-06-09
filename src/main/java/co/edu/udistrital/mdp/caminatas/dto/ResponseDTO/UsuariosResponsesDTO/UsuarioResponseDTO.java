package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.UsuariosResponsesDTO;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private RolUsuario rol;
    private String nombreUsuario;
    private String correo;
    private Long cedula;
    private Long telefono;
}

