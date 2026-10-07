import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Programa principal de MediHome.
 * Instancia un paciente, un profesional, un servicio domiciliario, una atención médica
 * y una medición de signos vitales, y al final presenta el reporte de la atención prestada.
 */
public class Main {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String LINEA = "=".repeat(70);
    private static final String SUBLINEA = "-".repeat(70);

    public static void main(String[] args) {
        System.out.println(LINEA);
        System.out.println("            MEDIHOME - SISTEMA DE ATENCIÓN MÉDICA DOMICILIARIA");
        System.out.println(LINEA);

        // 1. Paciente
        Paciente paciente = new Paciente("1085123456", "Carlos Andrés Rosero",
                "carlos.rosero@correo.com", "3104567890", "Calle 18 # 25-40, Barrio Las Cuadras, Pasto");

        // 2. Profesional de la salud (y el equipo al que pertenece)
        ProfesionalSalud profesional = new ProfesionalSalud("27456789", "Dra. María Fernanda Benavides",
                "mfbenavides@medihome.com", "RP-52-001234", "Medicina General");

        EquipoAtencion equipo = new EquipoAtencion("EQ-01", "Equipo Domiciliario Centro", "Pasto - Comuna 1");
        equipo.agregarProfesional(profesional);

        // 3. Servicio domiciliario solicitado por el paciente
        System.out.println("\n>> Solicitud del servicio");
        ServicioDomiciliario servicio = new ServicioDomiciliario("SD-2026-0001", paciente,
                "Fiebre persistente y malestar general desde hace 3 días");
        System.out.println("   Estado: " + servicio.getEstado());

        // Programación y asignación del profesional
        System.out.println("\n>> Programación del servicio");
        LocalDateTime fechaProgramada = LocalDateTime.of(2026, 10, 8, 9, 0);
        servicio.programar(fechaProgramada);
        servicio.asignarProfesional(profesional);
        System.out.println("   Estado: " + servicio.getEstado());

        // 4. Atención médica (la crea el servicio porque es una composición)
        System.out.println("\n>> Inicio de la atención");
        servicio.iniciarAtencion();
        AtencionMedica atencion = servicio.getAtencionMedica();
        atencion.setFechaHoraInicio(fechaProgramada.plusMinutes(5)); // hora real de llegada
        System.out.println("   Estado: " + servicio.getEstado());

        // 5. Medición de signos vitales registrada dentro de la atención
        MedicionSignosVitales medicion = new MedicionSignosVitales(38.4, 96, 128, 84, 95.0);
        medicion.realizarMedicion();
        medicion.setFechaHora(fechaProgramada.plusMinutes(12)); // hora real de la toma
        atencion.agregarMedicion(medicion);

        atencion.setObservaciones("Paciente consciente y orientado. Febril, con congestión nasal y "
                + "odinofagia leve. Auscultación pulmonar sin agregados. Cuadro compatible con "
                + "infección respiratoria alta.");
        atencion.setRecomendaciones("Acetaminofén 500 mg cada 8 horas por 3 días, abundante "
                + "hidratación y reposo. Consultar a urgencias si la fiebre supera 39 °C o hay "
                + "dificultad para respirar.");
        atencion.setFechaHoraFin(fechaProgramada.plusMinutes(45));

        System.out.println("\n>> Finalización del servicio");
        servicio.finalizar();
        System.out.println("   Estado: " + servicio.getEstado());

        // Reporte final
        imprimirReporte(servicio);
    }

    /** Presenta el reporte de la atención prestada al paciente. */
    private static void imprimirReporte(ServicioDomiciliario servicio) {
        Paciente paciente = servicio.getPaciente();
        ProfesionalSalud profesional = servicio.getProfesionalSalud();
        AtencionMedica atencion = servicio.getAtencionMedica();

        System.out.println("\n" + LINEA);
        System.out.println("                REPORTE DE ATENCIÓN MÉDICA DOMICILIARIA");
        System.out.println(LINEA);

        System.out.println("SERVICIO");
        System.out.println("  Código            : " + servicio.getCodigo());
        System.out.println("  Estado            : " + servicio.getEstado());
        System.out.println("  Fecha programada  : " + formatear(servicio.getFechaProgramada()));
        System.out.println("  Dirección         : " + servicio.getDireccionAtencion());
        System.out.println("  Motivo            : " + servicio.getMotivo());
        System.out.println(SUBLINEA);

        System.out.println("PACIENTE");
        System.out.println("  Identificación    : " + paciente.getIdentificacion());
        System.out.println("  Nombre            : " + paciente.getNombre());
        System.out.println("  Correo            : " + paciente.getCorreo());
        System.out.println("  Teléfono          : " + paciente.getTelefono());
        System.out.println("  Dirección         : " + paciente.getDireccion());
        System.out.println(SUBLINEA);

        System.out.println("PROFESIONAL DE LA SALUD");
        System.out.println("  Identificación    : " + profesional.getIdentificacion());
        System.out.println("  Nombre            : " + profesional.getNombre());
        System.out.println("  Registro prof.    : " + profesional.getNumeroRegistroProfesional());
        System.out.println("  Especialidad      : " + profesional.getEspecialidad());
        EquipoAtencion equipo = profesional.getEquipo();
        if (equipo != null) {
            System.out.println("  Equipo            : " + equipo.getNombre() + " (" + equipo.getCodigo()
                    + ") - Zona: " + equipo.getZonaCobertura());
        }
        System.out.println(SUBLINEA);

        System.out.println("ATENCIÓN MÉDICA");
        System.out.println("  Inicio            : " + formatear(atencion.getFechaHoraInicio()));
        System.out.println("  Fin               : " + formatear(atencion.getFechaHoraFin()));
        if (atencion.getFechaHoraInicio() != null && atencion.getFechaHoraFin() != null) {
            long minutos = Duration.between(atencion.getFechaHoraInicio(), atencion.getFechaHoraFin()).toMinutes();
            System.out.println("  Duración          : " + minutos + " minutos");
        }
        System.out.println("  Observaciones     : " + atencion.getObservaciones());
        System.out.println("  Recomendaciones   : " + atencion.getRecomendaciones());
        System.out.println(SUBLINEA);

        List<MedicionSignosVitales> mediciones = atencion.getMediciones();
        System.out.println("SIGNOS VITALES (" + mediciones.size() + " medición(es))");
        int i = 1;
        for (MedicionSignosVitales m : mediciones) {
            System.out.println("  #" + i++ + " - " + formatear(m.getFechaHora()));
            System.out.printf("     Temperatura          : %.1f °C%n", m.getTemperatura());
            System.out.println("     Frecuencia cardiaca  : " + m.getFrecuenciaCardiaca() + " lpm");
            System.out.println("     Presión arterial     : " + m.getPresionSistolica() + "/"
                    + m.getPresionDiastolica() + " mmHg");
            System.out.printf("     Saturación oxígeno   : %.1f %%%n", m.getSaturacionOxigeno());
        }
        System.out.println(LINEA);
    }

    private static String formatear(LocalDateTime fecha) {
        return fecha == null ? "N/A" : fecha.format(FORMATO);
    }
}
