package gestor.libros.app.controller;

import gestor.libros.app.model.Autor;
import gestor.libros.app.model.Lectura;
import gestor.libros.app.model.Libro;
import gestor.libros.app.service.AutorService;
import gestor.libros.app.service.LecturaService;
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

public class FavoritosController {

    // ── Libros favoritos ──────────────────────────────────────────────────────
    @FXML private StackPane   stackLibros;
    @FXML private TableView<Lectura>          tablaLibrosFav;
    @FXML private TableColumn<Lectura, String> colTitulo;
    @FXML private TableColumn<Lectura, String> colEstado;
    @FXML private TableColumn<Lectura, Double> colPuntuacion;
    @FXML private ScrollPane  scrollLibrosFav;
    @FXML private FlowPane    flowLibrosFav;
    @FXML private ToggleButton btnVistaLibros;

    // ── Autores favoritos ─────────────────────────────────────────────────────
    @FXML private StackPane   stackAutores;
    @FXML private TableView<Autor>           tablaAutoresFav;
    @FXML private TableColumn<Autor, String>  colNombre;
    @FXML private TableColumn<Autor, String>  colNacionalidad;
    @FXML private ScrollPane  scrollAutoresFav;
    @FXML private FlowPane    flowAutoresFav;
    @FXML private ToggleButton btnVistaAutores;

    // ── Estadísticas ──────────────────────────────────────────────────────────
    @FXML private Label lblTotalLibros;
    @FXML private Label lblLeidos;
    @FXML private Label lblEnProgreso;
    @FXML private Label lblPendientes;

    private final LecturaService lecturaService = new LecturaService();
    private final AutorService   autorService   = new AutorService();
    private final LibroService   libroService   = new LibroService();

    private final ObservableList<Lectura> datosLibros  = FXCollections.observableArrayList();
    private final ObservableList<Autor>   datosAutores = FXCollections.observableArrayList();

    private boolean vistaLibrosCuadricula  = false;
    private boolean vistaAutoresCuadricula = false;

    @FXML
    public void initialize() {
        // Columnas libros
        colTitulo    .setCellValueFactory(new PropertyValueFactory<>("libroTitulo"));
        colEstado    .setCellValueFactory(new PropertyValueFactory<>("estado"));
        colPuntuacion.setCellValueFactory(new PropertyValueFactory<>("puntuacion"));
        tablaLibrosFav.setItems(datosLibros);

        // Columnas autores
        colNombre      .setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        tablaAutoresFav.setItems(datosAutores);

        cargarDatos();
        actualizarEstadisticas();
    }

    // ── Toggle libros ─────────────────────────────────────────────────────────

    @FXML
    public void onToggleVistaLibros() {
        vistaLibrosCuadricula = btnVistaLibros.isSelected();
        tablaLibrosFav.setVisible(!vistaLibrosCuadricula);
        tablaLibrosFav.setManaged(!vistaLibrosCuadricula);
        scrollLibrosFav.setVisible(vistaLibrosCuadricula);
        scrollLibrosFav.setManaged(vistaLibrosCuadricula);
        if (vistaLibrosCuadricula) refrescarCuadriculaLibros();
    }

    private void refrescarCuadriculaLibros() {
        flowLibrosFav.getChildren().clear();
        for (Lectura lectura : datosLibros) {
            flowLibrosFav.getChildren().add(crearTarjetaLibro(lectura));
        }
    }

    private VBox crearTarjetaLibro(Lectura lectura) {
        StackPane imgContainer = new StackPane();
        imgContainer.getStyleClass().add("portada-container");
        imgContainer.setPrefSize(110, 160);
        imgContainer.setMinSize(110, 160);
        imgContainer.setMaxSize(110, 160);

        Label placeholder = new Label("📖");
        placeholder.setStyle("-fx-font-size: 36px;");
        imgContainer.getChildren().add(placeholder);

        // Buscar portadaUrl del libro
        if (lectura.getLibroId() != null) {
            try {
                Libro libro = libroService.findById(lectura.getLibroId());
                if (libro != null && libro.getPortadaUrl() != null && !libro.getPortadaUrl().isBlank()) {
                    ImageView imgView = new ImageView();
                    imgView.setFitWidth(110);
                    imgView.setFitHeight(160);
                    imgView.setPreserveRatio(false);
                    imgView.setSmooth(true);
                    Image img = new Image(libro.getPortadaUrl(), 110, 160, false, true, true);
                    imgView.setImage(img);
                    imgContainer.getChildren().add(imgView);
                }
            } catch (Exception ignored) {}
        }

        Label lblTitulo = new Label(lectura.getLibroTitulo() != null ? lectura.getLibroTitulo() : "");
        lblTitulo.setMaxWidth(110);
        lblTitulo.setWrapText(true);
        lblTitulo.setTextAlignment(TextAlignment.CENTER);
        lblTitulo.setAlignment(Pos.CENTER);
        lblTitulo.getStyleClass().add("portada-titulo");

        Label lblEstado = new Label(lectura.getEstado() != null ? lectura.getEstado() : "");
        lblEstado.setMaxWidth(110);
        lblEstado.setTextAlignment(TextAlignment.CENTER);
        lblEstado.setAlignment(Pos.CENTER);
        lblEstado.getStyleClass().add("portada-autor");

        VBox card = new VBox(5, imgContainer, lblTitulo, lblEstado);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(120);
        card.getStyleClass().add("portada-card");
        return card;
    }

