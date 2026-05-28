package gestor.libros.app.controller;

import gestor.libros.app.model.Autor;
import gestor.libros.app.service.AutorService;
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

import java.util.Optional;

public class AutoresController {

    // ── Tabla ────────────────────────────────────────────────────────────────
    @FXML private TableView<Autor>           tablaAutores;
    @FXML private TableColumn<Autor, String>  colNombre;
    @FXML private TableColumn<Autor, String>  colNacionalidad;
    @FXML private TableColumn<Autor, Boolean> colFavorito;

    // ── Vista cuadrícula ─────────────────────────────────────────────────────
    @FXML private StackPane  stackCentro;
    @FXML private ScrollPane scrollCuadricula;
    @FXML private FlowPane   flowAutores;

    // ── Toggle ───────────────────────────────────────────────────────────────
    @FXML private ToggleButton btnVistaCuadricula;

    // ── Formulario ───────────────────────────────────────────────────────────
    @FXML private TextField txtNombre;
    @FXML private TextField txtNacionalidad;
    @FXML private TextField txtFotoUrl;
    @FXML private TextArea  txtBiografia;
    @FXML private CheckBox  chkFavorito;
    @FXML private TextField txtBuscar;

    // ── Botones ──────────────────────────────────────────────────────────────
    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnNuevo;

    private final AutorService autorService = new AutorService();
    private final ObservableList<Autor> datos = FXCollections.observableArrayList();
    private Autor autorSeleccionado = null;
    private boolean vistaCuadricula = false;

