package gestor.libros.app;

import java.time.LocalDate;
import java.util.List;

import gestor.libros.app.model.*;
import gestor.libros.app.service.*;
import gestor.libros.database.sqlite.DatabaseInitializer;

public class Main {
    /*public static void main(String[] args) {

        // 1. Inicializar base de datos (crea tablas si no existen)
        new DatabaseInitializer().inicializar();

        AutorService autorService     = new AutorService();
        LibroService libroService     = new LibroService();
        LecturaService lecturaService = new LecturaService();
        NotaService notaService       = new NotaService();
        CitaService citaService       = new CitaService();

        // ── AUTORES ────────────────────────────────────────────────
        System.out.println("\n=== AUTORES ===");
        Autor a1 = new Autor("Gabriel García Márquez", "Colombiana",
                "Premio Nobel de Literatura 1982.", false);
        Autor a2 = new Autor("Jorge Luis Borges", "Argentina",
                "Maestro del cuento fantástico.", true);

        autorService.create(a1);
        autorService.create(a2);

        List<Autor> autores = autorService.findAll();
        autores.forEach(a -> System.out.println("  - [" + a.getId() + "] " + a.getNombre()
                + (a.isFavorito() ? " ⭐" : "")));

        // ── LIBROS ─────────────────────────────────────────────────
        System.out.println("\n=== LIBROS ===");
        // Obtenemos los IDs generados
        int idGGM   = autores.get(0).getId();
        int idBorges = autores.get(1).getId();

        Libro l1 = new Libro("Cien años de soledad", idGGM,
                "9780307474728", 1967, "Realismo mágico",
                "La saga de la familia Buendía en Macondo.", null);
        Libro l2 = new Libro("Ficciones", idBorges,
                "9780802130303", 1944, "Cuento",
                "Colección de relatos fantásticos.", null);
        Libro l3 = new Libro("El amor en los tiempos del cólera", idGGM,
                "9780307389732", 1985, "Novela",
                "Historia de amor que dura cincuenta años.", null);

        libroService.create(l1);
        libroService.create(l2);
        libroService.create(l3);

        List<Libro> libros = libroService.findAll();
        libros.forEach(l -> System.out.println("  - [" + l.getId() + "] " + l.getTitulo()
                + " (" + l.getAutorNombre() + ")"));

        // ── LECTURAS ───────────────────────────────────────────────
        System.out.println("\n=== LECTURAS ===");
        int idL1 = libros.get(0).getId();
        int idL2 = libros.get(1).getId();
        int idL3 = libros.get(2).getId();

        Lectura lec1 = new Lectura(idL1, "LEIDO", true, 417, 417,
                "2024-01-10", "2024-02-05", 5.0);
        Lectura lec2 = new Lectura(idL2, "LEYENDO", false, 80, 224,
                LocalDate.now().toString(), null, null);
        Lectura lec3 = new Lectura(idL3, "PENDIENTE", false, 0, 348,
                null, null, null);

        lecturaService.create(lec1);
        lecturaService.create(lec2);
        lecturaService.create(lec3);

        List<Lectura> leidos = lecturaService.findLeidos();
        System.out.println("  Leídos:");
        leidos.forEach(l -> System.out.println("    · " + l.getLibroTitulo()
                + " - " + l.getPuntuacion() + "⭐"));

        List<Lectura> enProgreso = lecturaService.findEnProgreso();
        System.out.println("  En progreso:");
        enProgreso.forEach(l -> System.out.printf("    · %s — %.0f%%%n",
                l.getLibroTitulo(), l.getPorcentajeProgreso()));

        // ── ACTUALIZAR PROGRESO ────────────────────────────────────
        System.out.println("\n=== ACTUALIZAR PROGRESO ===");
        Lectura lec2guardada = lecturaService.findByLibroId(idL2);
        if (lec2guardada != null) {
            lecturaService.actualizarProgreso(lec2guardada.getId(), 150);
            Lectura actualizada = lecturaService.findById(lec2guardada.getId());
            System.out.printf("  Ficciones: página %d/%d (%.0f%%)%n",
                    actualizada.getPaginaActual(),
                    actualizada.getTotalPaginas(),
                    actualizada.getPorcentajeProgreso());
        }

        // ── NOTAS ──────────────────────────────────────────────────
        System.out.println("\n=== NOTAS ===");
        Lectura lec1guardada = lecturaService.findByLibroId(idL1);
        if (lec1guardada != null) {
            Nota n1 = new Nota(lec1guardada.getId(),
                    "El realismo mágico se manifiesta desde la primera página.",
                    1, LocalDate.now().toString());
            Nota n2 = new Nota(lec1guardada.getId(),
                    "La estructura circular del tiempo es fascinante.",
                    42, LocalDate.now().toString());
            notaService.create(n1);
            notaService.create(n2);

            List<Nota> notas = notaService.findByLecturaId(lec1guardada.getId());
            System.out.println("  Notas de \"Cien años de soledad\":");
            notas.forEach(n -> System.out.println("    · (p." + n.getPagina() + ") " + n.getContenido()));
        }

        // ── CITAS ──────────────────────────────────────────────────
        System.out.println("\n=== CITAS ===");
        if (lec1guardada != null) {
            Cita c1 = new Cita(lec1guardada.getId(),
                    "Era inevitable: el olor de las almendras amargas le recordaba siempre el destino de los amores contrariados.",
                    1, LocalDate.now().toString());
            citaService.create(c1);

            List<Cita> citas = citaService.findByLecturaId(lec1guardada.getId());
            System.out.println("  Citas guardadas: " + citas.size());
            citas.forEach(c -> System.out.println("    \"" + c.getTexto().substring(0, 40) + "...\""));
        }

        // ── FAVORITOS ──────────────────────────────────────────────
        System.out.println("\n=== FAVORITOS ===");
        List<Lectura> favoritos = lecturaService.findFavoritos();
        System.out.println("  Libros favoritos:");
        favoritos.forEach(f -> System.out.println("    ⭐ " + f.getLibroTitulo()));

        List<Autor> autoresFav = autorService.findFavoritos();
        System.out.println("  Autores favoritos:");
        autoresFav.forEach(a -> System.out.println("    ⭐ " + a.getNombre()));

        System.out.println("\n✅ Demo completada con éxito.");
    }*/
}
