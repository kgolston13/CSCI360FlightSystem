package csci360flightsystem;

/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: FlightPath.java 
 * Description:  
 * This class has the capability to create and display the flight path of an airplane from one airport to another. 
 * It can also search through the list of aiplanes and airports to find the correct flight path. The main method 
 * is used for creating an interface for the user to interact with the flight path system.
*/
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.Scanner;


public class FlightPath {
    // Attributes of the FlightPath class
    // key, starting airport, middle airport(s), ending airport, airplane

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
    private static Vector<FlightPath> flightPaths;

    // Constructor for the FlightPath class
    public FlightPath() {
        flightPaths = new Vector<>();
        loadFlightPathsFromFile("FlightLog.txt");
    }

    // Methods for the FlightPath class
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
        saveFlightPathsToFile("FlightLog.txt");
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
                saveFlightPathsToFile("FlightLog.txt");
                return;
            }
        }
        System.out.println("Flight path with key " + key + " not found.");
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

    // Method to calulate the heading of the flight path
    public double calculateHeading() {
        AirportManager airportManager = AirportManager.getInstance();

        Airport start = airportManager.searchAirport(this.startingAirport);
        Airport end = airportManager.searchAirport(this.endingAirport);

        if (start == null || end == null) {
            throw new IllegalStateException("Start or end airport not found.");
        }

        double lat1 = Math.toRadians(start.getLatitude());
        double lon1 = Math.toRadians(start.getLongitude());
        double lat2 = Math.toRadians(end.getLatitude());
        double lon2 = Math.toRadians(end.getLongitude());

        double dLon = lon2 - lon1;

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double theta = Math.atan2(y, x);

        return (Math.toDegrees(theta) + 360) % 360;
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

    // Method to manage airplanes
    public static void manageAirplanes(Scanner scanner) {
        AirplaneManager airplaneManager = AirplaneManager.getInstance();

        while (true) {
            System.out.println("\nManage Airplanes");
            System.out.println("1. Create Airplane");
            System.out.println("2. Modify Airplane");
            System.out.println("3. Delete Airplane");
            System.out.println("4. Display Airplanes");
            System.out.println("5. Display Airplane by Key");
            System.out.println("6. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Creating a new airplane...");
                    System.out.print("Enter the airplane's airspeed: ");
                    double airspeed = scanner.nextDouble();
                    System.out.print("Enter the airplane's fuel burn: ");
                    double fuelBurn = scanner.nextDouble();
                    System.out.print("Enter the airplane's fuel capacity: ");
                    double fuelCapacity = scanner.nextDouble();
                    System.out.print("Enter the airplane's fuel type: ");
                    String fuelType = scanner.next();
                    System.out.print("Enter the airplane's key: ");
                    int key = scanner.nextInt();
                    System.out.print("Enter the airplane's make: ");
                    String make = scanner.next();
                    System.out.print("Enter the airplane's model: ");
                    String model = scanner.next();
                    System.out.print("Enter the airplane's type: ");
                    String type = scanner.next();
                    try {
                        airplaneManager.createAirplane(
                                new Airplane(airspeed, fuelBurn, fuelCapacity, fuelType, key, make, model, type));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error creating airplane: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Modifying an existing airplane...");
                    System.out.print("Enter the airplane's key: ");
                    int modifyKey = scanner.nextInt();
                    Airplane modifyAirplane = airplaneManager.searchAirplane(modifyKey);
                    if (modifyAirplane != null) {
                        System.out.print("Enter the airplane's airspeed: ");
                        modifyAirplane.setAirspeed(scanner.nextDouble());
                        System.out.print("Enter the airplane's fuel burn: ");
                        modifyAirplane.setFuelBurn(scanner.nextDouble());
                        System.out.print("Enter the airplane's fuel capacity: ");
                        modifyAirplane.setFuelCapacity(scanner.nextDouble());
                        System.out.print("Enter the airplane's fuel type: ");
                        modifyAirplane.setFuelType(scanner.next());
                        System.out.print("Enter the airplane's make: ");
                        modifyAirplane.setMake(scanner.next());
                        System.out.print("Enter the airplane's model: ");
                        modifyAirplane.setModel(scanner.next());
                        System.out.print("Enter the airplane's type: ");
                        modifyAirplane.setType(scanner.next());
                        try {
                            airplaneManager.modifyAirplane(modifyKey, modifyAirplane);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Error modifying airplane: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Airplane with key " + modifyKey + " not found.");
                    }
                    break;
                case 3:
                    System.out.println("Deleting an existing airplane...");
                    System.out.print("Enter the airplane's key: ");
                    int deleteKey = scanner.nextInt();
                    Airplane deleteAirplane = airplaneManager.searchAirplane(deleteKey);
                    if (deleteAirplane != null) {
                        airplaneManager.deleteAirplane(deleteAirplane);
                    } else {
                        System.out.println("Airplane with key " + deleteKey + " not found.");
                    }
                    break;
                case 4:
                    System.out.println("Displaying all airplanes...");
                    airplaneManager.displayAirplanes();
                    break;
                case 5:
                    System.out.println("Displaying an airplane by key...");
                    System.out.print("Enter the airplane's key: ");
                    int displayKey = scanner.nextInt();
                    airplaneManager.searchAirplane(displayKey);
                    break;
                case 6:
                    System.out.println("Exiting the airplane management interface.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Method to manage airports
    public static void manageAirports(Scanner scanner) {
        AirportManager airportManager = AirportManager.getInstance();

        while (true) {
            System.out.println("\nManage Airports");
            System.out.println("1. Create Airport");
            System.out.println("2. Modify Airport");
            System.out.println("3. Delete Airport");
            System.out.println("4. Display Airports");
            System.out.println("5. Display Airport by ICAO");
            System.out.println("6. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Creating a new airport...");
                    System.out.print("Enter the airport's ICAO: ");
                    String ICAO = scanner.next();
                    System.out.print("Enter the airport's radio frequency (30 - 300 MHz for VHF): ");
                    double radioFrequency = scanner.nextDouble();
                    System.out.print("Enter the airport's latitude: ");
                    double latitude = scanner.nextDouble();
                    System.out.print("Enter the airport's longitude: ");
                    double longitude = scanner.nextDouble();
                    System.out.print("Enter the airport's name: ");
                    String name = scanner.next();
                    System.out.print("Enter the airport's fuel type: ");
                    String fuelType = scanner.next();
                    try {
                        airportManager.createAirport(
                                new Airport(ICAO, radioFrequency, "VHF", fuelType, latitude, longitude, name));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error creating airport: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Modifying an existing airport...");
                    System.out.print("Enter the airport's ICAO: ");
                    String modifyICAO = scanner.next();
                    Airport modifyAirport = airportManager.searchAirport(modifyICAO);
                    if (modifyAirport != null) {
                        System.out.print("Enter the airport's new latitude: ");
                        double newLatitude = scanner.nextDouble();
                        System.out.print("Enter the airport's new longitude: ");
                        double newLongitude = scanner.nextDouble();
                        System.out.print("Enter the airport's new name: ");
                        String newName = scanner.next();
                        System.out.print("Enter the airport's new radio frequency (30 - 300 MHz for VHF): ");
                        double newRadioFrequency = scanner.nextDouble();
                        System.out.print("Enter the airport's new fuel type: ");
                        String newFuelType = scanner.next();
                        modifyAirport.setLatitude(newLatitude);
                        modifyAirport.setLongitude(newLongitude);
                        modifyAirport.setName(newName);
                        modifyAirport.setRadioFrequency(newRadioFrequency); // Set the new radio frequency
                        modifyAirport.setFuelType(newFuelType);
                        // Radio type remains VHF and is not changed
                        try {
                            airportManager.modifyAirport(modifyICAO, modifyAirport);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Error modifying airport: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Airport with ICAO " + modifyICAO + " not found.");
                    }
                    break;
                case 3:
                    System.out.println("Deleting an existing airport...");
                    System.out.print("Enter the airport's ICAO: ");
                    String deleteICAO = scanner.next();
                    Airport deleteAirport = airportManager.searchAirport(deleteICAO);
                    if (deleteAirport != null) {
                        airportManager.deleteAirport(deleteAirport);
                    } else {
                        System.out.println("Airport with ICAO " + deleteICAO + " not found.");
                    }
                    break;
                case 4:
                    System.out.println("Displaying all airports...");
                    airportManager.displayAirports();
                    break;
                case 5:
                    System.out.println("Displaying an airport by ICAO...");
                    System.out.print("Enter the airport's ICAO: ");
                    String displayICAO = scanner.next();
                    airportManager.searchAirport(displayICAO);
                    break;
                case 6:
                    System.out.println("Exiting the airport management interface.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Method to manage flight paths
    public static void manageFlightPaths(Scanner scanner) {
        AirplaneManager airplaneManager = AirplaneManager.getInstance();
        FlightPath flightPath = new FlightPath();

        while (true) {
            System.out.println("\nManage Flight Paths");
            System.out.println("1. Create Flight Path");
            System.out.println("2. Modify Flight Path");
            System.out.println("3. Delete Flight Path");
            System.out.println("4. Display Flight Paths");
            System.out.println("5. Display Flight Path by Key");
            System.out.println("6. Display the heading of a flight path");
            System.out.println("7. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Creating a new flight path...");
                    System.out.print("Enter the flight path's key: ");
                    int key = scanner.nextInt();
                    System.out.print("Enter the flight path's starting airport: ");
                    String startingAirport = scanner.next();
                    System.out.print("Enter the flight path's middle airport(s) (separated by ;): ");
                    String middleAirports = scanner.next();
                    System.out.print("Enter the flight path's ending airport: ");
                    String endingAirport = scanner.next();
                    System.out.print("Enter the flight path's airplane key: ");
                    int airplaneKey = scanner.nextInt();
                    try {
                        FlightPath newFlightPath = new FlightPath(key, startingAirport,
                                Arrays.asList(middleAirports.split(";")), endingAirport,
                                airplaneManager.searchAirplane(airplaneKey));
                        FlightPath.createFlightPath(newFlightPath);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error creating flight path: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Modifying an existing flight path...");
                    System.out.print("Enter the flight path's key: ");
                    int modifyKey = scanner.nextInt();
                    FlightPath modifyFlightPath = flightPath.searchFlightPath(modifyKey);
                    if (modifyFlightPath != null) {
                        System.out.print("Enter the flight path's starting airport: ");
                        modifyFlightPath.setStartingAirport(scanner.next());
                        System.out.print("Enter the flight path's middle airport(s) (separated by ;): ");
                        modifyFlightPath.setMiddleAirports(Arrays.asList(scanner.next().split(";")));
                        System.out.print("Enter the flight path's ending airport: ");
                        modifyFlightPath.setEndingAirport(scanner.next());
                        System.out.print("Enter the flight path's airplane key: ");
                        modifyFlightPath.getAirplane().setKey(scanner.nextInt());
                        try {
                            FlightPath.modifyFlightPath(modifyKey, modifyFlightPath);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Error modifying flight path: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Flight path with key " + modifyKey + " not found.");
                    }
                    break;
                case 3:
                    System.out.println("Deleting an existing flight path...");
                    System.out.print("Enter the flight path's key: ");
                    int deleteKey = scanner.nextInt();
                    FlightPath deleteFlightPath = flightPath.searchFlightPath(deleteKey);
                    if (deleteFlightPath != null) {
                        FlightPath.deleteFlightPath(deleteKey);
                    } else {
                        System.out.println("Flight path with key " + deleteKey + " not found.");
                    }
                    break;
                case 4:
                    System.out.println("Displaying all flight paths...");
                    flightPath.displayFlightPaths();
                    break;
                case 5:
                    System.out.println("Displaying a flight path by key...");
                    System.out.print("Enter the flight path's key: ");
                    int displayKey = scanner.nextInt();
                    System.out.println(flightPath.searchFlightPath(displayKey));
                    break;
                case 6:
                    System.out.println("Displaying the heading of a flight path...");
                    System.out.print("Enter the flight path's key: ");
                    int headingKey = scanner.nextInt();
                    System.out.println("Heading: " + flightPath.searchFlightPath(headingKey).calculateHeading());
                    break;
                case 7:
                    System.out.println("Exiting the flight path management interface.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Main method for the Flight System, used for creating an interface for the
    // user to interact with the flight path system
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to the Flight System Interface!");
            System.out.println("1. Manage Airplanes");
            System.out.println("2. Manage Airports");
            System.out.println("3. Manage Flight Paths");
            System.out.println("4. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manageAirplanes(scanner);
                    break;
                case 2:
                    manageAirports(scanner);
                    break;
                case 3:
                    manageFlightPaths(scanner);
                    break;
                case 4:
                    System.out.println("Exiting the Flight System Interface.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}