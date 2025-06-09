package co.edu.udistrital.mdp.caminatas.services.NotificationServices;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.caminatas.services.NotificationServices.Observers.ObservadorUsuario;

public class NotificationManager {

    private final List<ObservadorUsuario> observadores = new ArrayList<>();

    public void registrar(ObservadorUsuario observador) {
        observadores.add(observador);
    }

    public void notificar(String mensaje) {
        for (ObservadorUsuario observador : observadores) {
            observador.actualizar(mensaje);
        }
    }
}

