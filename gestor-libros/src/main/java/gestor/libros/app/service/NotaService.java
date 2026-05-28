package gestor.libros.app.service;

import java.util.List;
import gestor.libros.app.model.Nota;
import gestor.libros.app.repository.NotaRepository;
import gestor.libros.app.repository.interfaces.INotaRepository;
import gestor.libros.app.service.interfaces.INotaService;

public class NotaService implements INotaService {

    private final INotaRepository repository;

    public NotaService() {
        this.repository = new NotaRepository();
    }

    @Override
    public boolean create(Nota nota) {
        if (nota.getContenido() == null || nota.getContenido().isBlank()) return false;
        return repository.create(nota);
    }

    @Override
    public Nota findById(Integer id) {
        if (id == null) return null;
        return repository.findById(id);
    }

    @Override
    public List<Nota> findByLecturaId(Integer lecturaId) {
        if (lecturaId == null) return new java.util.ArrayList<>();
        return repository.findByLecturaId(lecturaId);
    }

    @Override
    public boolean update(Nota nota) {
        return repository.update(nota);
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) return false;
        return repository.deleteById(id);
    }
}
