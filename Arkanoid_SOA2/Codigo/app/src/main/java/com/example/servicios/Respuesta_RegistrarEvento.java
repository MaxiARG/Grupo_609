package com.example.servicios;

import com.google.gson.annotations.SerializedName;

public class Respuesta_RegistrarEvento {
    @SerializedName("state")
    String state;
    @SerializedName("env")
    String env;
    @SerializedName("msg")
    String msg;
    @SerializedName("event")
    Evento event;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Evento getEvent() {
        return event;
    }

    public void setEvent(Evento event) {
        this.event = event;
    }
}
