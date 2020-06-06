package com.example.servicios;

import com.google.gson.annotations.SerializedName;

public class Respuesta_Webservice {

    @SerializedName("state")
    String state;
    @SerializedName("env")
    String env;
    @SerializedName("token")
    String token;
    @SerializedName("msg")
    String msg;


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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
