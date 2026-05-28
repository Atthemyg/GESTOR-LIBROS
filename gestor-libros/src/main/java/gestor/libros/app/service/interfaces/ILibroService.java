package gestor.libros.app.service.interfaces;

import java.util.List;
import gestor.libros.app.model.Libro;

public interface ILibroService {
    boolean create(Libro libro);
    Libro findById(Integer id);
    List<Libro> findAll();
    List<Libro> findByTitulo(String titulo);
    List<Libro> findByAutorId(Integer autorId);
    boolean update(Libro libro);
    boolean deleteById(Integer id);
}
