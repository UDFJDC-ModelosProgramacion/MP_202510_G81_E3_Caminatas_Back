package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.UsuariosResponsesDTO;

import lombok.Data;
import java.util.List;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;

@Data
public class UsuarioJuridicoResponseDTO {
    private Long id;
    private RolUsuario rol;
    private String nombreUsuario;
    private String correo;
    private Long cedula;
    private Long telefono;
    private String nombreEmpresa;
    private int numParticipantes;
    private List<String> nombresParticipantes;
}
