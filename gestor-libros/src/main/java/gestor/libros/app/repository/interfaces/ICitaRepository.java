package gestor.libros.app.repository.interfaces;

import java.util.List;
import gestor.libros.app.model.Cita;

public interface ICitaRepository {

    /** Crea una cita */
    boolean create(Cita cita);

    /** Busca una cita por su ID */
    Cita findById(Integer id);

    /** Devuelve todas las citas de una lectura */
    List<Cita> findByLecturaId(Integer lecturaId);

    /** Actualiza el texto de una cita */
    boolean update(Cita cita);

    /** Elimina una cita por su ID */
    boolean deleteById(Integer id);
}
