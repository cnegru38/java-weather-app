package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void start(){
        try {
            this.socket = new Socket("localhost", 6543);
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            this.in = new ObjectInputStream(this.socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            System.out.println("Conectat la server. Introduceți numele utilizatorului:");
            String username = scanner.nextLine().trim();
            out.writeObject(username); // Send username to server

            System.out.println("Utilizați comenzile: GET, SET, EXIT.");;

            while (true) {
                // Citire comandă de la utilizator
                System.out.print("Introduceți comanda (GET/SET/EXIT): ");
                String command = scanner.nextLine().trim();
                out.writeObject(command); // Trimitere comandă la server

                if ("SET".equalsIgnoreCase(command)) {
                    System.out.print("Introduceți locația: ");
                    String location = scanner.nextLine().trim();
                    out.writeObject(location); // Trimitere locație la server
                }

                // Citirea răspunsului de la server
                Object response = this.in.readObject();
                handleResponse(response);

                if ("EXIT".equalsIgnoreCase(command)) {
                    System.out.println("Deconectare de la server. La revedere!");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare de conexiune: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Eroare la citirea obiectului: " + e.getMessage());
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.println("Eroare la închiderea conexiunii: " + e.getMessage());
            }
        }
    }

    private void handleResponse(Object response) {
        if (response instanceof String) {
            System.out.println("Server: " + response);
        } else if (response instanceof WeatherData) {
            WeatherData data = (WeatherData) response;
            System.out.println("Vremea în " + data.getLocation() + ":");
            System.out.println("  Stare: " + data.getCurrentWeather());
            System.out.println("  Temperatură: " + data.getTemperature() + "°C");

            System.out.println("Prognoză pentru următoarele zile:");
            for (WeatherData.Forecast forecast : data.getForecast()) {
                System.out.println("  Ziua: " + forecast.getDay() +
                        ", Stare: " + forecast.getWeather() +
                        ", Temperatură: " + forecast.getTemperature() + "°C");
            }
        } else {
            System.err.println("Răspuns necunoscut de la server.");
        }
    }
}