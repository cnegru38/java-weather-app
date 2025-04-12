package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;

public class WeatherServer {
    private final int PORT = 6543;
    private DataManager dataManager = new DataManager();
    private UserManager locationManager = new UserManager();

    public void start() {

        String dataFilePath = Paths.get("src", "main", "java", "org", "example", "data.json").toAbsolutePath().toString();
        this.dataManager.loadFromJson(dataFilePath);

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Serverul rulează...");
            Socket socket = new Socket();

            while(true) {
                Socket received = serverSocket.accept();
                new ClientThread(received, dataManager, locationManager).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}