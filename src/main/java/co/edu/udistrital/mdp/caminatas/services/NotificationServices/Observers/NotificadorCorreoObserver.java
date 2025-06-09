package co.edu.udistrital.mdp.caminatas.services.NotificationServices.Observers;

import co.edu.udistrital.mdp.caminatas.services.NotificationServices.NotificationWaysServices.EmailService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class NotificadorCorreoObserver implements ObservadorUsuario {

    private final String destinatario;
    private final EmailService emailService;

    @Override
    public void actualizar(String mensaje) {
        emailService.enviarCorreo(destinatario, "Notificación de caminata", mensaje);
    }
}


