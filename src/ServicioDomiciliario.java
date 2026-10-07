import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servicio domiciliario solicitado por un paciente.
 * Estados posibles: SOLICITADO, PROGRAMADO, EN ATENCION, FINALIZADO y CANCELADO.
 */
public class ServicioDomiciliario {

    public static final String SOLICITADO = "SOLICITADO";
    public static final String PROGRAMADO = "PROGRAMADO";
    public static final String EN_ATENCION = "EN ATENCION";
    public static final String FINALIZADO = "FINALIZADO";
    public static final String CANCELADO = "CANCELADO";

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private String codigo;
    private LocalDateTime fechaProgramada;
    private String direccionAtencion;
    private String motivo;
    private String estado;

    private Paciente paciente;                 // solicita (1 paciente por servicio)
    private ProfesionalSalud profesionalSalud; // atiende (0..1)
    private AtencionMedica atencionMedica;     // genera  (0..1, composición)

    public ServicioDomiciliario() {
        this.estado = SOLICITADO;
    }

    /** Crea un servicio solicitado por un paciente; la dirección por defecto es la del paciente. */
    public ServicioDomiciliario(String codigo, Paciente paciente, String motivo) {
        this();
        this.codigo = codigo;
        this.motivo = motivo;
        this.paciente = paciente;
        if (paciente != null) {
            this.direccionAtencion = paciente.getDireccion();
            paciente.agregarServicio(this);
            paciente.notificar("Su solicitud " + codigo + " fue registrada. Motivo: " + motivo);
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public String getDireccionAtencion() {
        return direccionAtencion;
    }

    public void setDireccionAtencion(String direccionAtencion) {
        this.direccionAtencion = direccionAtencion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public ProfesionalSalud getProfesionalSalud() {
        return profesionalSalud;
    }

    public AtencionMedica getAtencionMedica() {
        return atencionMedica;
    }

    /** Fija la fecha y hora en que se prestará el servicio. */
    public void programar(LocalDateTime fecha) {
        validarEstado("programar", SOLICITADO, PROGRAMADO);
        this.fechaProgramada = fecha;
        if (profesionalSalud != null) {
            this.estado = PROGRAMADO;
            notificarParticipantes("El servicio " + codigo + " fue reprogramado para " + fecha.format(FORMATO));
        }
    }

    /**
     * Asigna el profesional que atenderá el servicio. Al tener fecha y profesional
     * el servicio queda en estado PROGRAMADO y se notifica a paciente y profesional.
     */
    public void asignarProfesional(ProfesionalSalud profesional) {
        validarEstado("asignar profesional", SOLICITADO, PROGRAMADO);
        if (profesional == null) {
            throw new IllegalArgumentException("El profesional no puede ser nulo");
        }
        if (profesional == this.profesionalSalud) {
            return; // ya está asignado a este servicio
        }
        if (fechaProgramada == null) {
            throw new IllegalStateException("Primero debe programar la fecha del servicio " + codigo);
        }
        if (!profesional.estaDisponible(fechaProgramada)) {
            throw new IllegalStateException(profesional.getNombre()
                    + " no está disponible el " + fechaProgramada.format(FORMATO));
        }
        if (this.profesionalSalud != null) {
            this.profesionalSalud.quitarServicio(this);
        }
        this.profesionalSalud = profesional;
        profesional.agregarServicio(this);
        this.estado = PROGRAMADO;
        notificarParticipantes("Servicio " + codigo + " PROGRAMADO para " + fechaProgramada.format(FORMATO)
                + " en " + direccionAtencion + " con " + profesional.getNombre()
                + " (" + profesional.getEspecialidad() + ")");
    }

    /** Inicia la atención: crea la AtencionMedica (composición) y cambia el estado a EN ATENCION. */
    public void iniciarAtencion() {
        validarEstado("iniciar la atención", PROGRAMADO);
        this.atencionMedica = new AtencionMedica(LocalDateTime.now());
        this.estado = EN_ATENCION;
        notificarParticipantes("La atención del servicio " + codigo + " ha iniciado");
    }

    /** Finaliza la atención registrando la hora de fin. */
    public void finalizar() {
        validarEstado("finalizar", EN_ATENCION);
        if (atencionMedica.getFechaHoraFin() == null) {
            atencionMedica.setFechaHoraFin(LocalDateTime.now());
        }
        this.estado = FINALIZADO;
        notificarParticipantes("El servicio " + codigo + " ha FINALIZADO. Recomendaciones: "
                + atencionMedica.getRecomendaciones());
    }

    /** Cancela el servicio si aún no se ha atendido. */
    public void cancelar() {
        validarEstado("cancelar", SOLICITADO, PROGRAMADO);
        this.estado = CANCELADO;
        notificarParticipantes("El servicio " + codigo + " fue CANCELADO");
    }

    private void notificarParticipantes(String mensaje) {
        if (paciente != null) {
            paciente.notificar(mensaje);
        }
        if (profesionalSalud != null) {
            profesionalSalud.notificar(mensaje);
        }
    }

    private void validarEstado(String accion, String... permitidos) {
        for (String p : permitidos) {
            if (p.equals(estado)) {
                return;
            }
        }
        throw new IllegalStateException("No se puede " + accion + " el servicio " + codigo
                + " en estado " + estado);
    }
}
