package mx.itson.campus;

public class Item {


        private String foto;
        private String dia;
        private String hora;
        private String lugar;
        private int id;

    public Item() {
    }

    public Item(String foto, String dia, String hora, String lugar, int id) {
        this.foto = foto;
        this.dia = dia;
        this.hora = hora;
        this.lugar = lugar;
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
