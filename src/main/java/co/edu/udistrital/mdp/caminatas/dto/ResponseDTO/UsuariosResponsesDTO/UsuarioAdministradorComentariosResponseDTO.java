package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.UsuariosResponsesDTO;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioAdministradorComentariosResponseDTO {
    private Long id;
    private RolUsuario rol;
    private String nombreUsuario;
    private String correo;
    private Long cedula;
    private Long telefono;
    
}
