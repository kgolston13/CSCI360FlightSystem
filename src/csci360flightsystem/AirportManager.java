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
    private Vector<Airport> airports;

    // Static instance for the singleton pattern
    private static AirportManager instance;

    // Private constructor for the AirportManager class
    private AirportManager() {
        airports = new Vector<>();
        loadAirportsFromFile("Airports.txt");
    }

    // Methods for the AirportManager class
    // Public method to get the instance of the class
    public static AirportManager getInstance() {
        if (instance == null) {
            instance = new AirportManager();
        }
        return instance;
    }

    // Method to create a new airport
    public void createAirport(Airport airport) {
        // Validate the airport object
        if (airport.getICAO().length() != 4 ||
                airport.getLatitude() < -90 || airport.getLatitude() > 90 ||
                airport.getLongitude() < -180 || airport.getLongitude() > 180 ||
                airport.getRadioFrequency() < 30 || airport.getRadioFrequency() > 300 ||
                airport.getName() == null || airport.getName().trim().isEmpty() ||
                airport.getFuelType() == null || airport.getFuelType().trim().isEmpty() ||
                (airport.getICAO().charAt(0) != 'C' || airport.getICAO().charAt(0) != 'K')) {
            System.out.println("Invalid airport data provided.");
            return;
        }

        // Check if the airport with the same ICAO already exists
        for (Airport existingAirport : airports) {
            if (existingAirport.getICAO().equals(airport.getICAO())) {
                System.out.println("Airport with ICAO " + airport.getICAO() + " already exists.");
                return;
            }
        }

        // Add the airport to the list
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

    // Method to modify an existing airport
    public void modifyAirport(String icao, Airport newAirport) {
        for (int i = 0; i < airports.size(); i++) {
            if (airports.get(i).getICAO().equals(icao)) {
                // Ensure the new ICAO code matches the existing one
                if (!newAirport.getICAO().equals(icao) ||
                        newAirport.getLatitude() < -90 || newAirport.getLatitude() > 90 ||
                        newAirport.getLongitude() < -180 || newAirport.getLongitude() > 180 ||
                        newAirport.getRadioFrequency() < 30 || newAirport.getRadioFrequency() > 300 ||
                        newAirport.getName() == null || newAirport.getName().trim().isEmpty() ||
                        newAirport.getFuelType() == null || newAirport.getFuelType().trim().isEmpty()) {
                    System.out.println("Invalid airport data provided.");
                    return;
                }

                // Update the airport details without changing the ICAO code
                newAirport.setICAO(icao);
                airports.set(i, newAirport);
                saveAirportsToFile("Airports.txt");
                return;
            }
        }
        System.out.println("Airport with ICAO " + icao + " not found.");
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
