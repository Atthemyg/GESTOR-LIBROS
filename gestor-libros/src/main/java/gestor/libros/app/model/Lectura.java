package gestor.libros.app.model;

import java.util.Objects;

/**
 * Representa el estado de lectura de un libro concreto.
 * Estado puede ser: PENDIENTE, LEYENDO, LEIDO, ABANDONADO
 */
public class Lectura {

    private Integer id;
    private Integer libroId;
    private String libroTitulo; // campo de apoyo para vistas
    private String estado;
    private boolean favorito;
    private int paginaActual;
    private int totalPaginas;
    private String fechaInicio;
    private String fechaFin;
    private Double puntuacion;

    public Lectura(Integer libroId, String estado, boolean favorito,
                   int paginaActual, int totalPaginas, String fechaInicio,
                   String fechaFin, Double puntuacion) {
        this.libroId = libroId;
        this.estado = estado;
        this.favorito = favorito;
        this.paginaActual = paginaActual;
        this.totalPaginas = totalPaginas;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.puntuacion = puntuacion;
    }

    public Lectura(int id, Integer libroId, String estado, boolean favorito,
                   int paginaActual, int totalPaginas, String fechaInicio,
                   String fechaFin, Double puntuacion) {
        this.id = id;
        this.libroId = libroId;
        this.estado = estado;
        this.favorito = favorito;
        this.paginaActual = paginaActual;
        this.totalPaginas = totalPaginas;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.puntuacion = puntuacion;
    }

    /** Porcentaje de progreso (0-100) */
    public double getPorcentajeProgreso() {
        if (totalPaginas == 0) return 0;
        return (paginaActual * 100.0) / totalPaginas;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getLibroId() { return libroId; }
    public void setLibroId(Integer libroId) { this.libroId = libroId; }

    public String getLibroTitulo() { return libroTitulo; }
    public void setLibroTitulo(String libroTitulo) { this.libroTitulo = libroTitulo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isFavorito() { return favorito; }
    public void setFavorito(boolean favorito) { this.favorito = favorito; }

    public int getPaginaActual() { return paginaActual; }
    public void setPaginaActual(int paginaActual) { this.paginaActual = paginaActual; }

    public int getTotalPaginas() { return totalPaginas; }
    public void setTotalPaginas(int totalPaginas) { this.totalPaginas = totalPaginas; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public Double getPuntuacion() { return puntuacion; }
    public void setPuntuacion(Double puntuacion) { this.puntuacion = puntuacion; }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Lectura other = (Lectura) obj;
        return Objects.equals(id, other.id);
    }
}
