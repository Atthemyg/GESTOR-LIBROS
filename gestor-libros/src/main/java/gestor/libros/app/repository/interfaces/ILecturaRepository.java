package gestor.libros.app.repository.interfaces;

import java.util.List;
import gestor.libros.app.model.Lectura;

public interface ILecturaRepository {

    /** Crea un registro de lectura */
    boolean create(Lectura lectura);

    /** Busca una lectura por su ID */
    Lectura findById(Integer id);

    /** Devuelve la lectura asociada a un libro concreto */
    Lectura findByLibroId(Integer libroId);

    /** Devuelve todas las lecturas */
    List<Lectura> findAll();

    /** Devuelve lecturas filtradas por estado (PENDIENTE, LEYENDO, LEIDO, ABANDONADO) */
    List<Lectura> findByEstado(String estado);

    /** Devuelve solo los libros marcados como favoritos */
    List<Lectura> findFavoritos();

    /** Actualiza el estado y progreso de una lectura */
    boolean update(Lectura lectura);

    /** Elimina una lectura por su ID */
    boolean deleteById(Integer id);
}
