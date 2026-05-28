package gestor.libros.app.service.interfaces;

import java.util.List;
import gestor.libros.app.model.Cita;

public interface ICitaService {
    boolean create(Cita cita);
    Cita findById(Integer id);
    List<Cita> findByLecturaId(Integer lecturaId);
    boolean update(Cita cita);
    boolean deleteById(Integer id);
}
