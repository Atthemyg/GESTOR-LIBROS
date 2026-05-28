package gestor.libros.app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import gestor.libros.app.model.Autor;
import gestor.libros.app.repository.interfaces.IAutorRepository;
import gestor.libros.database.sqlite.SQLiteConnectionManager;

public class AutorRepository extends SQLiteConnectionManager implements IAutorRepository {

    public AutorRepository() {
        super(rutaDb);
    }

    // Asegura que la columna foto_url existe (migración automática)
    private void asegurarColumnaFoto(Connection connection) {
        try {
            connection.createStatement().execute(
                "ALTER TABLE autores ADD COLUMN foto_url TEXT"
            );
        } catch (Exception ignored) {
            // Ya existe, no pasa nada
        }
    }

    private Autor mapear(ResultSet rs) throws Exception {
        String fotoUrl = null;
        try { fotoUrl = rs.getString("foto_url"); } catch (Exception ignored) {}
        return new Autor(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("nacionalidad"),
                rs.getString("biografia"),
                rs.getInt("favorito") == 1,
                fotoUrl
        );
    }

    @Override
    public boolean create(Autor autor) {
        try (Connection connection = getConnection()) {
            asegurarColumnaFoto(connection);
            PreparedStatement sentencia = connection.prepareStatement(
                "INSERT INTO autores (nombre, nacionalidad, biografia, favorito, foto_url) VALUES (?, ?, ?, ?, ?)");
            sentencia.setString(1, autor.getNombre());
            sentencia.setString(2, autor.getNacionalidad());
            sentencia.setString(3, autor.getBiografia());
            sentencia.setInt(4, autor.isFavorito() ? 1 : 0);
            sentencia.setString(5, autor.getFotoUrl());
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error creando autor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Autor findById(Integer id) {
        try (Connection connection = getConnection()) {
            asegurarColumnaFoto(connection);
            PreparedStatement sentencia = connection.prepareStatement(
                "SELECT * FROM autores WHERE id=?");
            sentencia.setInt(1, id);
            ResultSet rs = sentencia.executeQuery();
            if (!rs.next()) return null;
            return mapear(rs);
        } catch (Exception e) {
            System.err.println("Error buscando autor: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Autor> findAll() {
        try (Connection connection = getConnection()) {
            asegurarColumnaFoto(connection);
            PreparedStatement sentencia = connection.prepareStatement(
                "SELECT * FROM autores ORDER BY nombre");
            List<Autor> autores = new ArrayList<>();
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) autores.add(mapear(rs));
            return autores;
        } catch (Exception e) {
            System.err.println("Error listando autores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Autor> findFavoritos() {
        try (Connection connection = getConnection()) {
            asegurarColumnaFoto(connection);
            PreparedStatement sentencia = connection.prepareStatement(
                "SELECT * FROM autores WHERE favorito=1 ORDER BY nombre");
            List<Autor> autores = new ArrayList<>();
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) autores.add(mapear(rs));
            return autores;
        } catch (Exception e) {
            System.err.println("Error buscando autores favoritos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean update(Autor autor) {
        try (Connection connection = getConnection()) {
            asegurarColumnaFoto(connection);
            PreparedStatement sentencia = connection.prepareStatement(
                "UPDATE autores SET nombre=?, nacionalidad=?, biografia=?, favorito=?, foto_url=? WHERE id=?");
            sentencia.setString(1, autor.getNombre());
            sentencia.setString(2, autor.getNacionalidad());
            sentencia.setString(3, autor.getBiografia());
            sentencia.setInt(4, autor.isFavorito() ? 1 : 0);
            sentencia.setString(5, autor.getFotoUrl());
            sentencia.setInt(6, autor.getId());
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error actualizando autor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "DELETE FROM autores WHERE id=?")) {
            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error eliminando autor: " + e.getMessage());
            return false;
        }
    }
}
