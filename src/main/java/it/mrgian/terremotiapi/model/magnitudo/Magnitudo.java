package it.mrgian.terremotiapi.model.magnitudo;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class Magnitudo {
    @JsonPropertyDescription("Valore della magnitudo")
    private float valore;

    @JsonPropertyDescription("Tipo della magnitudo ")
    private TipoMagnitudo tipo;

    public Magnitudo(float valore, TipoMagnitudo tipo) {
        this.valore = valore;
        this.tipo = tipo;
    }

    public TipoMagnitudo getTipo() {
        return tipo;
    }

    public float getValore() {
        return valore;
    }
}