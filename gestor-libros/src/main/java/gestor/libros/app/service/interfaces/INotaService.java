package gestor.libros.app.service.interfaces;

import java.util.List;
import gestor.libros.app.model.Nota;

public interface INotaService {
    boolean create(Nota nota);
    Nota findById(Integer id);
    List<Nota> findByLecturaId(Integer lecturaId);
    boolean update(Nota nota);
    boolean deleteById(Integer id);
}
