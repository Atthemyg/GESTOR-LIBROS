package gestor.libros.app.controller;

import gestor.libros.app.service.BackupService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.IOException;

public class PrincipalController {

    @FXML private StackPane contenidoPrincipal;
    @FXML private Button btnLibros;
    @FXML private Button btnLecturas;
    @FXML private Button btnAutores;
    @FXML private Button btnFavoritos;
    @FXML private Button btnBackup;
    @FXML private Label  lblBackupEstado;

    private BackupService backupService;

    @FXML
    public void initialize() {
        // Iniciar el servicio de backup automático
        backupService = new BackupService();
        backupService.iniciar();

        // Actualizar la etiqueta de estado tras el backup inicial (en hilo FX)
        Platform.runLater(() -> actualizarEtiquetaBackup());

        mostrarLibros();
    }

    // ── Navegación ─────────────────────────────────────────────────────────────

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

    // ── Backup ─────────────────────────────────────────────────────────────────

    @FXML
    public void hacerBackupManual() {
        if (backupService == null) return;

        btnBackup.setDisable(true);
        btnBackup.setText("💾  Guardando…");

        // Ejecutar en hilo secundario para no bloquear la UI
        Thread t = new Thread(() -> {
            File archivo = backupService.realizarBackupManual();
            Platform.runLater(() -> {
                actualizarEtiquetaBackup();
                btnBackup.setDisable(false);
                btnBackup.setText("💾  Backup ahora");

                if (archivo != null) {
                    mostrarAlerta(Alert.AlertType.INFORMATION,
                            "Backup realizado",
                            "Se ha guardado una copia de seguridad en:\n"
                                    + archivo.getParent());
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR,
                            "Error de backup",
                            backupService.getUltimoMensaje());
                }
            });
        });
        t.setDaemon(true);
        t.start();
    }

    // ── Helpers privados ───────────────────────────────────────────────────────

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

    private void actualizarEtiquetaBackup() {
        if (lblBackupEstado != null && backupService != null) {
            lblBackupEstado.setText(backupService.getUltimoMensaje());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
