package co.edu.udistrital.mdp.caminatas.services.NotificationServices.NotificationWaysServices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    public void enviarSms(String telefono, String mensaje) {
        log.info("📱 Enviando SMS a {}: {}", telefono, mensaje);
        // Aquí podrías integrar Twilio, Nexmo, etc.
    }
}

