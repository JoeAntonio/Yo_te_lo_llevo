package yotelollevo.mx.usuario_general.model;

public class Top {

    String top, resumen, descripcion, vector, nombre, fondo;

    public Top(String top, String resumen, String descripcion, String vector, String nombre, String fondo) {
        this.top = top;
        this.resumen = resumen;
        this.descripcion = descripcion;
        this.vector = vector;
        this.nombre = nombre;
        this.fondo = fondo;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFondo() {
        return fondo;
    }

    public void setFondo(String fondo) {
        this.fondo = fondo;
    }
}
