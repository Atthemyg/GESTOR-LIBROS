package gestor.libros.app.model;

import java.util.Objects;

public class Nota {

    private Integer id;
    private Integer lecturaId;
    private String contenido;
    private Integer pagina;
    private String fecha;

    public Nota(Integer lecturaId, String contenido, Integer pagina, String fecha) {
        this.lecturaId = lecturaId;
        this.contenido = contenido;
        this.pagina = pagina;
        this.fecha = fecha;
    }

    public Nota(int id, Integer lecturaId, String contenido, Integer pagina, String fecha) {
        this.id = id;
        this.lecturaId = lecturaId;
        this.contenido = contenido;
        this.pagina = pagina;
        this.fecha = fecha;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getLecturaId() { return lecturaId; }
    public void setLecturaId(Integer lecturaId) { this.lecturaId = lecturaId; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Integer getPagina() { return pagina; }
    public void setPagina(Integer pagina) { this.pagina = pagina; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Nota other = (Nota) obj;
        return Objects.equals(id, other.id);
    }
}
