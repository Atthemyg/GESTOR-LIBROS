package gestor.libros.app.service;

import java.time.LocalDate;
import java.util.List;

import gestor.libros.app.model.Lectura;
import gestor.libros.app.repository.LecturaRepository;
import gestor.libros.app.repository.interfaces.ILecturaRepository;
import gestor.libros.app.service.interfaces.ILecturaService;

public class LecturaService implements ILecturaService {

    private final ILecturaRepository repository;

    public LecturaService() {
        this.repository = new LecturaRepository();
    }

    @Override
    public boolean create(Lectura lectura) {
        if (lectura.getLibroId() == null) return false;
        return repository.create(lectura);
    }

    @Override
    public Lectura findById(Integer id) {
        if (id == null) return null;
        return repository.findById(id);
    }

    @Override
    public Lectura findByLibroId(Integer libroId) {
        if (libroId == null) return null;
        return repository.findByLibroId(libroId);
    }

    @Override
    public List<Lectura> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Lectura> findLeidos() {
        return repository.findByEstado("LEIDO");
    }

    @Override
    public List<Lectura> findEnProgreso() {
        return repository.findByEstado("LEYENDO");
    }

    @Override
    public List<Lectura> findFavoritos() {
        return repository.findFavoritos();
    }

    @Override
    public boolean update(Lectura lectura) {
        return repository.update(lectura);
    }

    /**
     * Actualiza solo la página actual. Si llega a total_paginas, marca como LEIDO.
     */
    @Override
    public boolean actualizarProgreso(Integer id, int paginaActual) {
        Lectura lectura = repository.findById(id);
        if (lectura == null) return false;

        lectura.setPaginaActual(paginaActual);

        if (lectura.getTotalPaginas() > 0 && paginaActual >= lectura.getTotalPaginas()) {
            lectura.setEstado("LEIDO");
            if (lectura.getFechaFin() == null) {
                lectura.setFechaFin(LocalDate.now().toString());
            }
        } else if ("PENDIENTE".equals(lectura.getEstado())) {
            lectura.setEstado("LEYENDO");
            if (lectura.getFechaInicio() == null) {
                lectura.setFechaInicio(LocalDate.now().toString());
            }
        }

        return repository.update(lectura);
    }

    /** Marca el libro directamente como LEIDO */
    @Override
    public boolean marcarComoLeido(Integer id) {
        Lectura lectura = repository.findById(id);
        if (lectura == null) return false;

        lectura.setEstado("LEIDO");
        lectura.setPaginaActual(lectura.getTotalPaginas());
        if (lectura.getFechaFin() == null) {
            lectura.setFechaFin(LocalDate.now().toString());
        }

        return repository.update(lectura);
    }

    /** Activa/desactiva el favorito */
    @Override
    public boolean toggleFavorito(Integer id) {
        Lectura lectura = repository.findById(id);
        if (lectura == null) return false;

        lectura.setFavorito(!lectura.isFavorito());
        return repository.update(lectura);
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) return false;
        return repository.deleteById(id);
    }
}
