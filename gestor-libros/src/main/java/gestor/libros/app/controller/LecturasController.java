package gestor.libros.app.controller;

import gestor.libros.app.model.*;
import gestor.libros.app.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LecturasController {

    // ── Tabla lecturas ────────────────────────────────────────────────────────
    @FXML private TableView<Lectura>         tablaLecturas;
    @FXML private TableColumn<Lectura,String>  colLibro;
    @FXML private TableColumn<Lectura,String>  colEstado;
    @FXML private TableColumn<Lectura,String>  colFechaInicio;
    @FXML private TableColumn<Lectura,String>  colFechaFin;
    @FXML private TableColumn<Lectura,Double>  colPuntuacion;

    // ── Filtro de estado ──────────────────────────────────────────────────────
    @FXML private ComboBox<String> cmbFiltroEstado;

    // ── Formulario de lectura ─────────────────────────────────────────────────
    @FXML private ComboBox<Libro>  cmbLibro;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TextField        txtPaginaActual;
    @FXML private TextField        txtTotalPaginas;
    @FXML private DatePicker       dpFechaInicio;
    @FXML private DatePicker       dpFechaFin;
    @FXML private Slider           sliderPuntuacion;
    @FXML private Label            lblPuntuacion;
    @FXML private CheckBox         chkFavorito;
    @FXML private ProgressBar      barProgreso;
    @FXML private Label            lblProgreso;

    // ── Notas ─────────────────────────────────────────────────────────────────
    @FXML private TableView<Nota>          tablaNotas;
    @FXML private TableColumn<Nota,Integer> colNotaPagina;
    @FXML private TableColumn<Nota,String>  colNotaContenido;
    @FXML private TextArea                  txtNota;
    @FXML private TextField                 txtNotaPagina;

    // ── Citas ─────────────────────────────────────────────────────────────────
    @FXML private TableView<Cita>          tablaCitas;
    @FXML private TableColumn<Cita,Integer> colCitaPagina;
    @FXML private TableColumn<Cita,String>  colCitaTexto;
    @FXML private TextArea                  txtCita;
    @FXML private TextField                 txtCitaPagina;

    // ── Botones ───────────────────────────────────────────────────────────────
    @FXML private Button btnGuardarLectura;
    @FXML private Button btnEliminarLectura;
    @FXML private Button btnNuevaLectura;
    @FXML private Button btnMarcarLeido;

    // ── Servicios ─────────────────────────────────────────────────────────────
    private final LecturaService lecturaService = new LecturaService();
    private final LibroService   libroService   = new LibroService();
    private final NotaService    notaService    = new NotaService();
    private final CitaService    citaService    = new CitaService();

    private final ObservableList<Lectura> datos      = FXCollections.observableArrayList();
    private final ObservableList<Nota>    datosNotas = FXCollections.observableArrayList();
    private final ObservableList<Cita>    datosCitas = FXCollections.observableArrayList();

    private Lectura lecturaSeleccionada = null;

    // ─────────────────────────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        configurarFiltroEstado();
        configurarTablaLecturas();
        configurarFormulario();
        configurarTablaNotas();
        configurarTablaCitas();
        cargarLibros();
        cargarLecturas(null);
        btnEliminarLectura.setDisable(true);
        btnMarcarLeido.setDisable(true);
    }

    // ── Configuración ─────────────────────────────────────────────────────────

    private void configurarFiltroEstado() {
        cmbFiltroEstado.setItems(FXCollections.observableArrayList(
                "Todos", "PENDIENTE", "LEYENDO", "LEIDO", "ABANDONADO"));
        cmbFiltroEstado.setValue("Todos");
        cmbFiltroEstado.setOnAction(e -> {
            String f = cmbFiltroEstado.getValue();
            cargarLecturas("Todos".equals(f) ? null : f);
        });
    }

    private void configurarTablaLecturas() {
        colLibro      .setCellValueFactory(new PropertyValueFactory<>("libroTitulo"));
        colEstado     .setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFin   .setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colPuntuacion .setCellValueFactory(new PropertyValueFactory<>("puntuacion"));
        tablaLecturas.setItems(datos);

        tablaLecturas.getSelectionModel().selectedItemProperty().addListener(
                (obs, ant, actual) -> {
                    lecturaSeleccionada = actual;
                    if (actual != null) {
                        rellenarFormulario(actual);
                        cargarNotasDeLectura(actual.getId());
                        cargarCitasDeLectura(actual.getId());
                        btnEliminarLectura.setDisable(false);
                        btnMarcarLeido.setDisable("LEIDO".equals(actual.getEstado()));
                    } else {
                        limpiarFormulario();
                        datosNotas.clear();
                        datosCitas.clear();
                        btnEliminarLectura.setDisable(true);
                        btnMarcarLeido.setDisable(true);
                    }
                });
    }

    private void configurarFormulario() {
        cmbEstado.setItems(FXCollections.observableArrayList(
                "PENDIENTE", "LEYENDO", "LEIDO", "ABANDONADO"));
        cmbEstado.setValue("PENDIENTE");

        sliderPuntuacion.setMin(0);
        sliderPuntuacion.setMax(5);
        sliderPuntuacion.setMajorTickUnit(1);
        sliderPuntuacion.setSnapToTicks(true);
        sliderPuntuacion.valueProperty().addListener((obs, ant, val) -> {
            double v = Math.round(val.doubleValue() * 2) / 2.0;
            lblPuntuacion.setText(String.format("%.1f ⭐", v));
        });
        lblPuntuacion.setText("0.0 ⭐");
    }

    private void configurarTablaNotas() {
        colNotaPagina  .setCellValueFactory(new PropertyValueFactory<>("pagina"));
        colNotaContenido.setCellValueFactory(new PropertyValueFactory<>("contenido"));
        tablaNotas.setItems(datosNotas);
    }

    private void configurarTablaCitas() {
        colCitaPagina.setCellValueFactory(new PropertyValueFactory<>("pagina"));
        colCitaTexto .setCellValueFactory(new PropertyValueFactory<>("texto"));
        tablaCitas.setItems(datosCitas);
    }

    private void cargarLibros() {
        cmbLibro.setItems(FXCollections.observableArrayList(libroService.findAll()));
    }

    private void cargarLecturas(String estado) {
        List<Lectura> lista = (estado == null)
                ? lecturaService.findAll()
                : lecturaService.findAll().stream()
                    .filter(l -> estado.equals(l.getEstado()))
                    .toList();
        datos.setAll(lista);
    }

    private void cargarNotasDeLectura(int lecturaId) {
        datosNotas.setAll(notaService.findByLecturaId(lecturaId));
    }

    private void cargarCitasDeLectura(int lecturaId) {
        datosCitas.setAll(citaService.findByLecturaId(lecturaId));
    }

    // ── Acciones – Lectura ────────────────────────────────────────────────────

    @FXML
    public void onNuevaLectura() {
        lecturaSeleccionada = null;
        tablaLecturas.getSelectionModel().clearSelection();
        limpiarFormulario();
        datosNotas.clear();
        datosCitas.clear();
        cmbLibro.requestFocus();
    }

    @FXML
    public void onGuardarLectura() {
        if (cmbLibro.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Selecciona un libro.", ButtonType.OK).showAndWait();
            return;
        }

        int libroId    = cmbLibro.getValue().getId();
        String estado  = cmbEstado.getValue();
        boolean fav    = chkFavorito.isSelected();
        int pagActual  = parsearInt(txtPaginaActual.getText(), 0);
        int pagTotal   = parsearInt(txtTotalPaginas.getText(), 0);
        String fInicio = dpFechaInicio.getValue() != null ? dpFechaInicio.getValue().toString() : null;
        String fFin    = dpFechaFin.getValue()    != null ? dpFechaFin.getValue().toString()    : null;
        double puntos  = Math.round(sliderPuntuacion.getValue() * 2) / 2.0;

        if (lecturaSeleccionada == null) {
            Lectura nueva = new Lectura(libroId, estado, fav, pagActual, pagTotal,
                    fInicio, fFin, puntos > 0 ? puntos : null);
            if (lecturaService.create(nueva)) {
                mostrarInfo("Lectura registrada.");
            }
        } else {
            lecturaSeleccionada.setLibroId(libroId);
            lecturaSeleccionada.setEstado(estado);
            lecturaSeleccionada.setFavorito(fav);
            lecturaSeleccionada.setPaginaActual(pagActual);
            lecturaSeleccionada.setTotalPaginas(pagTotal);
            lecturaSeleccionada.setFechaInicio(fInicio);
            lecturaSeleccionada.setFechaFin(fFin);
            lecturaSeleccionada.setPuntuacion(puntos > 0 ? puntos : null);
            if (lecturaService.update(lecturaSeleccionada)) {
                mostrarInfo("Lectura actualizada.");
            }
        }
        String filtro = cmbFiltroEstado.getValue();
        cargarLecturas("Todos".equals(filtro) ? null : filtro);
        actualizarBarraProgreso(pagActual, pagTotal);
    }

    @FXML
    public void onEliminarLectura() {
        if (lecturaSeleccionada == null) return;
        Optional<ButtonType> r = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar esta lectura junto con sus notas y citas?",
                ButtonType.OK, ButtonType.CANCEL).showAndWait();
        if (r.isPresent() && r.get() == ButtonType.OK) {
            lecturaService.deleteById(lecturaSeleccionada.getId());
            cargarLecturas(null);
            limpiarFormulario();
            datosNotas.clear();
            datosCitas.clear();
            lecturaSeleccionada = null;
        }
    }

    @FXML
    public void onMarcarLeido() {
        if (lecturaSeleccionada == null) return;
        lecturaService.marcarComoLeido(lecturaSeleccionada.getId());
        cargarLecturas(null);
        mostrarInfo("Libro marcado como leído. ¡Enhorabuena!");
    }

    @FXML
    public void onToggleFavorito() {
        if (lecturaSeleccionada == null) return;
        lecturaService.toggleFavorito(lecturaSeleccionada.getId());
        cargarLecturas(null);
    }

    // ── Acciones – Notas ─────────────────────────────────────────────────────

    @FXML
    public void onGuardarNota() {
        if (lecturaSeleccionada == null) {
            mostrarError("Selecciona primero una lectura.");
            return;
        }
        if (txtNota.getText().isBlank()) {
            mostrarError("Escribe el contenido de la nota.");
            return;
        }
        Nota nota = new Nota(
                lecturaSeleccionada.getId(),
                txtNota.getText().trim(),
                parsearInt(txtNotaPagina.getText(), 0),
                LocalDate.now().toString());
        if (notaService.create(nota)) {
            txtNota.clear();
            txtNotaPagina.clear();
            cargarNotasDeLectura(lecturaSeleccionada.getId());
        }
    }

    @FXML
    public void onEliminarNota() {
        Nota nota = tablaNotas.getSelectionModel().getSelectedItem();
        if (nota == null) return;
        notaService.deleteById(nota.getId());
        cargarNotasDeLectura(lecturaSeleccionada.getId());
    }

    // ── Acciones – Citas ─────────────────────────────────────────────────────

    @FXML
    public void onGuardarCita() {
        if (lecturaSeleccionada == null) {
            mostrarError("Selecciona primero una lectura.");
            return;
        }
        if (txtCita.getText().isBlank()) {
            mostrarError("Escribe el texto de la cita.");
            return;
        }
        Cita cita = new Cita(
                lecturaSeleccionada.getId(),
                txtCita.getText().trim(),
                parsearInt(txtCitaPagina.getText(), 0),
                LocalDate.now().toString());
        if (citaService.create(cita)) {
            txtCita.clear();
            txtCitaPagina.clear();
            cargarCitasDeLectura(lecturaSeleccionada.getId());
        }
    }

    @FXML
    public void onEliminarCita() {
        Cita cita = tablaCitas.getSelectionModel().getSelectedItem();
        if (cita == null) return;
        citaService.deleteById(cita.getId());
        cargarCitasDeLectura(lecturaSeleccionada.getId());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void rellenarFormulario(Lectura l) {
        // Libro
        libroService.findAll().stream()
                .filter(lib -> lib.getId().equals(l.getLibroId()))
                .findFirst().ifPresent(cmbLibro::setValue);

        cmbEstado.setValue(l.getEstado());
        chkFavorito.setSelected(l.isFavorito());
        txtPaginaActual.setText(String.valueOf(l.getPaginaActual()));
        txtTotalPaginas.setText(String.valueOf(l.getTotalPaginas()));
        dpFechaInicio.setValue(l.getFechaInicio() != null ? LocalDate.parse(l.getFechaInicio()) : null);
        dpFechaFin   .setValue(l.getFechaFin()    != null ? LocalDate.parse(l.getFechaFin())    : null);

        double puntos = l.getPuntuacion() != null ? l.getPuntuacion() : 0;
        sliderPuntuacion.setValue(puntos);
        lblPuntuacion.setText(String.format("%.1f ⭐", puntos));

        actualizarBarraProgreso(l.getPaginaActual(), l.getTotalPaginas());
    }

    private void limpiarFormulario() {
        cmbLibro.setValue(null);
        cmbEstado.setValue("PENDIENTE");
        chkFavorito.setSelected(false);
        txtPaginaActual.clear();
        txtTotalPaginas.clear();
        dpFechaInicio.setValue(null);
        dpFechaFin.setValue(null);
        sliderPuntuacion.setValue(0);
        lblPuntuacion.setText("0.0 ⭐");
        barProgreso.setProgress(0);
        lblProgreso.setText("0%");
    }

    private void actualizarBarraProgreso(int actual, int total) {
        double pct = (total > 0) ? (double) actual / total : 0;
        barProgreso.setProgress(pct);
        lblProgreso.setText(String.format("%.0f%%", pct * 100));
    }

    private int parsearInt(String s, int defecto) {
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return defecto; }
    }

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private void mostrarInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
