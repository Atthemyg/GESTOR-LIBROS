package gestor.libros.database.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer extends SQLiteConnectionManager {


    public DatabaseInitializer() {
        super(rutaDb);
    }

    public void inicializar() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // Tabla autores
            statement.execute("""
                CREATE TABLE IF NOT EXISTS autores (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre      TEXT NOT NULL,
                    nacionalidad TEXT,
                    biografia   TEXT,
                    favorito    INTEGER NOT NULL DEFAULT 0
                )
            """);

            // Tabla libros
            statement.execute("""
                CREATE TABLE IF NOT EXISTS libros (
                    id              INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo          TEXT NOT NULL,
                    autor_id        INTEGER,
                    isbn            TEXT,
                    anio_publicacion INTEGER,
                    genero          TEXT,
                    sinopsis        TEXT,
                    portada_url     TEXT,
                    FOREIGN KEY (autor_id) REFERENCES autores(id) ON DELETE SET NULL
                )
            """);

            // Tabla lecturas (estado de cada libro para el usuario)
            statement.execute("""
                CREATE TABLE IF NOT EXISTS lecturas (
                    id              INTEGER PRIMARY KEY AUTOINCREMENT,
                    libro_id        INTEGER NOT NULL,
                    estado          TEXT NOT NULL DEFAULT 'PENDIENTE',
                    favorito        INTEGER NOT NULL DEFAULT 0,
                    pagina_actual   INTEGER NOT NULL DEFAULT 0,
                    total_paginas   INTEGER NOT NULL DEFAULT 0,
                    fecha_inicio    TEXT,
                    fecha_fin       TEXT,
                    puntuacion      REAL,
                    FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE
                )
            """);

            // Tabla notas
            statement.execute("""
                CREATE TABLE IF NOT EXISTS notas (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    lectura_id  INTEGER NOT NULL,
                    contenido   TEXT NOT NULL,
                    pagina      INTEGER,
                    fecha       TEXT NOT NULL,
                    FOREIGN KEY (lectura_id) REFERENCES lecturas(id) ON DELETE CASCADE
                )
            """);

            // Tabla citas
            statement.execute("""
                CREATE TABLE IF NOT EXISTS citas (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    lectura_id  INTEGER NOT NULL,
                    texto       TEXT NOT NULL,
                    pagina      INTEGER,
                    fecha       TEXT NOT NULL,
                    FOREIGN KEY (lectura_id) REFERENCES lecturas(id) ON DELETE CASCADE
                )
            """);

            System.out.println("[DB] Base de datos inicializada correctamente.");

        } catch (SQLException e) {
            System.err.println("[DB] Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
