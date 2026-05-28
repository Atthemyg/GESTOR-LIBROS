package gestor.libros.app.service;

import java.util.List;
import gestor.libros.app.model.Autor;
import gestor.libros.app.repository.AutorRepository;
import gestor.libros.app.repository.interfaces.IAutorRepository;
import gestor.libros.app.service.interfaces.IAutorService;

public class AutorService implements IAutorService {

    private final IAutorRepository repository;

    public AutorService() {
        this.repository = new AutorRepository();
    }

    @Override
    public boolean create(Autor autor) {
        if (autor.getNombre() == null || autor.getNombre().isBlank()) return false;
        return repository.create(autor);
    }

    @Override
    public Autor findById(Integer id) {
        if (id == null) return null;
        return repository.findById(id);
    }

    @Override
    public List<Autor> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Autor> findFavoritos() {
        return repository.findFavoritos();
    }

    @Override
    public boolean update(Autor autor) {
        return repository.update(autor);
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) return false;
        return repository.deleteById(id);
    }
}
