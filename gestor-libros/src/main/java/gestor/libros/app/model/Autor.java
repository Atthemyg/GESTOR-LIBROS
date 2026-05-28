package gestor.libros.app.model;

import java.util.Objects;

public class Autor {

    private Integer id;
    private String nombre;
    private String nacionalidad;
    private String biografia;
    private boolean favorito;
    private String fotoUrl;

    public Autor(String nombre, String nacionalidad, String biografia, boolean favorito) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.biografia = biografia;
        this.favorito = favorito;
    }

    public Autor(int id, String nombre, String nacionalidad, String biografia, boolean favorito) {
        this.id = id;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.biografia = biografia;
        this.favorito = favorito;
    }

    public Autor(int id, String nombre, String nacionalidad, String biografia, boolean favorito, String fotoUrl) {
        this.id = id;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.biografia = biografia;
        this.favorito = favorito;
        this.fotoUrl = fotoUrl;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public boolean isFavorito() { return favorito; }
    public void setFavorito(boolean favorito) { this.favorito = favorito; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    @Override
    public String toString() { return nombre; }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Autor other = (Autor) obj;
        return Objects.equals(id, other.id);
    }
}
