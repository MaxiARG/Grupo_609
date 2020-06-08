package com.example.servicios;

import com.google.gson.annotations.SerializedName;

public class Evento {
    @SerializedName("type_events")
    String type_events;
    @SerializedName("state")
    String state;
    @SerializedName("description")
    String description;
    @SerializedName("group")
    String group;

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
