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
import java.util.List;
import java.util.Map;
import java.util.Vector;

import csci360flightsystem.AirportManager.AirportNode;

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

    // Constructors for the FlightPath class
    public FlightPath(int key, String startingAirport, List<String> middleAirports, String endingAirport,
            Airplane airplane) {
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
    private static Vector<FlightPath> flightPaths = new Vector<>();

    // Constructor for the FlightPath class
    public FlightPath() {
        flightPaths = new Vector<>();
        loadFlightPathsFromFile(FILE_LOCATION);
    }

    // Methods for the FlightPath class
    // Override the toString method
    @Override
    public String toString() {
        return "FlightPath{" +
                "key=" + key +
                ", startingAirport='" + startingAirport + '\'' +
                ", middleAirports=" + middleAirports +
                ", endingAirport='" + endingAirport + '\'' +
                ", airplane=" + (airplane != null ? airplane.toString() : "null") +
                '}';
    }

    // Method to create a new flight path
    public static void createFlightPath(FlightPath flightPath) {
        if (flightPath.getKey() <= 0 || flightPath.getStartingAirport() == null ||
                flightPath.getEndingAirport() == null || flightPath.getAirplane() == null ||
                flightPath.getMiddleAirports().contains(null) || flightPath.getMiddleAirports().contains("")) {
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

    // Method to modify an existing flight path
    public static void modifyFlightPath(int key, FlightPath newFlightPath) {
        for (int i = 0; i < flightPaths.size(); i++) {
            FlightPath existingFlightPath = flightPaths.get(i);
            if (existingFlightPath.getKey() == key) {
                if (newFlightPath.getStartingAirport() == null || newFlightPath.getEndingAirport() == null ||
                        newFlightPath.getAirplane() == null || newFlightPath.getMiddleAirports().contains(null) ||
                        newFlightPath.getMiddleAirports().contains("")) {
                    System.out.println("Invalid flight path data provided for modification.");
                    return;
                }

                // Ensure the key is not modified
                newFlightPath.setKey(key);
                flightPaths.set(i, newFlightPath);
                saveFlightPathsToFile(FILE_LOCATION);
                return;
            }
        }
        System.out.println("Flight path with key " + key + " not found.");
    }

    // Method to delete a flight path based on its key
    public static void deleteFlightPath(int key) {
        FlightPath flightPathToRemove = null;
        for (FlightPath flightPath : flightPaths) {
            if (flightPath.getKey() == key) {
                flightPathToRemove = flightPath;
                break;
            }
        }

        if (flightPathToRemove != null) {
            flightPaths.remove(flightPathToRemove);
            saveFlightPathsToFile(FILE_LOCATION);
            System.out.println("Flight path with key " + key + " has been deleted.");
        } else {
            System.out.println("No flight path found with key " + key);
        }
    }

    // Method to display flight paths
    public static void displayFlightPaths() {
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

    // Method to search for a flight path
    private AirportManager airportManager = AirportManager.getInstance();

    public void launchFlight(String startingICAO, String endingICAO, Airplane airplane) {
        Airport start = airportManager.searchAirport(startingICAO);
        Airport end = airportManager.searchAirport(endingICAO);

        if (start == null || end == null || airplane == null) {
            System.out.println("Invalid flight path data provided.");
            return;
        }

        double distance = AirportManager.calculateDistance(start, end);
        double flightRange = airplane.getFuelCapacity() / airplane.getFuelBurn() * airplane.getAirspeed();

        System.out.println("Attempting to launch flight...");
        List<Airport> visitedAirports = new ArrayList<>();
        visitedAirports.add(start);

        if (distance <= flightRange) {
            // Direct flight is possible
            System.out.println("Direct flight is possible.");
            printFlightDetails(visitedAirports, end, distance, airplane);
        } else {
            // Start looking for multi-leg options
            Airport current = start;
            double totalDistance = 0;
            while (!current.equals(end)) {
                Airport next = findNextAirport(current, end, flightRange, airplane.getFuelType());
                if (next == null) {
                    System.out.println("Flight not possible with current fuel and range limitations.");
                    return;
                }
                visitedAirports.add(next);
                double legDistance = AirportManager.calculateDistance(current, next);
                totalDistance += legDistance;
                current = next;
            }
            printFlightDetails(visitedAirports, end, totalDistance, airplane);
        }
    }

    private Airport findNextAirport(Airport current, Airport target, double range, String fuelType) {
        Map<Airport, Double> possibleDestinations = new HashMap<>();

        for (Map.Entry<AirportNode, Double> entry : airportManager.getAirportGraph().get(current.getICAO()).edges
                .entrySet()) {
            Airport nextAirport = entry.getKey().getAirport();
            double distanceToNextAirport = entry.getValue();

            // Check if the next airport is within the range and has the required fuel type
            if (distanceToNextAirport <= range && nextAirport.getFuelType().equalsIgnoreCase(fuelType)) {
                // Calculate the distance from nextAirport to the target to ensure it's closer
                double distanceToTarget = AirportManager.calculateDistance(nextAirport, target);
                double currentDistanceToTarget = AirportManager.calculateDistance(current, target);

                // Only consider this airport if it gets us closer to the target
                if (distanceToTarget < currentDistanceToTarget) {
                    possibleDestinations.put(nextAirport, distanceToTarget);
                }
            }
        }

        // Select the next airport which is closest to the target among the possible
        // options
        return possibleDestinations.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private void printFlightDetails(List<Airport> visitedAirports, Airport end, double totalDistance,
            Airplane airplane) {
        Airport previousAirport = null;
        for (Airport airport : visitedAirports) {
            if (previousAirport != null) {
                double legHeading = calculateHeading(previousAirport, airport);
                System.out.println("Heading from " + previousAirport.getName() + " to " + airport.getName() + ": "
                        + direction(legHeading));
            }
            System.out.println("Visited " + airport.getName() + " - Radio Frequency: " + airport.getRadioFrequency());
            previousAirport = airport;
        }
        // Calculate heading from the last visited airport to the final destination
        if (previousAirport != null && end != null) {
            double finalLegHeading = calculateHeading(previousAirport, end);
            System.out.println("Final leg heading from " + previousAirport.getName() + " to " + end.getName() + ": "
                    + direction(finalLegHeading));
            System.out.println("Arrived at " + end.getName() + " - Radio Frequency: " + end.getRadioFrequency());
        }
        double totalFlightTime = totalDistance / airplane.getAirspeed();
        System.out.println("Total Distance: " + totalDistance + " km");
        System.out.println("Total Flight Time: " + totalFlightTime + " hours");
    }

    // Method to calulate the heading of the flight path
    public static double calculateHeading(Airport startingAirport, Airport endingAirport) {

        if (startingAirport == null || endingAirport == null) {
            throw new IllegalStateException("Start or end airport not found.");
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
                    List<String> middleAirportsICAOs = new Vector<>(Arrays.asList(attributes[2].split(";")));

                    // Initialize the list of middle airports
                    List<String> middleAirports = new Vector<>();
                    for (String icao : middleAirportsICAOs) {
                        Airport airport = airportManager.searchAirport(icao);// Using searchAirport method
                        if (airport != null) {
                            middleAirports.add(airport.getName());
                        }
                    }

                    Airplane airplane = airplaneManager.searchAirplane(Integer.parseInt(attributes[4]));
                    Airport startingAirport = airportManager.searchAirport(attributes[1]);
                    Airport endingAirport = airportManager.searchAirport(attributes[3]);

                    if (startingAirport != null && endingAirport != null) {
                        flightPaths.add(new FlightPath(Integer.parseInt(attributes[0]),
                                startingAirport.getName(), middleAirports, endingAirport.getName(), airplane));
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line + "; Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    // Save flight paths to file
    private static void saveFlightPathsToFile(String fileName) {
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

    // Main method for the FlightPath class
    public static void main(String[] args) {
        // Testing creation of flightpaths, modifying existing flightpaths, deleting
        // existing flightpaths,
        // display a specific flightpath by key, calculating the heading and direction,
        // and launching a flight,
        // and displaying all flight paths.

        // Add airports for testing
        // Airports for testing
        AirportManager airportManager = AirportManager.getInstance();
        Airport augusta = new Airport("KAGS", 270.30, "VHF", "JetA", 33.3703, -81.9649, "Augusta");
        Airport miami = new Airport("KMIA", 123.00, "VHF", "JetA", 25.7951, -80.2795, "Miami");
        Airport dallas = new Airport("KDFW", 122.95, "VHF", "JetA", 32.8990, -97.0336, "Dallas");
        Airport losAngeles = new Airport("KLAX", 122.95, "VHF", "JetA", 33.9422, -118.4036, "Los Angeles");
        Airport seattle = new Airport("KSEA", 119.90, "VHF", "JetA", 47.4484, -122.3086, "Seattle");
        Airport minneapolis = new Airport("KMSP", 122.95, "VHF", "JetA", 44.8851, -93.2144, "Minneapolis");
        Airport portland = new Airport("KPWM", 120.90, "VHF", "JetA", 43.6465, -43.6465, "Portland");
        Airport washington = new Airport("KIAD", 135.70, "VHF", "JetA", 38.9523, -77.4586, "Washington");
        Airport vancouver = new Airport("CYVR", 133.10, "VHF", "JetA", 49.1934, -123.1751, "Vancouver");
        Airport whitehorse = new Airport("CYXY", 121.90, "VHF", "JetA", 60.7141, -135.0761, "Whitehorse");
        Airport iqaluit = new Airport("CYFB", 122.20, "VHF", "JetA", 63.7570, -68.5450, "Iqaluit");
        Airport stJohns = new Airport("CYYT", 132.05, "VHF", "JetA", 47.3707, -52.4509, "St. John's");

        // Create airports
        airportManager.createAirport(augusta);
        airportManager.createAirport(miami);
        airportManager.createAirport(dallas);
        airportManager.createAirport(losAngeles);
        airportManager.createAirport(seattle);
        airportManager.createAirport(minneapolis);
        airportManager.createAirport(portland);
        airportManager.createAirport(washington);
        airportManager.createAirport(vancouver);
        airportManager.createAirport(whitehorse);
        airportManager.createAirport(iqaluit);
        airportManager.createAirport(stJohns);

        // Call the displayNodesAndEdges method to test if edges and nodes are set up
        airportManager.displayNodesAndEdges();

        // Add an airplane for testing
        AirplaneManager airplaneManager = AirplaneManager.getInstance();
        Airplane boeing777 = new Airplane(1225, 850, 2620, "JetA", 2, "Cessna", "Citation XLS", "Business Jet");
        airplaneManager.createAirplane(boeing777);

        // Test 1: Creating a flight path and display it
        System.out.println("Test 1: Creating a flight path");
        FlightPath flightPath1 = new FlightPath(1, "KAGS", Arrays.asList(), "KMIA", boeing777);
        createFlightPath(flightPath1);

        displayFlightPaths();

        // Test 2: Modifying an existing fligh path and display it
        System.out.println("Test 2: Modifying an existing flight path");
        FlightPath newFlightPathDetails = new FlightPath(1, "KMIA", Arrays.asList(), "CYFB", boeing777);
        modifyFlightPath(1, newFlightPathDetails);

        displayFlightPaths();

        // Test 3: Calculate the heading and direction of a flight
        System.out.println("Test 3: Calculate the heading and direction of a flight");
        System.out.println("Heading: " + calculateHeading(miami, iqaluit) + " degrees" + " ("
                + direction(calculateHeading(miami, iqaluit)) + ")");

        // Test 4: Launching a flight
        System.out.println("Test 4: Launching a flight");
        if (flightPath1.getStartingAirport() != null && flightPath1.getEndingAirport() != null) {
            flightPath1.launchFlight(flightPath1.getStartingAirport(), flightPath1.getEndingAirport(), boeing777);
        } else {
            System.out.println("Flight cannot be launched due to invalid airports.");
        }
        // Output the flight range of the airplane
        System.out.println("Flight range of the airplane: " + airplaneManager.calculateFlightRange(boeing777) + " km");
        // Output the distance between the starting and ending airports
        System.out.println("Distance between starting and ending airports: "
                + AirportManager.calculateDistance(miami, iqaluit) + " km");

        // Test 5: Deleting a flight path and attempting to display it
        System.out.println("Test 5: Deleting a flight path");
        deleteFlightPath(1);
        displayFlightPaths();
    }
}