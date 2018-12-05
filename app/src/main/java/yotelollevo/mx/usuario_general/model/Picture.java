package yotelollevo.mx.usuario_general.model;

public class Picture {

    private int imagen;
    private String titulo, subtitulo;

    public Picture(int imagen, String titulo, String subtitulo) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
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
