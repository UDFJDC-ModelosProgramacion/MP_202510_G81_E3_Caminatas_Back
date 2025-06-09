package co.edu.udistrital.mdp.caminatas.services.NotificationServices.Strategy;


public interface NotificationStrategy {
    void send(String destinatario, String mensaje);
}
