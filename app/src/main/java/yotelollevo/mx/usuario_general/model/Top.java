package yotelollevo.mx.usuario_general.model;

public class Top {

    String top, resumen, descripcion;

    public Top(String top, String resumen, String descripcion) {
        this.top = top;
        this.resumen = resumen;
        this.descripcion = descripcion;
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
}
