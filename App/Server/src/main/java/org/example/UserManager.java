package org.example;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class UserManager {
    private Map<String, String> userLocations = new HashMap<>();

    // Setează locația pentru un utilizator
    public void setLocation(String username, String location) {
        userLocations.put(username, location);
    }

    // Obține locația unui utilizator
    public String getLocation(String username) {
        return userLocations.get(username);
    }

    // Verifică dacă utilizatorul are o locație setată
    public boolean hasLocation(String username) {
        return userLocations.containsKey(username);
    }

    // Funcționalități pentru salvarea/încărcarea locațiilor utilizatorilor într-un/dintr-un fișier
    public void saveToFile(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(userLocations);
        }
    }

    public void loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            userLocations = (Map<String, String>) ois.readObject();
        }
    }
}
