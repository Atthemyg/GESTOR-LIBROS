package gestor.libros.app.service;

import java.util.List;
import gestor.libros.app.model.Libro;
import gestor.libros.app.repository.LibroRepository;
import gestor.libros.app.repository.interfaces.ILibroRepository;
import gestor.libros.app.service.interfaces.ILibroService;

public class LibroService implements ILibroService {

    private final ILibroRepository repository;

    public LibroService() {
        this.repository = new LibroRepository();
    }

    @Override
    public boolean create(Libro libro) {
        if (libro.getTitulo() == null || libro.getTitulo().isBlank()) return false;
        return repository.create(libro);
    }

    @Override
    public Libro findById(Integer id) {
        if (id == null) return null;
        return repository.findById(id);
    }

    @Override
    public List<Libro> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Libro> findByTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) return findAll();
        return repository.findByTitulo(titulo);
    }

    @Override
    public List<Libro> findByAutorId(Integer autorId) {
        if (autorId == null) return new java.util.ArrayList<>();
        return repository.findByAutorId(autorId);
    }

    @Override
    public boolean update(Libro libro) {
        return repository.update(libro);
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) return false;
        return repository.deleteById(id);
    }
}
