package gestor.libros.app.controller;

import gestor.libros.app.model.Autor;
import gestor.libros.app.model.Libro;
import gestor.libros.app.service.AutorService;
import gestor.libros.app.service.LibroService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.Optional;

public class LibrosController {

    // ── Tabla ────────────────────────────────────────────────────────────────
    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro, String>  colTitulo;
    @FXML private TableColumn<Libro, String>  colAutor;
    @FXML private TableColumn<Libro, String>  colGenero;
    @FXML private TableColumn<Libro, Integer> colAnio;
    @FXML private TableColumn<Libro, String>  colIsbn;

    // ── Vista cuadrícula ─────────────────────────────────────────────────────
    @FXML private StackPane   stackCentro;
    @FXML private ScrollPane  scrollCuadricula;
    @FXML private FlowPane    flowPortadas;

    // ── Toggle vistas ────────────────────────────────────────────────────────
    @FXML private ToggleButton btnVistaCuadricula;

    // ── Formulario ───────────────────────────────────────────────────────────
    @FXML private TextField      txtTitulo;
    @FXML private ComboBox<Autor> cmbAutor;
    @FXML private TextField      txtIsbn;
    @FXML private TextField      txtAnio;
    @FXML private TextField      txtGenero;
    @FXML private TextArea       txtSinopsis;
    @FXML private TextField      txtPortadaUrl;
    @FXML private TextField      txtBuscar;

    // ── Botones ──────────────────────────────────────────────────────────────
    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;

    // ── Servicios ────────────────────────────────────────────────────────────
    private final LibroService  libroService  = new LibroService();
    private final AutorService  autorService  = new AutorService();

    private final ObservableList<Libro> datos = FXCollections.observableArrayList();
    private Libro libroSeleccionado = null;
    private boolean vistaCuadricula = false;

