package yotelollevo.mx.usuario_general.model;

public class Top {

    String top, resumen, descripcion, vector;

    public Top(String top, String resumen, String descripcion, String vector) {
        this.top = top;
        this.resumen = resumen;
        this.descripcion = descripcion;
        this.vector = vector;
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
}
