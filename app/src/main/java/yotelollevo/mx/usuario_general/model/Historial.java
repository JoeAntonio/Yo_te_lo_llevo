package yotelollevo.mx.usuario_general.model;

public class Historial {

    String nombres, fecha, detalle, ordenSer, servicio;

    public Historial(String nombres, String fecha, String detalle, String ordenSer, String servicio) {
        this.nombres = nombres;
        this.fecha = fecha;
        this.detalle = detalle;
        this.ordenSer = ordenSer;
        this.servicio = servicio;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getOrdenSer() {
        return ordenSer;
    }

    public void setOrdenSer(String ordenSer) {
        this.ordenSer = ordenSer;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
}