package com.example.servicios;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("count")
    String count;
    @SerializedName("next")
    String next;
    @SerializedName("previous")
    String previous;
    @SerializedName("results")
    List<Personaje_pojo> personajes;

    public Personaje_pojo getPersonaje() {
        return personajes.get(0);
    }

    public void setPersonaje(List<Personaje_pojo> personajes) {
        this.personajes = personajes;
    }
}
