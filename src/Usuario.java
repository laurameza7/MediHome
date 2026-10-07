/**
 * Clase base para los usuarios del sistema MediHome (pacientes y profesionales de salud).
 */
public class Usuario {

    private String identificacion;
    private String nombre;
    private String correo;

    public Usuario() {
    }

    public Usuario(String identificacion, String nombre, String correo) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombre + " (ID: " + identificacion + ", correo: " + correo + ")";
    }
}
