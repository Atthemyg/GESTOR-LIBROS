package gestor.libros.app.repository.interfaces;

import java.util.List;
import gestor.libros.app.model.Autor;

public interface IAutorRepository {

    /** Crea un autor con ID automático */
    boolean create(Autor autor);

    /** Busca un autor por su ID */
    Autor findById(Integer id);

    /** Devuelve todos los autores */
    List<Autor> findAll();

    /** Devuelve solo los autores marcados como favoritos */
    List<Autor> findFavoritos();

    /** Actualiza los datos de un autor */
    boolean update(Autor autor);

    /** Elimina un autor por su ID */
    boolean deleteById(Integer id);
}
