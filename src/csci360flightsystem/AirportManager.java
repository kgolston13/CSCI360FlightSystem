/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: AirportManager.java 
 * Description:  
 * This file has the capability to create, modify, delete, display, or search airport objects.
*/ 
package csci360flightsystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class AirportManager {
    // Vector list of airports
    Vector<Airport> airports;

    // Constructor for the AirportManager class
    public AirportManager() {
        airports = new Vector<>();
        loadAirportsFromFile("Airports.txt");
    }

    //Methods for the AirportManager class
    // Method to create a new airport
    public void createAirport(Airport airport) {
        airports.add(airport);
        saveAirportsToFile("Airports.txt");
    }

    // Method to delete an airport
    public void deleteAirport(Airport airport) {
        airports.remove(airport);
        saveAirportsToFile("Airports.txt");
    }

    // Method to display all airports
    public void displayAirports() {
        for (Airport airport : airports) {
            System.out.println(airport);
        }
    }

    // Method to modify an airport
    public void modifyAirport(int index, Airport newAirport) {
        airports.set(index, newAirport);
        saveAirportsToFile("Airports.txt");
    }

    // Method to search for an airport by code
    public Airport searchAirport(String code) {
        for (Airport airport : airports) {
            if (airport.getICAO().equals(code)) {
                return airport;
            }
        }
        return null; // Return null if airport with specified code is not found
    }

    // Load airports from file
    private void loadAirportsFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by comma to extract attributes
                String[] attributes = line.split(",");
                // Create a new Airport object from attributes and add it to the vector
                airports.add(new Airport(attributes[0],
                                          Double.parseDouble(attributes[1]),
                                          attributes[2],
                                          attributes[3],
                                          Double.parseDouble(attributes[4]),
                                          Double.parseDouble(attributes[5]),
                                          attributes[6]));
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    // Save airports to file
    private void saveAirportsToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Airport airport : airports) {
                writer.write(airport.getICAO() + "," +
                             airport.getRadioFrequency() + "," +
                             airport.getRadioType() + "," +
                             airport.getFuelType() + "," +
                             airport.getLatitude() + "," +
                             airport.getLongitude() + "," +
                             airport.getName() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
