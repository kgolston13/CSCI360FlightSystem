/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: FlightPath.java 
 * Description:  
 * This class has the capability to create, modify, delete, and display the flight path of an airplane from one airport to another. 
 * It can also search through the list of aiplanes and airports to find the correct flight path. The main method 
 * is used for creating an interface for the user to interact with the flight path system.
*/
package csci360flightsystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public class FlightPath {

    // Variables for file location
    private static final String FILE_LOCATION = "src/csci360flightsystem/FlightLog.txt";

    // Attributes of the FlightPath class
    // key, starting airport, middle airport(s), ending airport, airplane
    // changed location of txt

    // The unique identifier of an airplane, represented by type int.
    public int key;
    // The starting airport of a flight path, represented by type string.
    public String startingAirport;
    // The middle airport(s) of a flight path, represented by type list of strings.
    public List<String> middleAirports;
    // The ending airport of a flight path, represented by type string.
    public String endingAirport;
    // The airplane of a flight path, represented by type Airplane.
    public Airplane airplane;
    // Static instance for singleton pattern
    private static FlightPath instance;

    // Constructors for the FlightPath class
    // Overloaded constructor without middleAirports attribute (will be added later)
    public FlightPath(int key, String startingAirport, String endingAirport, Airplane airplane) {
        this.key = key;
        this.startingAirport = startingAirport;
        this.middleAirports = new ArrayList<>(); // Initialize with an empty list
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

    public List<String> getMiddleAirports() {
        return middleAirports;
    }

    public void setMiddleAirports(List<String> middleAirports) {
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

    public void addMiddleAirport(String airportCode) {
        if (!middleAirports.contains(airportCode)) {
            middleAirports.add(airportCode);
        }
    }

    // Vector list of flight paths
    private Vector<FlightPath> flightPaths;

    // Public method to get the list of flight paths
    public Vector<FlightPath> getFlightPaths() {
        return flightPaths; // Return a copy of the flightPaths vector
    }

    // Private constructor to prevent instantiation
    private FlightPath() {
        flightPaths = new Vector<>();
        loadFlightPathsFromFile(FILE_LOCATION);
    }

    // Static method to get instance
    public static FlightPath getInstance() {
        if (instance == null) {
            instance = new FlightPath();
        }
        return instance;
    }

    // Methods for the FlightPath class
    // Method to create a new flight path
    public void createFlightPath(FlightPath flightPath) {
        if (flightPath.getKey() <= 0 || flightPath.getStartingAirport() == null ||
                flightPath.getEndingAirport() == null || flightPath.getAirplane() == null) {
            System.out.println("Invalid flight path data provided.");
            return;
        }

        for (FlightPath existingFlightPath : flightPaths) {
            if (existingFlightPath.getKey() == flightPath.getKey()) {
                System.out.println("FlightPath with key " + flightPath.getKey() + " already exists.");
                return;
            }
        }

        flightPaths.add(flightPath);
        saveFlightPathsToFile(FILE_LOCATION);
    }

    // Method to modify a flight path
    public void modifyFlightPath(int key, FlightPath newFlightPathDetails) {
        for (FlightPath flightPath : flightPaths) {
            if (flightPath.getKey() == key) {
                // Found the flight path to modify

                // Validate new details
                if (newFlightPathDetails.getStartingAirport() == null ||
                        newFlightPathDetails.getEndingAirport() == null ||
                        newFlightPathDetails.getAirplane() == null) {
                    System.out.println("Invalid flight path data provided.");
                    return;
                }

                // Update the flight path details
                flightPath.setStartingAirport(newFlightPathDetails.getStartingAirport());
                flightPath.setEndingAirport(newFlightPathDetails.getEndingAirport());
                flightPath.setAirplane(newFlightPathDetails.getAirplane());

                saveFlightPathsToFile(FILE_LOCATION);
                System.out.println("Flight path updated successfully.");
                return;
            }
        }

        System.out.println("Flight path with key " + key + " not found.");
    }

    // Method to delete a flight path
    public void deleteFlightPath(int key) {
        boolean found = false;
        for (int i = 0; i < flightPaths.size(); i++) {
            if (flightPaths.get(i).getKey() == key) {
                flightPaths.remove(i);
                found = true;
                saveFlightPathsToFile(FILE_LOCATION);
                System.out.println("Flight path deleted successfully.");
                break;
            }
        }

        if (!found) {
            System.out.println("Flight path with key " + key + " not found.");
        }
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

    public void launchFlight(FlightPath flightPath) {
        AirportManager airportManager = AirportManager.getInstance();
        AirplaneManager airplaneManager = AirplaneManager.getInstance();

        Airport startingAirport = airportManager.searchAirport(flightPath.getStartingAirport());
        Airport endingAirport = airportManager.searchAirport(flightPath.getEndingAirport());
        Airplane airplane = flightPath.getAirplane();

        double flightRange = airplaneManager.calculateFlightRange(airplane);

        List<String> path = isPathAvailable(startingAirport, endingAirport, flightRange, airportManager,
                airplane.getFuelType());

        if (!path.isEmpty() && path.size() >= 2) {
            double totalDistance = 0;
            Airport prevAirport = startingAirport;

            // Calculate the total distance by summing the distances of each leg
            for (String airportCode : path) {
                Airport nextAirport = airportManager.searchAirport(airportCode);
                if (nextAirport != null && prevAirport != null) {
                    totalDistance += AirportManager.calculateDistance(prevAirport, nextAirport);
                    prevAirport = nextAirport;
                }
            }

            // Add middle airports from the path to the flightPath object, excluding start
            // and end
            flightPath.getMiddleAirports().clear();
            for (int i = 1; i < path.size() - 1; i++) {
                flightPath.addMiddleAirport(path.get(i));
            }

            // Calculate time
            double time = totalDistance / airplane.getAirspeed(); // Assuming constant speed

            System.out.println(
                    "Flight path found. Total distance: " + totalDistance + " km. Estimated time: " + time + " hours.");
        } else {
            System.out.println("No viable flight path found. Cannot launch flight.");
        }
    }

    // Method to check if a path is available with refueling stops
    private List<String> isPathAvailable(Airport start, Airport end, double range, AirportManager airportManager,
            String requiredFuelType) {
        Set<String> visited = new HashSet<>();
        Stack<AirportManager.AirportNode> stack = new Stack<>();
        Map<Airport, Airport> prev = new HashMap<>(); // To track the path
        stack.push(airportManager.new AirportNode(start));

        while (!stack.isEmpty()) {
            AirportManager.AirportNode node = stack.pop();
            Airport currentAirport = node.getAirport();
            if (currentAirport == null || visited.contains(currentAirport.getICAO()))
                continue;
            visited.add(currentAirport.getICAO());

            if (currentAirport.getICAO().equals(end.getICAO())) {
                // Build and return the path using the prev map
                List<String> path = new ArrayList<>();
                for (Airport at = end; at != null; at = prev.get(at)) {
                    path.add(0, at.getICAO()); // Add to the beginning of the list
                }
                return path;
            }

            // Push all unvisited and reachable airports onto the stack
            for (Map.Entry<AirportManager.AirportNode, Double> edge : node.edges.entrySet()) {
                AirportManager.AirportNode adjacentNode = edge.getKey();
                Airport nextAirport = adjacentNode.getAirport();
                double distanceToNextAirport = edge.getValue();

                if (!visited.contains(nextAirport.getICAO()) && distanceToNextAirport <= range
                        && nextAirport.getFuelType().equalsIgnoreCase(requiredFuelType)) {
                    stack.push(adjacentNode);
                    prev.put(nextAirport, currentAirport); // Track the path
                }
            }
        }
        return new ArrayList<>(); // Return an empty list if no path found
    }

    // Method to calulate the heading of the flight path
    public static double calculateHeading(Airport startingAirport, Airport endingAirport) {

        if (startingAirport == null || endingAirport == null) {
            throw new IllegalStateException("Start or end airport not found.");
        }

        // Handling the case where the starting point is the South Pole
        if (startingAirport.getLatitude() == -90.0) {
            double lon2 = endingAirport.getLongitude();
            return (lon2 + 360) % 360; // Normalize the longitude to [0, 360) for heading
        }

        // Handling the case where the starting point is the North Pole
        if (startingAirport.getLatitude() == 90.0) {
            double lon2 = endingAirport.getLongitude();
            // Adjust the heading to be opposite the meridian's direction
            return (180 + lon2 + 360) % 360; // Normalize to [0, 360)
        }

        double lat1 = Math.toRadians(startingAirport.getLatitude());
        double lon1 = Math.toRadians(startingAirport.getLongitude());
        double lat2 = Math.toRadians(endingAirport.getLatitude());
        double lon2 = Math.toRadians(endingAirport.getLongitude());

        double dLon = lon2 - lon1;

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double theta = Math.atan2(y, x);

        return (Math.toDegrees(theta) + 360) % 360;
    }

    // Direction based on heading
    public static String direction(double heading) {
        if ((heading >= 0 && heading <= 22.5) || (heading > 337.5 && heading <= 360)) {
            return "North";
        } else if (heading > 22.5 && heading <= 67.5) {
            return "Northeast";
        } else if (heading > 67.5 && heading <= 112.5) {
            return "East";
        } else if (heading > 112.5 && heading <= 157.5) {
            return "Southeast";
        } else if (heading > 157.5 && heading <= 202.5) {
            return "South";
        } else if (heading > 202.5 && heading <= 247.5) {
            return "Southwest";
        } else if (heading > 247.5 && heading <= 292.5) {
            return "West";
        } else if (heading > 292.5 && heading <= 337.5) {
            return "Northwest";
        } else {
            return "Unknown";
        }
    }

    // Load flight paths from file
    private void loadFlightPathsFromFile(String fileName) {
        AirportManager airportManager = AirportManager.getInstance();
        AirplaneManager airplaneManager = AirplaneManager.getInstance();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] attributes = line.split(",");
                    List<String> middleAirportsICAOs = new ArrayList<>(Arrays.asList(attributes[2].split(";")));

                    // Make sure the ICAO codes are valid
                    List<String> validMiddleAirports = new ArrayList<>();
                    for (String icao : middleAirportsICAOs) {
                        if (airportManager.searchAirport(icao) != null) {
                            validMiddleAirports.add(icao);
                        }
                    }

                    Airplane airplane = airplaneManager.searchAirplane(Integer.parseInt(attributes[4]));
                    if (airplane == null) {
                        continue; // Skip this flight path if the airplane is not found
                    }

                    Airport startingAirport = airportManager.searchAirport(attributes[1]);
                    Airport endingAirport = airportManager.searchAirport(attributes[3]);
                    if (startingAirport == null || endingAirport == null) {
                        continue; // Skip this flight path if any airport is not found
                    }

                    // Using ICAO codes for airports
                    FlightPath flightPath = new FlightPath(Integer.parseInt(attributes[0]),
                            startingAirport.getICAO(), endingAirport.getICAO(), airplane);
                    flightPath.setMiddleAirports(validMiddleAirports);
                    flightPaths.add(flightPath);

                } catch (Exception e) {
                    System.err.println("Error processing line: " + line + "; Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    // Save flight paths to file
    private void saveFlightPathsToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (FlightPath flightPath : flightPaths) {
                String middleAirportsStr = String.join(";", flightPath.getMiddleAirports());
                writer.write(flightPath.getKey() + "," +
                        flightPath.getStartingAirport() + "," +
                        middleAirportsStr + "," +
                        flightPath.getEndingAirport() + "," +
                        (flightPath.getAirplane() != null ? flightPath.getAirplane().getKey() : "null") + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // toString method to display the attributes of a flight path
    @Override
    public String toString() {
        return "FlightPath{" +
                "key=" + key +
                ", startingAirport='" + startingAirport + '\'' +
                ", middleAirports=" + middleAirports +
                ", endingAirport='" + endingAirport + '\'' +
                ", airplane=" + airplane +
                '}';
    }
}