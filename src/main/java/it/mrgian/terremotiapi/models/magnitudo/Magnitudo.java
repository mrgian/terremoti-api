package it.mrgian.terremotiapi.models.magnitudo;

public class Magnitudo {
    private float valore;
    private TipoMagnitudo tipo;

    public Magnitudo(float valore, TipoMagnitudo tipo) {
        this.setValore(valore);
        this.setTipo(tipo);
    }

    public TipoMagnitudo getTipo() {
        return tipo;
    }

    public void setTipo(TipoMagnitudo tipo) {
        this.tipo = tipo;
    }

    public float getValore() {
        return valore;
    }

    public void setValore(float valore) {
        this.valore = valore;
    }
}