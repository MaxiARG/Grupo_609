package com.example.servicios;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Personaje_pojo {

    @SerializedName("birth_year")
    private String birthYear;

    @SerializedName("eye_color")
    private String eyeColor;

    @SerializedName("films")
    private List<String> films;

    @SerializedName("name")
    private String name;

    @SerializedName("height")
    private String height;

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public List<String> getFilms() {
        return films;
    }

    public void setFilms(List<String> films) {
        this.films = films;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