    @FXML
    public void initialize() {
        colNombre      .setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colFavorito    .setCellValueFactory(new PropertyValueFactory<>("favorito"));
        colFavorito.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : (item ? "⭐" : "—"));
            }
        });

        tablaAutores.setItems(datos);
        cargarAutores();
        btnEliminar.setDisable(true);

        tablaAutores.getSelectionModel().selectedItemProperty().addListener(
                (obs, ant, actual) -> {
                    autorSeleccionado = actual;
                    if (actual != null) {
                        rellenarFormulario(actual);
                        btnEliminar.setDisable(false);
                    } else {
                        limpiarFormulario();
                        btnEliminar.setDisable(true);
                    }
                });

        txtBuscar.textProperty().addListener((obs, anterior, actual) -> {
            String q = actual == null ? "" : actual.trim().toLowerCase();
            if (q.isBlank()) {
                datos.setAll(autorService.findAll());
            } else {
                datos.setAll(autorService.findAll().stream()
                        .filter(a -> a.getNombre().toLowerCase().contains(q))
                        .toList());
            }
            if (vistaCuadricula) refrescarCuadricula();
        });
    }

    // ── Toggle de vista ───────────────────────────────────────────────────────

    @FXML
    public void onToggleVista() {
        vistaCuadricula = btnVistaCuadricula.isSelected();
        tablaAutores.setVisible(!vistaCuadricula);
        tablaAutores.setManaged(!vistaCuadricula);
        scrollCuadricula.setVisible(vistaCuadricula);
        scrollCuadricula.setManaged(vistaCuadricula);
        if (vistaCuadricula) refrescarCuadricula();
    }

    private void refrescarCuadricula() {
        flowAutores.getChildren().clear();
        for (Autor autor : datos) {
            flowAutores.getChildren().add(crearTarjetaAutor(autor));
        }
    }

    private VBox crearTarjetaAutor(Autor autor) {
        // Imagen del autor
        StackPane imgContainer = new StackPane();
        imgContainer.getStyleClass().add("portada-container");
        imgContainer.setPrefSize(130, 160);
        imgContainer.setMinSize(130, 160);
        imgContainer.setMaxSize(130, 160);

        Label placeholder = new Label("👤");
        placeholder.setStyle("-fx-font-size: 48px;");
        imgContainer.getChildren().add(placeholder);

        String url = autor.getFotoUrl();
        if (url != null && !url.isBlank()) {
            try {
                ImageView imgView = new ImageView();
                imgView.setFitWidth(130);
                imgView.setFitHeight(160);
                imgView.setPreserveRatio(false);
                imgView.setSmooth(true);
                Image img = new Image(url, 130, 160, false, true, true);
                imgView.setImage(img);
                imgContainer.getChildren().add(imgView);
            } catch (Exception ignored) {}
        }

        // Nombre
        Label lblNombre = new Label(autor.getNombre());
        lblNombre.setMaxWidth(130);
        lblNombre.setWrapText(true);
        lblNombre.setTextAlignment(TextAlignment.CENTER);
        lblNombre.setAlignment(Pos.CENTER);
        lblNombre.getStyleClass().add("portada-titulo");

        // Nacionalidad
        String nac = autor.getNacionalidad() != null ? autor.getNacionalidad() : "";
        Label lblNac = new Label(nac);
        lblNac.setMaxWidth(130);
        lblNac.setWrapText(true);
        lblNac.setTextAlignment(TextAlignment.CENTER);
        lblNac.setAlignment(Pos.CENTER);
        lblNac.getStyleClass().add("portada-autor");

        VBox card = new VBox(6, imgContainer, lblNombre, lblNac);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(140);
        card.getStyleClass().add("portada-card");

        card.setOnMouseClicked(e -> {
            autorSeleccionado = autor;
            rellenarFormulario(autor);
            btnEliminar.setDisable(false);
            flowAutores.getChildren().forEach(n -> n.getStyleClass().remove("portada-card-selected"));
            card.getStyleClass().add("portada-card-selected");
        });

        return card;
    }

    // ── Acciones ──────────────────────────────────────────────────────────────

    @FXML
    public void onNuevo() {
        autorSeleccionado = null;
        tablaAutores.getSelectionModel().clearSelection();
        flowAutores.getChildren().forEach(n -> n.getStyleClass().remove("portada-card-selected"));
        limpiarFormulario();
        btnEliminar.setDisable(true);
        txtNombre.requestFocus();
    }

    @FXML
    public void onGuardar() {
        if (txtNombre.getText().isBlank()) {
            new Alert(Alert.AlertType.ERROR, "El nombre es obligatorio.", ButtonType.OK).showAndWait();
            return;
        }

        if (autorSeleccionado == null) {
            Autor nuevo = new Autor(
                    txtNombre.getText().trim(),
                    txtNacionalidad.getText().trim(),
                    txtBiografia.getText().trim(),
                    chkFavorito.isSelected());
            nuevo.setFotoUrl(txtFotoUrl.getText().trim());
            autorService.create(nuevo);
            new Alert(Alert.AlertType.INFORMATION, "Autor guardado.", ButtonType.OK).showAndWait();
        } else {
            autorSeleccionado.setNombre(txtNombre.getText().trim());
            autorSeleccionado.setNacionalidad(txtNacionalidad.getText().trim());
            autorSeleccionado.setBiografia(txtBiografia.getText().trim());
            autorSeleccionado.setFavorito(chkFavorito.isSelected());
            autorSeleccionado.setFotoUrl(txtFotoUrl.getText().trim());
            autorService.update(autorSeleccionado);
            new Alert(Alert.AlertType.INFORMATION, "Autor actualizado.", ButtonType.OK).showAndWait();
        }
        cargarAutores();
        limpiarFormulario();
    }

    @FXML
    public void onEliminar() {
        if (autorSeleccionado == null) return;
        Optional<ButtonType> r = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar al autor \"" + autorSeleccionado.getNombre() + "\"?",
                ButtonType.OK, ButtonType.CANCEL).showAndWait();
        if (r.isPresent() && r.get() == ButtonType.OK) {
            autorService.deleteById(autorSeleccionado.getId());
            cargarAutores();
            limpiarFormulario();
            autorSeleccionado = null;
        }
    }

    @FXML
    public void onBuscar() {
        String q = txtBuscar.getText().trim().toLowerCase();
        datos.setAll(autorService.findAll().stream()
                .filter(a -> a.getNombre().toLowerCase().contains(q))
                .toList());
        if (vistaCuadricula) refrescarCuadricula();
    }

    @FXML
    public void onLimpiarBusqueda() {
        txtBuscar.clear();
        cargarAutores();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void cargarAutores() {
        datos.setAll(autorService.findAll());
        if (vistaCuadricula) refrescarCuadricula();
    }

    private void rellenarFormulario(Autor a) {
        txtNombre.setText(a.getNombre());
        txtNacionalidad.setText(a.getNacionalidad() != null ? a.getNacionalidad() : "");
        txtBiografia.setText(a.getBiografia() != null ? a.getBiografia() : "");
        txtFotoUrl.setText(a.getFotoUrl() != null ? a.getFotoUrl() : "");
        chkFavorito.setSelected(a.isFavorito());
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtNacionalidad.clear();
        txtBiografia.clear();
        txtFotoUrl.clear();
        chkFavorito.setSelected(false);
    }
}
