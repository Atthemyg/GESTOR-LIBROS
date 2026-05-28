package gestor.libros.app;

import gestor.libros.database.sqlite.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PrincipalApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        System.out.println("INICIO APP");

        new DatabaseInitializer().inicializar();
        System.out.println("DB OK");

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/principal.fxml"));

        Scene scene = new Scene(loader.load(), 1100, 720);
        scene.getStylesheets().add(
                getClass().getResource("/css/estilos.css").toExternalForm());

        stage.setTitle("📚 Gestor de Libros");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
