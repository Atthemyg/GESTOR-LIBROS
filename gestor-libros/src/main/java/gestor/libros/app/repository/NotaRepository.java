package gestor.libros.app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import gestor.libros.app.model.Nota;
import gestor.libros.app.repository.interfaces.INotaRepository;
import gestor.libros.database.sqlite.SQLiteConnectionManager;

public class NotaRepository extends SQLiteConnectionManager implements INotaRepository {

    public NotaRepository() {
        super(rutaDb);
    }

    @Override
    public boolean create(Nota nota) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "INSERT INTO notas (lectura_id, contenido, pagina, fecha) VALUES (?, ?, ?, ?)")) {

            sentencia.setInt(1, nota.getLecturaId());
            sentencia.setString(2, nota.getContenido());
            sentencia.setObject(3, nota.getPagina());
            sentencia.setString(4, nota.getFecha());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error creando nota: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Nota findById(Integer id) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "SELECT * FROM notas WHERE id=?")) {

            sentencia.setInt(1, id);
            ResultSet rs = sentencia.executeQuery();

            if (!rs.next()) return null;
            return mapResultSet(rs);

        } catch (Exception e) {
            System.err.println("Error buscando nota: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Nota> findByLecturaId(Integer lecturaId) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "SELECT * FROM notas WHERE lectura_id=? ORDER BY fecha DESC")) {

            sentencia.setInt(1, lecturaId);
            List<Nota> notas = new ArrayList<>();
            ResultSet rs = sentencia.executeQuery();

            while (rs.next()) notas.add(mapResultSet(rs));
            return notas;

        } catch (Exception e) {
            System.err.println("Error listando notas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private Nota mapResultSet(ResultSet rs) throws Exception {
        return new Nota(
                rs.getInt("id"),
                rs.getInt("lectura_id"),
                rs.getString("contenido"),
                rs.getObject("pagina") != null ? rs.getInt("pagina") : null,
                rs.getString("fecha")
        );
    }

    @Override
    public boolean update(Nota nota) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "UPDATE notas SET contenido=?, pagina=?, fecha=? WHERE id=?")) {

            sentencia.setString(1, nota.getContenido());
            sentencia.setObject(2, nota.getPagina());
            sentencia.setString(3, nota.getFecha());
            sentencia.setInt(4, nota.getId());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error actualizando nota: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "DELETE FROM notas WHERE id=?")) {

            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error eliminando nota: " + e.getMessage());
            return false;
        }
    }
}
