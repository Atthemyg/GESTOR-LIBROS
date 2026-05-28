package gestor.libros.app.repository.interfaces;

import java.util.List;
import gestor.libros.app.model.Libro;

public interface ILibroRepository {

    /** Crea un libro con ID automático */
    boolean create(Libro libro);

    /** Busca un libro por su ID */
    Libro findById(Integer id);

    /** Devuelve todos los libros */
    List<Libro> findAll();

    /** Busca libros cuyo título contenga el texto dado */
    List<Libro> findByTitulo(String titulo);

    /** Devuelve los libros de un autor concreto */
    List<Libro> findByAutorId(Integer autorId);

    /** Actualiza los datos de un libro */
    boolean update(Libro libro);

    /** Elimina un libro por su ID */
    boolean deleteById(Integer id);
}
