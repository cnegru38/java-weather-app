package org.example;

import java.io.Serializable;
import java.util.List;

public class WeatherData implements Serializable {
    private static final long serialVersionUID = 1L; // versiunea serializării

    private String location;
    private String currentWeather;
    private int temperature;
    private List<Forecast> forecast;

    public WeatherData(String location, String currentWeather, int temperature, List<Forecast> forecast) {
        this.location = location;
        this.currentWeather = currentWeather;
        this.temperature = temperature;
        this.forecast = forecast;
    }

    // Getters și setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(String currentWeather) {
        this.currentWeather = currentWeather;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }

    // Clasă internă pentru prognoză
    public static class Forecast implements Serializable {
        private static final long serialVersionUID = 1L;

        private String day;
        private String weather;
        private int temperature;

        public Forecast(String day, String weather, int temperature) {
            this.day = day;
            this.weather = weather;
            this.temperature = temperature;
        }

        // Getters și setters
        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }
    }
}
