/**
 * Interfaz para todo elemento del sistema que pueda recibir notificaciones.
 * Cada clase que la implemente decide la forma concreta de recibir el mensaje.
 */
public interface INotificable {
    void notificar(String mensaje);
}
