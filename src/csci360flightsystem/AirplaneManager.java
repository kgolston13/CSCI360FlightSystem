/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: AirplaneManager.java 
 * Description:  
 * This file has the capability to create, modify, delete, display, or search airplane objects.
*/
package csci360flightsystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class AirplaneManager {

    // variable for file location
    private static final String FILE_LOCATION = "src/csci360flightsystem/Airplanes.txt";

    // Vector list of airplanes
    private Vector<Airplane> airplanes;

    // Static instance for singleton pattern
    private static AirplaneManager instance;

    // Private constructor for the AirplaneManager class
    private AirplaneManager() {
        airplanes = new Vector<>();
        loadAirplanesFromFile(FILE_LOCATION);
    }

    // Methods for the
    // Public method to get the instance of the class
    public static AirplaneManager getInstance() {
        if (instance == null) {
            instance = new AirplaneManager();
        }
        return instance;
    }

    // Method to create a new airplane
    public void createAirplane(Airplane airplane) {
        // Validate the airplane object
        if (airplane.getAirspeed() <= 0 || airplane.getFuelBurn() <= 0 || airplane.getFuelCapacity() <= 0 ||
                airplane.getKey() < 0 || airplane.getMake() == null || airplane.getMake().trim().isEmpty() ||
                airplane.getModel() == null || airplane.getModel().trim().isEmpty() ||
                airplane.getType() == null || airplane.getType().trim().isEmpty()) {
            System.out.println("Invalid airplane data provided.");
            return;
        }

        // Check if the airplane with the same key already exists
        for (Airplane existingAirplane : airplanes) {
            if (existingAirplane.getKey() == airplane.getKey()) {
                System.out.println("Airplane with key " + airplane.getKey() + " already exists.");
                return;
            }
        }

        // Add the airplane to the list
        airplanes.add(airplane);
        saveAirplanesToFile(FILE_LOCATION);
    }

    // Method to delete an airplane
    public void deleteAirplane(Airplane airplane) {
        airplanes.remove(airplane);
        saveAirplanesToFile(FILE_LOCATION);
    }

    // Method to display all airplanes
    public void displayAirplanes() {
        for (Airplane airplane : airplanes) {
            System.out.println(airplane);
        }
    }

    // Method to modify an airplane
    public void modifyAirplane(int key, Airplane newAirplane) {
        for (int i = 0; i < airplanes.size(); i++) {
            Airplane existingAirplane = airplanes.get(i);
            if (existingAirplane.getKey() == key) {
                // Validate the new airplane object except for the key
                if (newAirplane.getAirspeed() <= 0 || newAirplane.getFuelBurn() <= 0 ||
                        newAirplane.getFuelCapacity() <= 0 || newAirplane.getMake() == null ||
                        newAirplane.getMake().trim().isEmpty() || newAirplane.getModel() == null ||
                        newAirplane.getModel().trim().isEmpty() || newAirplane.getType() == null ||
                        newAirplane.getType().trim().isEmpty()) {
                    System.out.println("Invalid airplane data provided.");
                    return;
                }

                // Update the airplane details but keep the original key
                newAirplane.setKey(existingAirplane.getKey());
                airplanes.set(i, newAirplane);
                saveAirplanesToFile(FILE_LOCATION);
                return;
            }
        }

        System.out.println("Airplane with key " + key + " not found.");
    }

    // Method to calculate the distance an airplane can fly
    public double calculateFlightRange(Airplane airplane) {
        if (airplane == null) {
            throw new IllegalArgumentException("Airplane cannot be null.");
        }
        // Ensure fuel burn and airspeed are not zero to avoid division by zero.
        if (airplane.fuelBurn == 0 || airplane.airspeed == 0) {
            throw new IllegalArgumentException("Fuel burn and airspeed must be greater than zero.");
        }

        return (airplane.fuelCapacity / airplane.fuelBurn) * airplane.airspeed;
    }

    // Method to search for an airplane by key
    public Airplane searchAirplane(int key) {
        for (Airplane airplane : airplanes) {
            if (airplane.getKey() == key) {
                return airplane;
            }
        }
        return null;
    }

    // Load airplanes from file
    private void loadAirplanesFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by comma to extract attributes
                String[] attributes = line.split(",");
                // Create a new Airplane object from attributes and add it to the vector
                airplanes.add(new Airplane(Double.parseDouble(attributes[0]),
                        Double.parseDouble(attributes[1]),
                        Double.parseDouble(attributes[2]),
                        attributes[3],
                        Integer.parseInt(attributes[4]),
                        attributes[5],
                        attributes[6],
                        attributes[7]));
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    // Save airplanes to file
    private void saveAirplanesToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Airplane airplane : airplanes) {
                writer.write(airplane.getAirspeed() + "," +
                        airplane.getFuelBurn() + "," +
                        airplane.getFuelCapacity() + "," +
                        airplane.getFuelType() + "," +
                        airplane.getKey() + "," +
                        airplane.getMake() + "," +
                        airplane.getModel() + "," +
                        airplane.getType() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
