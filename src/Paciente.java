import java.util.ArrayList;
import java.util.List;

/**
 * Paciente de MediHome. Es un Usuario que puede recibir notificaciones
 * y solicitar varios servicios domiciliarios (asociación "solicita" 0..1 - 0..*).
 */
public class Paciente extends Usuario implements INotificable {
    private String telefono;
    private String direccion;

    private final List<ServicioDomiciliario> servicios = new ArrayList<>();

    public Paciente() {
    }

    public Paciente(String identificacion, String nombre, String correo,
                    String telefono, String direccion) {
        super(identificacion, nombre, correo);
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /** Se invoca desde ServicioDomiciliario para mantener la asociación "solicita". */
    void agregarServicio(ServicioDomiciliario servicio) {
        if (servicio != null && !servicios.contains(servicio)) {
            servicios.add(servicio);
        }
    }

    public List<ServicioDomiciliario> getServicios() {
        return new ArrayList<>(servicios);
    }

    @Override
    public void notificar(String mensaje) {
        // El paciente recibe la notificación por SMS a su teléfono
        System.out.println("  [SMS a " + telefono + " | Paciente " + getNombre() + "] " + mensaje);
    }
}
