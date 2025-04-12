package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientThread extends Thread {
    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private DataManager dataManager;
    private UserManager userManager;
    private Connection connection;

    public ClientThread(Socket client, DataManager dataManager, UserManager userManager) {
        this.client = client;
        this.dataManager = dataManager;
        this.userManager = userManager;

        try {
            this.in = new ObjectInputStream(client.getInputStream());
            this.out = new ObjectOutputStream(client.getOutputStream());
            this.connection = establishDatabaseConnection(); // Se crează conexiunea cu baza da date
            createTableIfNotExists();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection establishDatabaseConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:users.db");
    }

    private void createTableIfNotExists() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS user_locations (" +
                "username TEXT PRIMARY KEY," +
                "location TEXT NOT NULL);";
        try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
            stmt.executeUpdate();
        }
    }

    @Override
    public void run() {
        try {
            // Citește numele utilizatorului
            String username = (String) in.readObject();

            // Bucla principală pentru gestionarea comenzilor clientului
            while (true) {
                String command = (String) in.readObject(); // Comanda clientului (GET/SET/EXIT)

                if ("GET".equalsIgnoreCase(command)) {
                    handleGetWeather(username);
                } else if ("SET".equalsIgnoreCase(command)) {
                    handleSetLocation(username);
                } else if ("EXIT".equalsIgnoreCase(command)) {
                    out.writeObject("Deconectare...");
                    break;
                } else {
                    out.writeObject("Comandă necunoscută. Folosiți GET, SET sau EXIT.");
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.err.println("Eroare în comunicarea cu clientul: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close(); // Închide conexiunea cu baza de date
                }
                client.close();
            } catch (IOException | SQLException e) {
                System.err.println("Eroare la închiderea conexiunii clientului: " + e.getMessage());
            }
        }
    }

    private void handleGetWeather(String username) throws IOException {
        // Obține locația utilizatorului
        String location = userManager.getLocation(username);

        if (location == null) {
            out.writeObject("Nu ai setat o locație. Folosește comanda SET pentru a seta locația.");
            return;
        }

        // Obține informațiile despre vreme pentru locația utilizatorului
        WeatherData data = dataManager.getWeatherData(location);

        if (data != null) {
            // Trimite informațiile despre vreme și prognoză către client
            StringBuilder responseBuilder = new StringBuilder();
            responseBuilder.append("Vremea în ").append(location).append(": ")
                    .append(data.getCurrentWeather()).append(", ")
                    .append(data.getTemperature()).append("°C.\n");

            responseBuilder.append("Prognoza:\n");
            for (WeatherData.Forecast forecast : data.getForecast()) {
                responseBuilder.append(forecast.getDay()).append(": ")
                        .append(forecast.getWeather()).append(", ")
                        .append(forecast.getTemperature()).append("°C\n");
            }

            out.writeObject(responseBuilder.toString());
        } else {
            out.writeObject("Nu există informații despre vreme pentru locația " + location + ".");
        }
    }

    private void handleSetLocation(String username) throws IOException, ClassNotFoundException, SQLException {
        // Citește locația dorită de utilizator
        String location = (String) in.readObject();

        if (dataManager.hasLocation(location)) {
            // Setează locația pentru utilizator
            userManager.setLocation(username, location);
            storeUserLocation(username, location); // Stochează datele despre utilizator în baza de date
            out.writeObject("Locația a fost setată la: " + location + ".");
        } else {
            out.writeObject("Locația " + location + " nu există în baza de date.");
        }
    }

    private void storeUserLocation(String username, String location) throws SQLException {
        String insertOrUpdateSQL = "INSERT INTO user_locations (username, location) VALUES (?, ?) " +
                "ON CONFLICT(username) DO UPDATE SET location = excluded.location;";
        try (PreparedStatement stmt = connection.prepareStatement(insertOrUpdateSQL)) {
            stmt.setString(1, username);
            stmt.setString(2, location);
            stmt.executeUpdate();
        }
    }
}