    // ── Toggle autores ────────────────────────────────────────────────────────

    @FXML
    public void onToggleVistaAutores() {
        vistaAutoresCuadricula = btnVistaAutores.isSelected();
        tablaAutoresFav.setVisible(!vistaAutoresCuadricula);
        tablaAutoresFav.setManaged(!vistaAutoresCuadricula);
        scrollAutoresFav.setVisible(vistaAutoresCuadricula);
        scrollAutoresFav.setManaged(vistaAutoresCuadricula);
        if (vistaAutoresCuadricula) refrescarCuadriculaAutores();
    }

    private void refrescarCuadriculaAutores() {
        flowAutoresFav.getChildren().clear();
        for (Autor autor : datosAutores) {
            flowAutoresFav.getChildren().add(crearTarjetaAutor(autor));
        }
    }

    private VBox crearTarjetaAutor(Autor autor) {
        StackPane imgContainer = new StackPane();
        imgContainer.getStyleClass().add("portada-container");
        imgContainer.setPrefSize(110, 135);
        imgContainer.setMinSize(110, 135);
        imgContainer.setMaxSize(110, 135);

        Label placeholder = new Label("👤");
        placeholder.setStyle("-fx-font-size: 40px;");
        imgContainer.getChildren().add(placeholder);

        String url = autor.getFotoUrl();
        if (url != null && !url.isBlank()) {
            try {
                ImageView imgView = new ImageView();
                imgView.setFitWidth(110);
                imgView.setFitHeight(135);
                imgView.setPreserveRatio(false);
                imgView.setSmooth(true);
                Image img = new Image(url, 110, 135, false, true, true);
                imgView.setImage(img);
                imgContainer.getChildren().add(imgView);
            } catch (Exception ignored) {}
        }

        Label lblNombre = new Label(autor.getNombre());
        lblNombre.setMaxWidth(110);
        lblNombre.setWrapText(true);
        lblNombre.setTextAlignment(TextAlignment.CENTER);
        lblNombre.setAlignment(Pos.CENTER);
        lblNombre.getStyleClass().add("portada-titulo");

        Label lblNac = new Label(autor.getNacionalidad() != null ? autor.getNacionalidad() : "");
        lblNac.setMaxWidth(110);
        lblNac.setTextAlignment(TextAlignment.CENTER);
        lblNac.setAlignment(Pos.CENTER);
        lblNac.getStyleClass().add("portada-autor");

        VBox card = new VBox(5, imgContainer, lblNombre, lblNac);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(120);
        card.getStyleClass().add("portada-card");
        return card;
    }

    // ── Refresco ──────────────────────────────────────────────────────────────

    @FXML
    public void onRefrescar() {
        cargarDatos();
        actualizarEstadisticas();
        if (vistaLibrosCuadricula)  refrescarCuadriculaLibros();
        if (vistaAutoresCuadricula) refrescarCuadriculaAutores();
    }

    private void cargarDatos() {
        datosLibros .setAll(lecturaService.findFavoritos());
        datosAutores.setAll(autorService.findFavoritos());
    }

    private void actualizarEstadisticas() {
        var todas = lecturaService.findAll();
        long leidos     = todas.stream().filter(l -> "LEIDO"    .equals(l.getEstado())).count();
        long enProgreso = todas.stream().filter(l -> "LEYENDO"  .equals(l.getEstado())).count();
        long pendientes = todas.stream().filter(l -> "PENDIENTE".equals(l.getEstado())).count();

        lblTotalLibros.setText(String.valueOf(todas.size()));
        lblLeidos     .setText(String.valueOf(leidos));
        lblEnProgreso .setText(String.valueOf(enProgreso));
        lblPendientes .setText(String.valueOf(pendientes));
    }
}
