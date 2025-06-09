package co.edu.udistrital.mdp.caminatas.exceptions.BaseException;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import co.edu.udistrital.mdp.caminatas.exceptions.http.LowerCaseClassNameResolver;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "ApiError", description = "Modelo para representar errores de la API")
@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error", visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    @Schema(description = "Código de estado HTTP", example = "NOT_FOUND")
    private HttpStatus status;

    @Schema(description = "Fecha y hora del error", example = "2025-05-21T14:00:00.123")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    
    @Schema(description = "Mensaje detallado del error", example = "El usuario con ID 99 no fue encontrado.")
    private String message;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }
}
