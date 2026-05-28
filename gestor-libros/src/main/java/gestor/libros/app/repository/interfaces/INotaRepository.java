package gestor.libros.app.repository.interfaces;

import java.util.List;
import gestor.libros.app.model.Nota;

public interface INotaRepository {

    /** Crea una nota */
    boolean create(Nota nota);

    /** Busca una nota por su ID */
    Nota findById(Integer id);

    /** Devuelve todas las notas de una lectura */
    List<Nota> findByLecturaId(Integer lecturaId);

    /** Actualiza el contenido de una nota */
    boolean update(Nota nota);

    /** Elimina una nota por su ID */
    boolean deleteById(Integer id);
}
