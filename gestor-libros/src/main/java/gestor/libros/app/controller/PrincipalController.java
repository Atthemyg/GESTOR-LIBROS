package gestor.libros.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

import java.io.IOException;

public class PrincipalController {

    @FXML private StackPane contenidoPrincipal;
    @FXML private Button btnLibros;
    @FXML private Button btnLecturas;
    @FXML private Button btnAutores;
    @FXML private Button btnFavoritos;

    @FXML
    public void initialize() {
        mostrarLibros();
    }

    @FXML
    public void mostrarLibros() {
        cargarVista("/fxml/libros.fxml");
        seleccionarBoton(btnLibros);
    }

    @FXML
    public void mostrarLecturas() {
        cargarVista("/fxml/lecturas.fxml");
        seleccionarBoton(btnLecturas);
    }

    @FXML
    public void mostrarAutores() {
        cargarVista("/fxml/autores.fxml");
        seleccionarBoton(btnAutores);
    }

    @FXML
    public void mostrarFavoritos() {
        cargarVista("/fxml/favoritos.fxml");
        seleccionarBoton(btnFavoritos);
    }

    private void cargarVista(String ruta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Node vista = loader.load();
            contenidoPrincipal.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void seleccionarBoton(Button boton) {
        for (Button b : new Button[]{btnLibros, btnLecturas, btnAutores, btnFavoritos}) {
            b.getStyleClass().remove("nav-activo");
        }
        boton.getStyleClass().add("nav-activo");
    }
}
