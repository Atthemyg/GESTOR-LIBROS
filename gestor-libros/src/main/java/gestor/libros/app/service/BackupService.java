package gestor.libros.app.service;

import gestor.libros.database.sqlite.SQLiteConnectionManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Servicio de backup automático de la base de datos SQLite.
 *
 * <p>Al arrancar la aplicación realiza un backup inmediato y después
 * programa copias automáticas cada {@value INTERVALO_HORAS} hora(s).
 * Guarda un máximo de {@value MAX_BACKUPS} backups; los más antiguos
 * se eliminan automáticamente.
 *
 * <p>Los backups se almacenan en:
 *   %APPDATA%\GestorLibros\backups\gestor_libros_YYYYMMDD_HHmmss.db
 */
public class BackupService {

    // ── Configuración ──────────────────────────────────────────────────────────
    /** Horas entre cada backup automático. */
    private static final long INTERVALO_HORAS = 1;

    /** Número máximo de archivos de backup a conservar. */
    private static final int MAX_BACKUPS = 10;

    /** Formato de la marca de tiempo en el nombre del archivo. */
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // ── Estado interno ─────────────────────────────────────────────────────────
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "backup-scheduler");
                t.setDaemon(true);   // no impide el cierre de la JVM
                return t;
            });

    private String ultimoMensaje = "Sin backups realizados aún.";

    // ── API pública ────────────────────────────────────────────────────────────

    /**
     * Inicia el servicio: hace un backup inmediato y programa los siguientes.
     */
    public void iniciar() {
        // Backup al arrancar
        realizarBackup();

        // Backup periódico
        scheduler.scheduleAtFixedRate(
                this::realizarBackup,
                INTERVALO_HORAS,
                INTERVALO_HORAS,
                TimeUnit.HOURS
        );
    }

    /**
     * Detiene el scheduler. Llamar al cerrar la aplicación.
     */
    public void detener() {
        scheduler.shutdownNow();
    }

    /**
     * Realiza un backup manual inmediato.
     *
     * @return Ruta del archivo generado, o {@code null} si falló.
     */
    public File realizarBackupManual() {
        return realizarBackup();
    }

    /**
     * Mensaje descriptivo del último backup realizado (éxito o error).
     */
    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    /**
     * Directorio donde se guardan los backups.
     */
    public File getCarpetaBackups() {
        return resolverCarpetaBackups();
    }

    // ── Lógica interna ─────────────────────────────────────────────────────────

    private synchronized File realizarBackup() {
        try {
            String origen = SQLiteConnectionManager.rutaDb;
            if (origen == null || origen.isBlank()) {
                ultimoMensaje = "Error: ruta de BD no disponible.";
                return null;
            }

            Path origenPath = Paths.get(origen);
            if (!Files.exists(origenPath)) {
                ultimoMensaje = "Error: archivo de BD no encontrado en " + origen;
                return null;
            }

            File carpeta = resolverCarpetaBackups();
            String nombreArchivo = "gestor_libros_"
                    + LocalDateTime.now().format(FORMATO_FECHA) + ".db";
            Path destino = carpeta.toPath().resolve(nombreArchivo);

            Files.copy(origenPath, destino, StandardCopyOption.REPLACE_EXISTING);

            limpiarBackupsAntiguos(carpeta);

            ultimoMensaje = "Último backup: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            System.out.println("[BackupService] Backup creado: " + destino);
            return destino.toFile();

        } catch (IOException e) {
            ultimoMensaje = "Error al realizar backup: " + e.getMessage();
            System.err.println("[BackupService] " + ultimoMensaje);
            e.printStackTrace();
            return null;
        }
    }

    private File resolverCarpetaBackups() {
        String appData = System.getenv("APPDATA");
        if (appData == null) appData = System.getProperty("user.home");

        File carpeta = new File(appData, "GestorLibros" + File.separator + "backups");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        return carpeta;
    }

    private void limpiarBackupsAntiguos(File carpeta) {
        File[] archivos = carpeta.listFiles(
                f -> f.isFile() && f.getName().endsWith(".db"));

        if (archivos == null || archivos.length <= MAX_BACKUPS) return;

        // Ordenar por fecha de modificación, más antiguos primero
        Arrays.sort(archivos, Comparator.comparingLong(File::lastModified));

        int aEliminar = archivos.length - MAX_BACKUPS;
        for (int i = 0; i < aEliminar; i++) {
            boolean eliminado = archivos[i].delete();
            if (eliminado) {
                System.out.println("[BackupService] Backup antiguo eliminado: "
                        + archivos[i].getName());
            }
        }
    }
}
