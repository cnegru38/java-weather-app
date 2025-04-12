package org.example;

import java.io.Serializable;

public class WeatherManager implements Serializable {
    private String username;
    private String command;
    private String location;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}