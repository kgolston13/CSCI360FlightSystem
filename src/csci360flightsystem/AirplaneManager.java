/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: AirplaneManager.java 
 * Description:  
 * This file has the capability to create, modify, delete, display, or search airplane objects.
*/ 
package csci360flightsystem;
//This is a test to see if my connection is working
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class AirplaneManager {
    // Vector list of airplanes
    public static Vector<Airplane> airplanes;

    // Constructor for the AirplaneManager class
    public AirplaneManager() {
        airplanes = new Vector<>();
        loadAirplanesFromFile("Airplanes.txt");
    }

    //Methods for the AirplaneManager class
    // Method to create a new airplane
    public void createAirplane(Airplane airplane) {
        airplanes.add(airplane);
        saveAirplanesToFile("Airplanes.txt");
    }

    // Method to delete an airplane
    public void deleteAirplane(Airplane airplane) {
        airplanes.remove(airplane);
        saveAirplanesToFile("Airplanes.txt");
    }

    // Method to display all airplanes
    public void displayAirplanes() {
        for (Airplane airplane : airplanes) {
            System.out.println(airplane);
        }
    }

    // Method to modify an airplane
    public void modifyAirplane(int index, Airplane newAirplane) {
        airplanes.set(index, newAirplane);
        saveAirplanesToFile("Airplanes.txt");
    }

    // Method to search for an airplane by key
    public Airplane searchAirplane(int key) {
        for (Airplane airplane : airplanes) {
            if (airplane.getKey() == key) {
                return airplane;
            }
        }
        return null; // Return null if airplane with specified key is not found
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
                                            Double.parseDouble(attributes[3]),
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
