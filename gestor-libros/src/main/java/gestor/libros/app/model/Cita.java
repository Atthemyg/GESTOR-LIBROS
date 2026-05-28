package gestor.libros.app.model;

import java.util.Objects;

public class Cita {

    private Integer id;
    private Integer lecturaId;
    private String texto;
    private Integer pagina;
    private String fecha;

    public Cita(Integer lecturaId, String texto, Integer pagina, String fecha) {
        this.lecturaId = lecturaId;
        this.texto = texto;
        this.pagina = pagina;
        this.fecha = fecha;
    }

    public Cita(int id, Integer lecturaId, String texto, Integer pagina, String fecha) {
        this.id = id;
        this.lecturaId = lecturaId;
        this.texto = texto;
        this.pagina = pagina;
        this.fecha = fecha;
    }

    public Integer getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getLecturaId() { return lecturaId; }
    public void setLecturaId(Integer lecturaId) { this.lecturaId = lecturaId; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

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
        Cita other = (Cita) obj;
        return Objects.equals(id, other.id);
    }
}
