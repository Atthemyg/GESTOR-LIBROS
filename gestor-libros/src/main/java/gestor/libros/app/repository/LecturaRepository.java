package gestor.libros.app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import gestor.libros.app.model.Lectura;
import gestor.libros.app.repository.interfaces.ILecturaRepository;
import gestor.libros.database.sqlite.SQLiteConnectionManager;

public class LecturaRepository extends SQLiteConnectionManager implements ILecturaRepository {

    public LecturaRepository() {
        super(rutaDb);
    }

    @Override
    public boolean create(Lectura lectura) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "INSERT INTO lecturas (libro_id, estado, favorito, pagina_actual, total_paginas, fecha_inicio, fecha_fin, puntuacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            sentencia.setInt(1, lectura.getLibroId());
            sentencia.setString(2, lectura.getEstado());
            sentencia.setInt(3, lectura.isFavorito() ? 1 : 0);
            sentencia.setInt(4, lectura.getPaginaActual());
            sentencia.setInt(5, lectura.getTotalPaginas());
            sentencia.setString(6, lectura.getFechaInicio());
            sentencia.setString(7, lectura.getFechaFin());
            sentencia.setObject(8, lectura.getPuntuacion());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error creando lectura: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Lectura findById(Integer id) {
        String sql = """
            SELECT lec.*, lib.titulo AS libro_titulo
            FROM lecturas lec
            JOIN libros lib ON lec.libro_id = lib.id
            WHERE lec.id=?
        """;
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setInt(1, id);
            ResultSet rs = sentencia.executeQuery();

            if (!rs.next()) return null;
            return mapResultSet(rs);

        } catch (Exception e) {
            System.err.println("Error buscando lectura: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Lectura findByLibroId(Integer libroId) {
        String sql = """
            SELECT lec.*, lib.titulo AS libro_titulo
            FROM lecturas lec
            JOIN libros lib ON lec.libro_id = lib.id
            WHERE lec.libro_id=?
        """;
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setInt(1, libroId);
            ResultSet rs = sentencia.executeQuery();

            if (!rs.next()) return null;
            return mapResultSet(rs);

        } catch (Exception e) {
            System.err.println("Error buscando lectura por libro: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Lectura> findAll() {
        String sql = """
            SELECT lec.*, lib.titulo AS libro_titulo
            FROM lecturas lec
            JOIN libros lib ON lec.libro_id = lib.id
            ORDER BY lib.titulo
        """;
        return ejecutarConsulta(sql, null);
    }

    @Override
    public List<Lectura> findByEstado(String estado) {
        String sql = """
            SELECT lec.*, lib.titulo AS libro_titulo
            FROM lecturas lec
            JOIN libros lib ON lec.libro_id = lib.id
            WHERE lec.estado=?
            ORDER BY lib.titulo
        """;
        return ejecutarConsulta(sql, estado);
    }

    @Override
    public List<Lectura> findFavoritos() {
        String sql = """
            SELECT lec.*, lib.titulo AS libro_titulo
            FROM lecturas lec
            JOIN libros lib ON lec.libro_id = lib.id
            WHERE lec.favorito=1
            ORDER BY lib.titulo
        """;
        return ejecutarConsulta(sql, null);
    }

    private List<Lectura> ejecutarConsulta(String sql, Object param) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(sql)) {

            if (param != null) sentencia.setObject(1, param);

            List<Lectura> lecturas = new ArrayList<>();
            ResultSet rs = sentencia.executeQuery();

            while (rs.next()) {
                lecturas.add(mapResultSet(rs));
            }
            return lecturas;

        } catch (Exception e) {
            System.err.println("Error listando lecturas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private Lectura mapResultSet(ResultSet rs) throws Exception {
        Lectura l = new Lectura(
                rs.getInt("id"),
                rs.getInt("libro_id"),
                rs.getString("estado"),
                rs.getInt("favorito") == 1,
                rs.getInt("pagina_actual"),
                rs.getInt("total_paginas"),
                rs.getString("fecha_inicio"),
                rs.getString("fecha_fin"),
                rs.getObject("puntuacion") != null ? rs.getDouble("puntuacion") : null
        );
        l.setLibroTitulo(rs.getString("libro_titulo"));
        return l;
    }

    @Override
    public boolean update(Lectura lectura) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "UPDATE lecturas SET estado=?, favorito=?, pagina_actual=?, total_paginas=?, fecha_inicio=?, fecha_fin=?, puntuacion=? WHERE id=?")) {

            sentencia.setString(1, lectura.getEstado());
            sentencia.setInt(2, lectura.isFavorito() ? 1 : 0);
            sentencia.setInt(3, lectura.getPaginaActual());
            sentencia.setInt(4, lectura.getTotalPaginas());
            sentencia.setString(5, lectura.getFechaInicio());
            sentencia.setString(6, lectura.getFechaFin());
            sentencia.setObject(7, lectura.getPuntuacion());
            sentencia.setInt(8, lectura.getId());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error actualizando lectura: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "DELETE FROM lecturas WHERE id=?")) {

            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error eliminando lectura: " + e.getMessage());
            return false;
        }
    }
}
