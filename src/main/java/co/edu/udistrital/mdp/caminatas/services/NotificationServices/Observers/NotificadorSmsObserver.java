package co.edu.udistrital.mdp.caminatas.services.NotificationServices.Observers;

import co.edu.udistrital.mdp.caminatas.services.NotificationServices.NotificationWaysServices.SmsService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificadorSmsObserver implements ObservadorUsuario {

    private final String telefono;
    private final SmsService smsService;

    @Override
    public void actualizar(String mensaje) {
        smsService.enviarSms(telefono, mensaje);
    }
}

