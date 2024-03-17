/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: FlightPath.java 
 * Description:  
 * This class has the capability to create and display the flight path of an airplane from one airport to another. 
 * It can also search through the list of aiplanes and airports to find the correct flight path.
*/ 
package csci360flightsystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class FlightPath {
	//Attributes of the FlightPath class
	//key, starting airport, middle airport(s), ending airport, airplane

	//The unique identifier of an airplane, represented by type int.
	public int key;
	//The starting airport of a flight path, represented by type string.
	public String startingAirport;
	//The middle airport(s) of a flight path, represented by type string.
	public String middleAirports;
	//The ending airport of a flight path, represented by type string.
	public String endingAirport;
	//The airplane of a flight path, represented by type Airplane.
	public Airplane airplane;

	// Constructors for the FlightPath class
	public FlightPath(int key, String startingAirport, String middleAirports, String endingAirport, Airplane airplane) {
		this.key = key;
		this.startingAirport = startingAirport;
		this.middleAirports = middleAirports;
		this.endingAirport = endingAirport;
		this.airplane = airplane;
	}

	// Getters and setters for the FlightPath class
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getStartingAirport() {
		return startingAirport;
	}
	public void setStartingAirport(String startingAirport) {
		this.startingAirport = startingAirport;
	}
	public String getMiddleAirports() {
		return middleAirports;
	}
	public void setMiddleAirports(String middleAirports) {
		this.middleAirports = middleAirports;
	}
	public String getEndingAirport() {
		return endingAirport;
	}
	public void setEndingAirport(String endingAirport) {
		this.endingAirport = endingAirport;
	}
	public Airplane getAirplane() {
		return airplane;
	}
	public void setAirplane(Airplane airplane) {
		this.airplane = airplane;
	}

	// Vector list of flight paths
	private static Vector<FlightPath> flightPaths;

	// Constructor for the FlightPath class
	public FlightPath() {
		flightPaths = new Vector<>();
		loadFlightPathsFromFile("FlightLog.txt");
	}

	//Methods for the FlightPath class
	// Method to create a new flight path
    public static void createFlightPath(FlightPath flightPath) {
        flightPaths.add(flightPath);
        saveFlightPathsToFile("FlightLog.txt");
    }

    // Method to modify an existing flight path
    public static void modifyFlightPath(int index, FlightPath newFlightPath) {
        flightPaths.set(index, newFlightPath);
        saveFlightPathsToFile("FlightLog.txt");
    }

    // Method to delete a flight path
    public static void deleteFlightPath(int index) {
        flightPaths.remove(index);
        saveFlightPathsToFile("FlightLog.txt");
    }
	// Method to display flight paths
    public void displayFlightPaths() {
        for (FlightPath flightPath : flightPaths) {
            System.out.println(flightPath);
        }
    }

    // Method to search for a flight path by key
    public FlightPath searchFlightPath(int key) {
        for (FlightPath flightPath : flightPaths) {
            if (flightPath.getKey() == key) {
                return flightPath;
            }
        }
        return null; // Return null if flight path with specified key is not found
    }

    // Method to launch a flight based on the flight path
    public void launchFlight(FlightPath flightPath) {
        // Flight logic to be added here in the future
        System.out.println("Flight launched based on flight path: " + flightPath);
    }

    // Load flight paths from file
    private void loadFlightPathsFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by comma to extract attributes
                String[] attributes = line.split(",");
                // Create a new FlightPath object from attributes and add it to the vector
                flightPaths.add(new FlightPath(Integer.parseInt(attributes[0]),
                        attributes[1], attributes[2], attributes[3], null)); // You might need to adjust this if Airplane object is available in FlightLog.txt
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    // Save flight paths to file
    private static void saveFlightPathsToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (FlightPath flightPath : flightPaths) {
                writer.write(flightPath.getKey() + "," +
                        flightPath.getStartingAirport() + "," +
                        flightPath.getMiddleAirports() + "," +
                        flightPath.getEndingAirport() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        // Create a new FlightPath object
        FlightPath flightPath = new FlightPath(1, "LAX", "DFW", "JFK", null);
        // Create a new Airplane object
        Airplane airplane = new Airplane(500, 100, 1000, 1, 1, "Boeing", "747", "Commercial");
        // Set the airplane for the flight path
        flightPath.setAirplane(airplane);
        // Create a new FlightPathManager object
        FlightPath flightPathManager = new FlightPath();
        // Create a new flight path
        flightPathManager.createFlightPath(flightPath);
        // Display all flight paths
        flightPathManager.displayFlightPaths();
        // Modify the flight path
        flightPathManager.modifyFlightPath(0, new FlightPath(1, "LAX", "DFW", "JFK", airplane));
        // Display all flight paths
        flightPathManager.displayFlightPaths();
        // Delete the flight path
        flightPathManager.deleteFlightPath(0);
        // Display all flight paths
        flightPathManager.displayFlightPaths();
    }
}