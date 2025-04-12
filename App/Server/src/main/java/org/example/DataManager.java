package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private Map<String, WeatherData> weatherData = new HashMap<>();

    // Încarcă datele din JSON
    public void loadFromJson(String jsonFilePath) {
        try {
            // Citește fișierul JSON ca un string
            String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            JSONArray jsonArray = new JSONArray(content);

            // Iterează array-ul și adaugă datele în WeatherData
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extragere locație, vreme, temperatură
                String location = jsonObject.getString("location");
                String currentWeather = jsonObject.getString("currentWeather");
                int currentTemperature = jsonObject.getInt("currentTemperature");

                JSONArray forecastArray = jsonObject.getJSONArray("forecast");
                List<WeatherData.Forecast> forecastList = new ArrayList<>();

                for (int j = 0; j < forecastArray.length(); j++) {
                    JSONObject forecastObj = forecastArray.getJSONObject(j);
                    String day = forecastObj.getString("day");
                    String weather = forecastObj.getString("weather");
                    int temperature = forecastObj.getInt("temperature");

                    // Adaugă obiectul Forecast în listă
                    forecastList.add(new WeatherData.Forecast(day, weather, temperature));
                }

                // Adaugă obiectul WeatherData în map
                WeatherData data = new WeatherData(location, currentWeather, currentTemperature, forecastList);
                weatherData.put(location, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Obține informațiile despre vreme pentru o locație
    public WeatherData getWeatherData(String location) {
        return weatherData.get(location);
    }

    // Verifică dacă există informații despre o locație
    public boolean hasLocation(String location) {
        return weatherData.containsKey(location);
    }
}
