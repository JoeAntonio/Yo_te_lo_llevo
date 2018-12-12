package yotelollevo.mx.usuario_general.model;

public class Picture {

    String imagen, titulo, subtitulo;

    public Picture(String imagen, String titulo, String subtitulo) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }
}
