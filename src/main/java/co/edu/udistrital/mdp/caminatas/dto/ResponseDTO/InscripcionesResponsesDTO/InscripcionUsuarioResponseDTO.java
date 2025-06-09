package co.edu.udistrital.mdp.caminatas.dto.ResponseDTO.InscripcionesResponsesDTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InscripcionUsuarioResponseDTO {

    private Long id;
    private LocalDate fechaInscripcion;
    private Boolean estadoPago;

    private Long usuarioId;
    private String nombreUsuario;

    private Long caminataId;
    private String nombreCaminata;
}
