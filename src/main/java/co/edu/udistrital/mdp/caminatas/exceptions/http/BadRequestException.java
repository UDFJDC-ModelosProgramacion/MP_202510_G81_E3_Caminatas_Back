package co.edu.udistrital.mdp.caminatas.exceptions.http;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
