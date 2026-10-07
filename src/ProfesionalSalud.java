import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Profesional de la salud de MediHome. Es un Usuario que puede recibir notificaciones,
 * atender múltiples servicios en fechas diferentes (asociación "atiende" 0..1 - 0..*)
 * y pertenecer a un equipo de atención (agregación "agrupa" 0..1 - 0..*).
 */
public class ProfesionalSalud extends Usuario implements INotificable {
    private String numeroRegistroProfesional;
    private String especialidad;

    private final List<ServicioDomiciliario> serviciosAsignados = new ArrayList<>();
    private EquipoAtencion equipo;

    public ProfesionalSalud() {
    }

    public ProfesionalSalud(String identificacion, String nombre, String correo,
                            String numeroRegistroProfesional, String especialidad) {
        super(identificacion, nombre, correo);
        this.numeroRegistroProfesional = numeroRegistroProfesional;
        this.especialidad = especialidad;
    }

    public String getNumeroRegistroProfesional() {
        return numeroRegistroProfesional;
    }

    public void setNumeroRegistroProfesional(String numeroRegistroProfesional) {
        this.numeroRegistroProfesional = numeroRegistroProfesional;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public EquipoAtencion getEquipo() {
        return equipo;
    }

    /** Lo usa EquipoAtencion al agregar o retirar al profesional. */
    void setEquipo(EquipoAtencion equipo) {
        this.equipo = equipo;
    }

    /** Lo usa ServicioDomiciliario al asignar al profesional. */
    void agregarServicio(ServicioDomiciliario servicio) {
        if (servicio != null && !serviciosAsignados.contains(servicio)) {
            serviciosAsignados.add(servicio);
        }
    }

    /** Lo usa ServicioDomiciliario si el servicio cambia de profesional. */
    void quitarServicio(ServicioDomiciliario servicio) {
        serviciosAsignados.remove(servicio);
    }

    public List<ServicioDomiciliario> getServiciosAsignados() {
        return new ArrayList<>(serviciosAsignados);
    }

    @Override
    public void notificar(String mensaje) {
        // El profesional recibe la notificación por correo electrónico
        System.out.println("  [Correo a " + getCorreo() + " | Profesional " + getNombre() + "] " + mensaje);
    }

    /**
     * Un profesional está disponible si no tiene otro servicio activo
     * (no cancelado ni finalizado) programado en la misma fecha y hora.
     */
    public boolean estaDisponible(LocalDateTime fecha) {
        if (fecha == null) {
            return false;
        }
        for (ServicioDomiciliario s : serviciosAsignados) {
            boolean activo = !ServicioDomiciliario.CANCELADO.equals(s.getEstado())
                    && !ServicioDomiciliario.FINALIZADO.equals(s.getEstado());
            if (activo && fecha.equals(s.getFechaProgramada())) {
                return false;
            }
        }
        return true;
    }
}
