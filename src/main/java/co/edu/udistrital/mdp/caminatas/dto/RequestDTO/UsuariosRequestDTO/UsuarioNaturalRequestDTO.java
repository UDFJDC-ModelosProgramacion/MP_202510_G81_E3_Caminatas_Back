package co.edu.udistrital.mdp.caminatas.dto.RequestDTO.UsuariosRequestDTO;

import co.edu.udistrital.mdp.caminatas.entities.UsuariosEntities.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.Data;

@Data
@Schema(name = "UsuarioNaturalDTO", description = "DTO para la creación y edición de usuarios tipo NATURAL")
public class UsuarioNaturalRequestDTO {

    @Schema(description = "Rol del usuario", example = "NATURAL")
    @NotNull(message = "El rol del usuario es obligatorio.")
    private RolUsuario rol;

    @Schema(description = "Nombre completo del usuario", example = "Juan")
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    private String nombreUsuario;

    @Schema(description = "Contraseña del usuario", example = "secret123")
    @NotBlank
    private String password;

    @Schema(description = "Correo electrónico del usuario", example = "juan@gmail.com")
    @Email(message = "El correo debe tener un formato válido.")
    @NotBlank(message = "El correo es obligatorio.")
    private String correo;

    @Schema(description = "Número de cédula del usuario", example = "12345678")
    @NotNull(message = "La cédula es obligatoria.")
    @Min(value = 1, message = "La cédula debe ser mayor a 0.")
    private Long cedula;

    @Schema(description = "Número de teléfono del usuario", example = "3100000000")
    @NotNull(message = "El teléfono es obligatorio.")
    @Digits(integer = 10, fraction = 0, message = "El teléfono debe tener hasta 10 dígitos.")
    private Long telefono;
}
