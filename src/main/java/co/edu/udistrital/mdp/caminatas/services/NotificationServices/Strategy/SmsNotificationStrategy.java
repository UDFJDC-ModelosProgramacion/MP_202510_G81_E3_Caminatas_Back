package co.edu.udistrital.mdp.caminatas.services.NotificationServices.Strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmsNotificationStrategy implements NotificationStrategy {

    @Override
    public void send(String destinatario, String mensaje) {
        // Simulación de envío de SMS
        log.info("📱 SMS enviado a {} con el mensaje: {}", destinatario, mensaje);
    }
}
