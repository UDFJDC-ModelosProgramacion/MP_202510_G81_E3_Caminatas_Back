package co.edu.udistrital.mdp.caminatas.services.NotificationServices.Strategy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationContext {

    private final NotificationStrategy strategy;

    public void enviar(String destinatario, String mensaje) {
        strategy.send(destinatario, mensaje);
    }
}
