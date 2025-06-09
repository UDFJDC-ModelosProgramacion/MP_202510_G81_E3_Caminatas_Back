package co.edu.udistrital.mdp.caminatas.exceptions.Handler;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

import org.springframework.lang.NonNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import co.edu.udistrital.mdp.caminatas.exceptions.BaseException.ApiError;
import co.edu.udistrital.mdp.caminatas.exceptions.http.ConflictException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.NotFoundException;
import co.edu.udistrital.mdp.caminatas.exceptions.http.IllegalOperationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /*
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
    		NotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
    
    /*
     * Handles IllegalOperationException. 
     *
     * @param ex the IllegalOperationException
     * @return the ApiError object
     */
    @ExceptionHandler(IllegalOperationException.class)
    protected ResponseEntity<Object> handleIllegalOperation(
    		IllegalOperationException ex) {
        ApiError apiError = new ApiError(PRECONDITION_FAILED);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
    /**
     * Crea una respuesta HTTP con el objeto ApiError.
     */
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    /* 
     * Maneja cualquier excepción inesperada no capturada.
     * 
     * @param ex la excepción no controlada
     * @return el objeto ApiError  
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR);
        apiError.setMessage("Error interno del servidor: " + ex.getMessage());
        return buildResponseEntity(apiError);
    }
    /*
     * Maneja la excepción ConflictException.
     *
     * @param ex la excepción de conflicto
     * @return el objeto ApiError
     */
    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflict(ConflictException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
    /*
     * Maneja la excepción MethodArgumentNotValidException.
     * 
     * @param ex la excepción de argumento no válido
     * @return el objeto ApiError
     * @param headers los encabezados HTTP
     * @param status el código de estado HTTP
     * @param request la solicitud web
     * @return la entidad de respuesta
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        StringBuilder message = new StringBuilder("Errores de validación: ");

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            message.append("[").append(error.getField()).append(": ").append(error.getDefaultMessage()).append("] ");
        }

        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY);
        apiError.setMessage(message.toString());
        return buildResponseEntity(apiError);
    }


}
