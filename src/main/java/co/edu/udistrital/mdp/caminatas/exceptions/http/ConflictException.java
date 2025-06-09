package co.edu.udistrital.mdp.caminatas.exceptions.http;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
