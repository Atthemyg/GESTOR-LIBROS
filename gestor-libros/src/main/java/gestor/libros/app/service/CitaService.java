package gestor.libros.app.service;

import java.util.List;
import gestor.libros.app.model.Cita;
import gestor.libros.app.repository.CitaRepository;
import gestor.libros.app.repository.interfaces.ICitaRepository;
import gestor.libros.app.service.interfaces.ICitaService;

public class CitaService implements ICitaService {

    private final ICitaRepository repository;

    public CitaService() {
        this.repository = new CitaRepository();
    }

    @Override
    public boolean create(Cita cita) {
        if (cita.getTexto() == null || cita.getTexto().isBlank()) return false;
        return repository.create(cita);
    }

    @Override
    public Cita findById(Integer id) {
        if (id == null) return null;
        return repository.findById(id);
    }

    @Override
    public List<Cita> findByLecturaId(Integer lecturaId) {
        if (lecturaId == null) return new java.util.ArrayList<>();
        return repository.findByLecturaId(lecturaId);
    }

    @Override
    public boolean update(Cita cita) {
        return repository.update(cita);
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) return false;
        return repository.deleteById(id);
    }
}
