import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Atención médica registrada por el profesional durante un servicio domiciliario.
 * Existe exclusivamente como resultado de un ServicioDomiciliario (composición "genera")
 * y contiene 0 o varias mediciones de signos vitales (composición "contiene").
 */
public class AtencionMedica {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String observaciones;
    private String recomendaciones;

    private final List<MedicionSignosVitales> mediciones = new ArrayList<>();

    /**
     * Constructor de paquete: solo ServicioDomiciliario crea atenciones,
     * porque la atención no tiene sentido sin su servicio.
     */
    AtencionMedica(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    /** Registra una medición de signos vitales dentro de esta atención. */
    public void agregarMedicion(MedicionSignosVitales medicion) {
        if (medicion == null) {
            return;
        }
        if (medicion.getFechaHora() == null) {
            medicion.realizarMedicion();
        }
        mediciones.add(medicion);
    }

    public List<MedicionSignosVitales> getMediciones() {
        return new ArrayList<>(mediciones);
    }
}
