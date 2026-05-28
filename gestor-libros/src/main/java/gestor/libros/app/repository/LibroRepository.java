package gestor.libros.app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import gestor.libros.app.model.Libro;
import gestor.libros.app.repository.interfaces.ILibroRepository;
import gestor.libros.database.sqlite.SQLiteConnectionManager;

public class LibroRepository extends SQLiteConnectionManager implements ILibroRepository {

    public LibroRepository() {
        super(rutaDb);
    }

    @Override
    public boolean create(Libro libro) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "INSERT INTO libros (titulo, autor_id, isbn, anio_publicacion, genero, sinopsis, portada_url) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            sentencia.setString(1, libro.getTitulo());
            sentencia.setObject(2, libro.getAutorId());
            sentencia.setString(3, libro.getIsbn());
            sentencia.setObject(4, libro.getAnioPublicacion());
            sentencia.setString(5, libro.getGenero());
            sentencia.setString(6, libro.getSinopsis());
            sentencia.setString(7, libro.getPortadaUrl());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error creando libro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Libro findById(Integer id) {
        String sql = """
            SELECT l.*, a.nombre AS autor_nombre
            FROM libros l
            LEFT JOIN autores a ON l.autor_id = a.id
            WHERE l.id=?
        """;
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setInt(1, id);
            ResultSet rs = sentencia.executeQuery();

            if (!rs.next()) return null;

            Libro libro = mapResultSet(rs);
            libro.setAutorNombre(rs.getString("autor_nombre"));
            return libro;

        } catch (Exception e) {
            System.err.println("Error buscando libro: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Libro> findAll() {
        String sql = """
            SELECT l.*, a.nombre AS autor_nombre
            FROM libros l
            LEFT JOIN autores a ON l.autor_id = a.id
            ORDER BY l.titulo
        """;
        return ejecutarConsulta(sql, null);
    }

    @Override
    public List<Libro> findByTitulo(String titulo) {
        String sql = """
            SELECT l.*, a.nombre AS autor_nombre
            FROM libros l
            LEFT JOIN autores a ON l.autor_id = a.id
            WHERE l.titulo LIKE ?
            ORDER BY l.titulo
        """;
        return ejecutarConsulta(sql, "%" + titulo + "%");
    }

    @Override
    public List<Libro> findByAutorId(Integer autorId) {
        String sql = """
            SELECT l.*, a.nombre AS autor_nombre
            FROM libros l
            LEFT JOIN autores a ON l.autor_id = a.id
            WHERE l.autor_id=?
            ORDER BY l.titulo
        """;
        return ejecutarConsulta(sql, autorId);
    }

    private List<Libro> ejecutarConsulta(String sql, Object param) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(sql)) {

            if (param != null) sentencia.setObject(1, param);

            List<Libro> libros = new ArrayList<>();
            ResultSet rs = sentencia.executeQuery();

            while (rs.next()) {
                Libro libro = mapResultSet(rs);
                libro.setAutorNombre(rs.getString("autor_nombre"));
                libros.add(libro);
            }
            return libros;

        } catch (Exception e) {
            System.err.println("Error listando libros: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private Libro mapResultSet(ResultSet rs) throws Exception {
        int id = rs.getInt("id");
        String titulo = rs.getString("titulo");
        int autorId = rs.getInt("autor_id");
        String isbn = rs.getString("isbn");
        int anio = rs.getInt("anio_publicacion");
        String genero = rs.getString("genero");
        String sinopsis = rs.getString("sinopsis");
        String portadaUrl = rs.getString("portada_url");
        return new Libro(id, titulo, autorId == 0 ? null : autorId, isbn,
                anio == 0 ? null : anio, genero, sinopsis, portadaUrl);
    }

    @Override
    public boolean update(Libro libro) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "UPDATE libros SET titulo=?, autor_id=?, isbn=?, anio_publicacion=?, genero=?, sinopsis=?, portada_url=? WHERE id=?")) {

            sentencia.setString(1, libro.getTitulo());
            sentencia.setObject(2, libro.getAutorId());
            sentencia.setString(3, libro.getIsbn());
            sentencia.setObject(4, libro.getAnioPublicacion());
            sentencia.setString(5, libro.getGenero());
            sentencia.setString(6, libro.getSinopsis());
            sentencia.setString(7, libro.getPortadaUrl());
            sentencia.setInt(8, libro.getId());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error actualizando libro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "DELETE FROM libros WHERE id=?")) {

            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error eliminando libro: " + e.getMessage());
            return false;
        }
    }
}
