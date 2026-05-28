package gestor.libros.app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import gestor.libros.app.model.Cita;
import gestor.libros.app.repository.interfaces.ICitaRepository;
import gestor.libros.database.sqlite.SQLiteConnectionManager;

public class CitaRepository extends SQLiteConnectionManager implements ICitaRepository {

    public CitaRepository() {
        super(rutaDb);
    }

    @Override
    public boolean create(Cita cita) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "INSERT INTO citas (lectura_id, texto, pagina, fecha) VALUES (?, ?, ?, ?)")) {

            sentencia.setInt(1, cita.getLecturaId());
            sentencia.setString(2, cita.getTexto());
            sentencia.setObject(3, cita.getPagina());
            sentencia.setString(4, cita.getFecha());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error creando cita: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cita findById(Integer id) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "SELECT * FROM citas WHERE id=?")) {

            sentencia.setInt(1, id);
            ResultSet rs = sentencia.executeQuery();

            if (!rs.next()) return null;
            return mapResultSet(rs);

        } catch (Exception e) {
            System.err.println("Error buscando cita: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Cita> findByLecturaId(Integer lecturaId) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "SELECT * FROM citas WHERE lectura_id=? ORDER BY fecha DESC")) {

            sentencia.setInt(1, lecturaId);
            List<Cita> citas = new ArrayList<>();
            ResultSet rs = sentencia.executeQuery();

            while (rs.next()) citas.add(mapResultSet(rs));
            return citas;

        } catch (Exception e) {
            System.err.println("Error listando citas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private Cita mapResultSet(ResultSet rs) throws Exception {
        return new Cita(
                rs.getInt("id"),
                rs.getInt("lectura_id"),
                rs.getString("texto"),
                rs.getObject("pagina") != null ? rs.getInt("pagina") : null,
                rs.getString("fecha")
        );
    }

    @Override
    public boolean update(Cita cita) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "UPDATE citas SET texto=?, pagina=?, fecha=? WHERE id=?")) {

            sentencia.setString(1, cita.getTexto());
            sentencia.setObject(2, cita.getPagina());
            sentencia.setString(3, cita.getFecha());
            sentencia.setInt(4, cita.getId());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error actualizando cita: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection connection = getConnection();
             PreparedStatement sentencia = connection.prepareStatement(
                     "DELETE FROM citas WHERE id=?")) {

            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error eliminando cita: " + e.getMessage());
            return false;
        }
    }
}
