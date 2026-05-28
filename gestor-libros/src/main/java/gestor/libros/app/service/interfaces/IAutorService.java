package gestor.libros.app.service.interfaces;

import java.util.List;
import gestor.libros.app.model.Autor;

public interface IAutorService {
    boolean create(Autor autor);
    Autor findById(Integer id);
    List<Autor> findAll();
    List<Autor> findFavoritos();
    boolean update(Autor autor);
    boolean deleteById(Integer id);
}
