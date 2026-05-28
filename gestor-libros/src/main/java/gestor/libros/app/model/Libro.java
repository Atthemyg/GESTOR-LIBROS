package gestor.libros.app.model;

import java.util.Objects;

public class Libro {

    private Integer id;
    private String titulo;
    private Integer autorId;
    private String autorNombre; // campo de apoyo para mostrar en vistas
    private String isbn;
    private Integer anioPublicacion;
    private String genero;
    private String sinopsis;
    private String portadaUrl;

    public Libro(String titulo, Integer autorId, String isbn, Integer anioPublicacion,
                 String genero, String sinopsis, String portadaUrl) {
        this.titulo = titulo;
        this.autorId = autorId;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.genero = genero;
        this.sinopsis = sinopsis;
        this.portadaUrl = portadaUrl;
    }

    public Libro(int id, String titulo, Integer autorId, String isbn, Integer anioPublicacion,
                 String genero, String sinopsis, String portadaUrl) {
        this.id = id;
        this.titulo = titulo;
        this.autorId = autorId;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.genero = genero;
        this.sinopsis = sinopsis;
        this.portadaUrl = portadaUrl;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Integer getAutorId() { return autorId; }
    public void setAutorId(Integer autorId) { this.autorId = autorId; }

    public String getAutorNombre() { return autorNombre; }
    public void setAutorNombre(String autorNombre) { this.autorNombre = autorNombre; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(Integer anioPublicacion) { this.anioPublicacion = anioPublicacion; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    public String getPortadaUrl() { return portadaUrl; }
    public void setPortadaUrl(String portadaUrl) { this.portadaUrl = portadaUrl; }

    @Override
    public String toString() { return titulo; }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Libro other = (Libro) obj;
        return Objects.equals(id, other.id);
    }
}
