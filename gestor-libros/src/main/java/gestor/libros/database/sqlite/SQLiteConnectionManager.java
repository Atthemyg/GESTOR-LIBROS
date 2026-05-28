package gestor.libros.database.sqlite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnectionManager {

    public static String rutaDb = obtenerRutaDb();

    public SQLiteConnectionManager(String rutaDb) {
        SQLiteConnectionManager.rutaDb = rutaDb;
    }

    private static String obtenerRutaDb() {
        // Guarda la BD en C:\Users\<usuario>\AppData\Roaming\GestorLibros\
        String appData = System.getenv("APPDATA");
        if (appData == null) {
            appData = System.getProperty("user.home");
        }

        File carpeta = new File(appData, "GestorLibros");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        File db = new File(carpeta, "gestor_libros.db");

        // Si la BD no existe aún, copia la que viene dentro del .jar (con las tablas ya creadas)
        if (!db.exists()) {
            try (InputStream is = SQLiteConnectionManager.class
                    .getResourceAsStream("/database/gestor_libros.db")) {
                if (is != null) {
                    Files.copy(is, db.toPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return db.getAbsolutePath();
    }

    public static Connection openConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + rutaDb);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    public Connection getConnection() throws SQLException {
        return openConnection();
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("No se pudo cerrar la conexion", e);
            }
        }
    }
}