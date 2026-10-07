import java.util.ArrayList;
import java.util.List;

/**
 * Equipo de atención domiciliaria. Agrupa varios profesionales (agregación):
 * un profesional puede cambiar de equipo sin dejar de existir en el sistema.
 */
public class EquipoAtencion {
    private String codigo;
    private String nombre;
    private String zonaCobertura;

    private final List<ProfesionalSalud> profesionales = new ArrayList<>();

    public EquipoAtencion() {
    }

    public EquipoAtencion(String codigo, String nombre, String zonaCobertura) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.zonaCobertura = zonaCobertura;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getZonaCobertura() {
        return zonaCobertura;
    }

    public void setZonaCobertura(String zonaCobertura) {
        this.zonaCobertura = zonaCobertura;
    }

    public List<ProfesionalSalud> getProfesionales() {
        return new ArrayList<>(profesionales);
    }

    /** Agrega un profesional; si estaba en otro equipo, primero se retira de ese. */
    public void agregarProfesional(ProfesionalSalud profesional) {
        if (profesional == null || profesionales.contains(profesional)) {
            return;
        }
        EquipoAtencion anterior = profesional.getEquipo();
        if (anterior != null) {
            anterior.retirarProfesional(profesional);
        }
        profesionales.add(profesional);
        profesional.setEquipo(this);
    }

    /** Retira al profesional del equipo; el profesional sigue existiendo en el sistema. */
    public void retirarProfesional(ProfesionalSalud profesional) {
        if (profesionales.remove(profesional)) {
            profesional.setEquipo(null);
        }
    }
}
