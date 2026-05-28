package gestor.libros.app.service.interfaces;

import java.util.List;
import gestor.libros.app.model.Lectura;

public interface ILecturaService {
    boolean create(Lectura lectura);
    Lectura findById(Integer id);
    Lectura findByLibroId(Integer libroId);
    List<Lectura> findAll();
    List<Lectura> findLeidos();
    List<Lectura> findEnProgreso();
    List<Lectura> findFavoritos();
    boolean update(Lectura lectura);
    boolean actualizarProgreso(Integer id, int paginaActual);
    boolean marcarComoLeido(Integer id);
    boolean toggleFavorito(Integer id);
    boolean deleteById(Integer id);
}
