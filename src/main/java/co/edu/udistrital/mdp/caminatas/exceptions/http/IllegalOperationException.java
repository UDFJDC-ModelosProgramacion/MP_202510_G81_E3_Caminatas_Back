package co.edu.udistrital.mdp.caminatas.exceptions.http;

/*
 * Excepción que se lanza cuando se realiza una operación ilegal
 */
public class IllegalOperationException extends RuntimeException {
    public IllegalOperationException(String message) {
        super(message);
    }
}

