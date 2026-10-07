import java.time.LocalDateTime;

/**
 * Medición de signos vitales tomada durante una atención médica.
 * Pertenece exclusivamente a la atención en la que fue tomada (composición "contiene").
 */
public class MedicionSignosVitales {
    private LocalDateTime fechaHora;
    private double temperatura;
    private int frecuenciaCardiaca;
    private int presionSistolica;
    private int presionDiastolica;
    private double saturacionOxigeno;

    public MedicionSignosVitales() {
    }

    public MedicionSignosVitales(double temperatura, int frecuenciaCardiaca,
                                 int presionSistolica, int presionDiastolica,
                                 double saturacionOxigeno) {
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.presionSistolica = presionSistolica;
        this.presionDiastolica = presionDiastolica;
        this.saturacionOxigeno = saturacionOxigeno;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public int getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(int frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public int getPresionSistolica() {
        return presionSistolica;
    }

    public void setPresionSistolica(int presionSistolica) {
        this.presionSistolica = presionSistolica;
    }

    public int getPresionDiastolica() {
        return presionDiastolica;
    }

    public void setPresionDiastolica(int presionDiastolica) {
        this.presionDiastolica = presionDiastolica;
    }

    public double getSaturacionOxigeno() {
        return saturacionOxigeno;
    }

    public void setSaturacionOxigeno(double saturacionOxigeno) {
        this.saturacionOxigeno = saturacionOxigeno;
    }

    /** Registra el momento en que se toma la medición. */
    public void realizarMedicion() {
        this.fechaHora = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Temp: %.1f °C | FC: %d lpm | PA: %d/%d mmHg | SpO2: %.1f %%",
                temperatura, frecuenciaCardiaca, presionSistolica, presionDiastolica, saturacionOxigeno);
    }
}
