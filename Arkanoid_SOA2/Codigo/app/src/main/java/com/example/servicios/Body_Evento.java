package com.example.servicios;

import com.google.gson.annotations.SerializedName;

public class Body_Evento {


    @SerializedName("env")
    String env;
    @SerializedName("type_events")
    String type_events;
    @SerializedName("state")
    String state;
    @SerializedName("description")
    String description;




    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getType_events() {
        return type_events;
    }

    public void setType_events(String type_events) {
        this.type_events = type_events;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