    // ─────────────────────────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarAutores();
        cargarLibros();
        configurarSeleccion();
        btnEliminar.setDisable(true);
        txtBuscar.textProperty().addListener((obs, anterior, actual) -> {
            if (actual == null || actual.isBlank()) {
                datos.setAll(libroService.findAll());
            } else {
                datos.setAll(libroService.findByTitulo(actual.trim()));
            }
            if (vistaCuadricula) refrescarCuadricula();
        });
    }

    // ── Configuración ─────────────────────────────────────────────────────────

    private void configurarColumnas() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor .setCellValueFactory(new PropertyValueFactory<>("autorNombre"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colAnio  .setCellValueFactory(new PropertyValueFactory<>("anioPublicacion"));
        colIsbn  .setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tablaLibros.setItems(datos);
    }

    private void cargarAutores() {
        List<Autor> autores = autorService.findAll();
        cmbAutor.setItems(FXCollections.observableArrayList(autores));
    }

    private void cargarLibros() {
        datos.setAll(libroService.findAll());
        if (vistaCuadricula) refrescarCuadricula();
    }

    private void configurarSeleccion() {
        tablaLibros.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, actual) -> {
                    libroSeleccionado = actual;
                    if (actual != null) {
                        rellenarFormulario(actual);
                        btnEliminar.setDisable(false);
                    } else {
                        btnEliminar.setDisable(true);
                    }
                });
    }

    // ── Toggle de vista ───────────────────────────────────────────────────────

    @FXML
    public void onToggleVista() {
        vistaCuadricula = btnVistaCuadricula.isSelected();
        tablaLibros.setVisible(!vistaCuadricula);
        tablaLibros.setManaged(!vistaCuadricula);
        scrollCuadricula.setVisible(vistaCuadricula);
        scrollCuadricula.setManaged(vistaCuadricula);
        if (vistaCuadricula) refrescarCuadricula();
    }

    private void refrescarCuadricula() {
        flowPortadas.getChildren().clear();
        for (Libro libro : datos) {
            flowPortadas.getChildren().add(crearTarjetaPortada(libro));
        }
    }

    private VBox crearTarjetaPortada(Libro libro) {
        // Imagen de portada
        ImageView imgView = new ImageView();
        imgView.setFitWidth(130);
        imgView.setFitHeight(190);
        imgView.setPreserveRatio(false);
        imgView.setSmooth(true);

        String url = libro.getPortadaUrl();
        if (url != null && !url.isBlank()) {
            try {
                Image img = new Image(url, 130, 190, false, true, true);
                imgView.setImage(img);
            } catch (Exception ignored) {}
        }

        // Placeholder si no hay imagen o falla
        StackPane imgContainer = new StackPane();
        imgContainer.getStyleClass().add("portada-container");
        imgContainer.setPrefSize(130, 190);
        imgContainer.setMinSize(130, 190);
        imgContainer.setMaxSize(130, 190);

        Label placeholder = new Label("📖");
        placeholder.setStyle("-fx-font-size: 40px;");
        imgContainer.getChildren().add(placeholder);
        if (imgView.getImage() != null) {
            imgContainer.getChildren().add(imgView);
        }

        // Título debajo
        Label lblTitulo = new Label(libro.getTitulo());
        lblTitulo.setMaxWidth(130);
        lblTitulo.setWrapText(true);
        lblTitulo.setTextAlignment(TextAlignment.CENTER);
        lblTitulo.setAlignment(Pos.CENTER);
        lblTitulo.getStyleClass().add("portada-titulo");

        // Autor debajo del título
        Label lblAutor = new Label(libro.getAutorNombre() != null ? libro.getAutorNombre() : "");
        lblAutor.setMaxWidth(130);
        lblAutor.setWrapText(true);
        lblAutor.setTextAlignment(TextAlignment.CENTER);
        lblAutor.setAlignment(Pos.CENTER);
        lblAutor.getStyleClass().add("portada-autor");

        VBox card = new VBox(6, imgContainer, lblTitulo, lblAutor);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(140);
        card.getStyleClass().add("portada-card");

        // Seleccionar al hacer click
        card.setOnMouseClicked(e -> {
            libroSeleccionado = libro;
            rellenarFormulario(libro);
            btnEliminar.setDisable(false);
            // Resaltar tarjeta seleccionada
            flowPortadas.getChildren().forEach(n -> n.getStyleClass().remove("portada-card-selected"));
            card.getStyleClass().add("portada-card-selected");
        });

        return card;
    }

    // ── Acciones ──────────────────────────────────────────────────────────────

    @FXML
    public void onNuevo() {
        libroSeleccionado = null;
        limpiarFormulario();
        tablaLibros.getSelectionModel().clearSelection();
        flowPortadas.getChildren().forEach(n -> n.getStyleClass().remove("portada-card-selected"));
        btnEliminar.setDisable(true);
        txtTitulo.requestFocus();
    }

    @FXML
    public void onGuardar() {
        if (!validar()) return;

        Autor autor = cmbAutor.getValue();
        Integer anio = txtAnio.getText().isBlank() ? null
                : Integer.parseInt(txtAnio.getText().trim());

        if (libroSeleccionado == null) {
            Libro nuevo = new Libro(
                    txtTitulo.getText().trim(),
                    autor != null ? autor.getId() : null,
                    txtIsbn.getText().trim(),
                    anio,
                    txtGenero.getText().trim(),
                    txtSinopsis.getText().trim(),
                    txtPortadaUrl.getText().trim());
            if (libroService.create(nuevo)) {
                mostrarInfo("Libro guardado correctamente.");
                cargarLibros();
                limpiarFormulario();
            }
        } else {
            libroSeleccionado.setTitulo(txtTitulo.getText().trim());
            libroSeleccionado.setAutorId(autor != null ? autor.getId() : null);
            libroSeleccionado.setIsbn(txtIsbn.getText().trim());
            libroSeleccionado.setAnioPublicacion(anio);
            libroSeleccionado.setGenero(txtGenero.getText().trim());
            libroSeleccionado.setSinopsis(txtSinopsis.getText().trim());
            libroSeleccionado.setPortadaUrl(txtPortadaUrl.getText().trim());
            if (libroService.update(libroSeleccionado)) {
                mostrarInfo("Libro actualizado correctamente.");
                cargarLibros();
            }
        }
    }

    @FXML
    public void onEliminar() {
        if (libroSeleccionado == null) return;

        Optional<ButtonType> resp = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar \"" + libroSeleccionado.getTitulo() + "\"?\n"
                        + "Se eliminarán también sus lecturas, notas y citas.",
                ButtonType.OK, ButtonType.CANCEL).showAndWait();

        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            libroService.deleteById(libroSeleccionado.getId());
            cargarLibros();
            limpiarFormulario();
            libroSeleccionado = null;
        }
    }

    @FXML
    public void onBuscar() {
        String texto = txtBuscar.getText().trim();
        datos.setAll(libroService.findByTitulo(texto));
        if (vistaCuadricula) refrescarCuadricula();
    }

    @FXML
    public void onLimpiarBusqueda() {
        txtBuscar.clear();
        cargarLibros();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void rellenarFormulario(Libro l) {
        txtTitulo.setText(l.getTitulo());
        txtIsbn.setText(l.getIsbn() != null ? l.getIsbn() : "");
        txtAnio.setText(l.getAnioPublicacion() != null ? String.valueOf(l.getAnioPublicacion()) : "");
        txtGenero.setText(l.getGenero() != null ? l.getGenero() : "");
        txtSinopsis.setText(l.getSinopsis() != null ? l.getSinopsis() : "");
        txtPortadaUrl.setText(l.getPortadaUrl() != null ? l.getPortadaUrl() : "");

        if (l.getAutorId() != null) {
            cmbAutor.getItems().stream()
                    .filter(a -> a.getId().equals(l.getAutorId()))
                    .findFirst()
                    .ifPresent(cmbAutor::setValue);
        } else {
            cmbAutor.setValue(null);
        }
    }

    private void limpiarFormulario() {
        txtTitulo.clear();
        cmbAutor.setValue(null);
        txtIsbn.clear();
        txtAnio.clear();
        txtGenero.clear();
        txtSinopsis.clear();
        txtPortadaUrl.clear();
    }

    private boolean validar() {
        if (txtTitulo.getText().isBlank()) {
            mostrarError("El título es obligatorio.");
            txtTitulo.requestFocus();
            return false;
        }
        String anioStr = txtAnio.getText().trim();
        if (!anioStr.isBlank()) {
            try {
                Integer.parseInt(anioStr);
            } catch (NumberFormatException e) {
                mostrarError("El año debe ser un número entero.");
                txtAnio.requestFocus();
                return false;
            }
        }
        return true;
    }

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private void mostrarInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
