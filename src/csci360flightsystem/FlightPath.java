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
import java.util.Scanner;

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
    
        double heading = (Math.toDegrees(theta) + 360) % 360;
    
        // Truncate the result to four decimal points
        heading = Math.round(heading * 10000.0) / 10000.0;
    
        return heading;
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

    public static double calculateFlightTime(double distanceKm, double cruiseSpeedKnots) {
        // Convert cruise speed from knots to km/h (1 knot ≈ 1.852 km/h)
        double cruiseSpeedKmh = cruiseSpeedKnots * 1.852;
        
        // Calculate time (hours) = distance (km) / speed (km/h)
        double flightTimeHours = distanceKm / cruiseSpeedKmh;
        
        // Truncate the result to two decimal points
        flightTimeHours = Math.round(flightTimeHours * 100.0) / 100.0;
        
        return flightTimeHours;
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

    public static void LaunchFlightPath2(){

        AirportManager airportManager = AirportManager.getInstance();
        AirplaneManager airplaneManager = AirplaneManager.getInstance();

        Scanner userInput = new Scanner(System.in);

        System.out.println("Please enter Starting Airport");
        String startAirport = userInput.next();
        System.out.println("Please enter ending Airport");
        String endAirport = userInput.next();
        System.out.println("Enter model of airplane");
        String modelTemp = userInput.next();
        double tempRange = (airplaneManager.calculateFlightRange(airplaneManager.getAirplaneByModel(modelTemp)));
        String tempFuel = (airplaneManager.getAirplaneByModel(modelTemp).getFuelType());
        
        // Call the method directly with parameters
        List<Airport> shortestFlightPath = airportManager.searchForShortestFlightPath(
                /* startingAirport */ airportManager.searchAirport(startAirport),
                /* endingAirport */ airportManager.searchAirport(endAirport),
                /* maxDistanceBetweenNodes */ tempRange, // This value will be replaced by plane max distance
                
                /* fuelTypeOfPlane */tempFuel);                                   // This value will be set by Airplane.getFuelType()
    
        // Check if a path is found
        if (shortestFlightPath != null) {
            // Print the shortest flight path
            System.out.println("\nShortest Flight Path:");
            Airport previousAirport = null;
            double totalDistance = 0;
            double totalTime = 0;
            for (Airport airport : shortestFlightPath) {
                System.out.println(airport);
                if (previousAirport != null && shortestFlightPath.size() > 2){
                    double distanceTemp = AirportManager.calculateDistance(airport, previousAirport);
                    System.out.println("Connecting flight distance is..." + distanceTemp + "km");
                    double headingTemp = calculateHeading(airport, previousAirport);
                    System.out.println("Heading is " + headingTemp);
                    double timeTemp = calculateFlightTime(distanceTemp, airplaneManager.getAirplaneByModel(modelTemp).airspeed);
                    System.out.println("Time to complete this leg is " + timeTemp + " hrs");
                    totalDistance += distanceTemp;
                    totalTime += timeTemp;
                }
                previousAirport = airport;
            }
            if(shortestFlightPath.size() == 2){
                totalDistance = AirportManager.calculateDistance(airportManager.searchAirport(startAirport),airportManager.searchAirport(endAirport));
            }
            //double headingTemp = calculateHeading(airportManager.searchAirport(startAirport),airportManager.searchAirport(endAirport));

            System.out.println("Total distance of trip is..." + totalDistance + "km"); 
            System.out.println("Total Time is " + totalTime + " hrs");
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

        AirplaneManager airplaneManager = AirplaneManager.getInstance();

        Scanner userInput = new Scanner(System.in);

        boolean end = false;
        
        System.out.println("THIS SOFTWARE IS NOT TO BE USED FOR FLIGHT PLANNING OR NAVIGATIONAL PURPOSE");

        while (!end) {
            System.out.println("\n0: Add Airplane");
            System.out.println("1: Delete Airplane");
            System.out.println("2: Modify Airplane");
            System.out.println("3: Search Airplane");
            System.out.println("4: Display Airplanes");
            System.out.println("5: Add Airports");
            System.out.println("6: Delete Airports");
            System.out.println("7: Modify Airports");
            System.out.println("8: Search Airports");
            System.out.println("9: Display Airports");
            System.out.println("10: Launch Flight Path Finder");
            System.out.println("-1: Exit Program");
            
            int choice = userInput.nextInt();
            
            switch (choice) {
                case 0:
                AirplaneManager.userCreateAirplane();
                break;
                case 1:
                AirplaneManager.deleteAirplaneByModel();
                break;
                case 2:
                AirplaneManager.UIModifyAirplane();
                break;
                case 3:
                AirplaneManager.searchAirplaneByModel();
                break;
                case 4:
                airplaneManager.displayAirplanes();
                break;
                case 5:
                AirportManager.userCreateAirport();
                break;
                case 6:
                AirportManager.deleteAirportByICAO();
                break;
                case 7:
                AirportManager.UIModifyAirport();
                break;
                case 8:
                AirportManager.UISearchAirport();
                break;
                case 9:
                airportManager.displayAirports();
                break;
                case 10:
                LaunchFlightPath2();
                break;
                case -1:
                end = true;
                break;
                default:
                System.out.println("Invalid choice. Please enter a valid option.");
                break;
            }
        }
        
        //Airplane tests
        //AirplaneManager.userCreateAirplane();
        //AirplaneManager.deleteAirplaneByModel();
        //AirplaneManager.UIModifyAirplane();
        //AirplaneManager.searchAirplaneByModel();
        //airplaneManager.displayAirplanes();
        
        //Airport tests
        //AirportManager.userCreateAirport();
        //AirportManager.deleteAirportByICAO();
        //AirportManager.UIModifyAirport();
        //AirportManager.UISearchAirport();
        //airportManager.displayAirports();

        //Flight path test
        //LaunchFlightPath2();
    }
}