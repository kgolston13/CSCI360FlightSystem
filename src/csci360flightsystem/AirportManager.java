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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AirportManager {
    
    private class AirportNode {
        private Airport airport;
        private Map<AirportNode, Double> edges;

        public AirportNode(Airport airport) {
            this.airport = airport;
            this.edges = new HashMap<>();
        }

        public Airport getAirport() {
            return airport;
        }

        public void addEdge(AirportNode node, double distance) {
            edges.put(node, distance);
        }
    }
    
    // Vector list of airports
    private Vector<Airport> airports;
    public Vector<Airport> getAirports() {
        return airports;
    }
    public Map<String, AirportNode> airportGraph;
    
    // Static instance for the singleton pattern
    private static AirportManager instance;
    
    // Private constructor for the AirportManager class
    public AirportManager() {
        airports = new Vector<>();
        airportGraph = new HashMap<>();
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

        // Add the airport to the graph   =======================              BUGGGED?! NEEDS FIXING
        AirportNode newNode = new AirportNode(airport);
        airportGraph.put(airport.getICAO(), newNode);

        for (AirportNode node : airportGraph.values()) {
            if (node != newNode) {
                double distance = calculateDistance(newNode.getAirport(), node.getAirport());
                newNode.addEdge(node, distance);
                node.addEdge(newNode, distance);
            }
        }

        // Save airports to file
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

    // Method to display all nodes and their edges
    public void displayNodesAndEdges() {
        System.out.println("Nodes and their corresponding edges:");

        // Iterate over each node in the airportGraph
        for (Map.Entry<String, AirportNode> entry : airportGraph.entrySet()) {
            String airportCode = entry.getKey();
            AirportNode node = entry.getValue();

            // Display airport code
            System.out.println("Airport: " + airportCode);

            // Display edges of the node
            for (Map.Entry<AirportNode, Double> edge : node.edges.entrySet()) {
                AirportNode neighbor = edge.getKey();
                double distance = edge.getValue();
                System.out.println("  -> Neighbor: " + neighbor.getAirport().getICAO() + ", Distance: " + distance);
            }
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

    public static double calculateDistance(Airport airport1, Airport airport2) {
        // Earth's radius in kilometers
        final double R = 6371.0;

        double lat1 = Math.toRadians(airport1.getLatitude());
        double lon1 = Math.toRadians(airport1.getLongitude());
        double lat2 = Math.toRadians(airport2.getLatitude());
        double lon2 = Math.toRadians(airport2.getLongitude());

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
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
    public void saveAirportsToFile(String fileName) {
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