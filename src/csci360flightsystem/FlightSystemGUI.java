/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: FlightSystemGUI.java 
 * Description:  
 * This class is responsible for creating the GUI for the flight system.
*/
package csci360flightsystem;

import javax.swing.*;
import java.awt.*;

public class FlightSystemGUI extends JFrame {

    // Main panel to hold different panels
    private JPanel mainPanel;

    // Constructor to initialize the GUI
    public FlightSystemGUI() {
        setTitle("CSCI 360 Flight System");
        setSize(800, 600); // Set the initial size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(createMenuBar());
        initializeUI();
    }

    // Method to initialize the GUI
    private void initializeUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout()); // Use CardLayout to switch between different panels
        setContentPane(mainPanel);

        // Add different panels to the main panel
        mainPanel.add(AirportsPanel(), "Airports");
        mainPanel.add(AirplanesPanel(), "Airplanes");
        mainPanel.add(FlightPathsPanel(), "FlightPaths");

        showPanel("Airports"); // Show the airports panel by default
    }

    // Method to show a specific panel
    private void showPanel(String name) {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, name);
    }

    // Method to create the menu bar
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu manageMenu = new JMenu("Manage");
        JMenuItem airportsItem = new JMenuItem("Airports");
        airportsItem.addActionListener(event -> showPanel("Airports"));
        JMenuItem airplanesItem = new JMenuItem("Airplanes");
        airplanesItem.addActionListener(event -> showPanel("Airplanes"));
        JMenuItem flightPathsItem = new JMenuItem("Flight Paths");
        flightPathsItem.addActionListener(event -> showPanel("FlightPaths"));

        manageMenu.add(airportsItem);
        manageMenu.add(airplanesItem);
        manageMenu.add(flightPathsItem);

        menuBar.add(manageMenu);
        return menuBar;
    }

    // Method to create the airports panel
    private JPanel AirportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Toggle panel at the top for switching between views
        JPanel togglePanel = new JPanel(new FlowLayout());
        JToggleButton tableViewButton = new JToggleButton("Table View", true);
        JToggleButton graphViewButton = new JToggleButton("Graph View");
        ButtonGroup viewToggleGroup = new ButtonGroup();
        viewToggleGroup.add(tableViewButton);
        viewToggleGroup.add(graphViewButton);
        togglePanel.add(tableViewButton);
        togglePanel.add(graphViewButton);
        panel.add(togglePanel, BorderLayout.NORTH);

        // Main content panel with CardLayout
        JPanel contentPanel = new JPanel(new CardLayout());
        JPanel tablePanel = createAirportTable(); // Separate method for creating the table panel
        JPanel graphPanel = createAirportGraph(); // Separate method for creating the graph panel
        contentPanel.add(tablePanel, "Table");
        contentPanel.add(graphPanel, "Graph");
        panel.add(contentPanel, BorderLayout.CENTER);

        // Toggle action listeners
        tableViewButton.addActionListener(e -> {
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Table");
            tableViewButton.setSelected(true);
        });
        graphViewButton.addActionListener(e -> {
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Graph");
            graphViewButton.setSelected(true);
        });

        return panel;
    }

    // Method to creat the airport table panel
    private JPanel createAirportTable() {
        JPanel tablePanel = new JPanel(new BorderLayout());

        // Table for displaying airports
        String[] columnNames = { "ICAO", "Radio Frequency", "Fuel Type", "Latitude", "Longitude", "Name" };
        Object[][] data = {}; // Populate with airport data
        JTable airportsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(airportsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Search panel
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        tablePanel.add(searchPanel, BorderLayout.NORTH);

        // Search button action listener
        searchButton.addActionListener(e -> {
            String searchICAO = searchField.getText().trim();
            AirportManager manager = AirportManager.getInstance();
            Airport foundAirport = manager.searchAirport(searchICAO);

            if (foundAirport != null) {
                // Find the row number where the airport is located
                for (int i = 0; i < airportsTable.getRowCount(); i++) {
                    if (airportsTable.getValueAt(i, 0).equals(foundAirport.getICAO())) {
                        // Scroll to the found row and highlight it
                        airportsTable.setRowSelectionInterval(i, i);
                        airportsTable.scrollRectToVisible(new Rectangle(airportsTable.getCellRect(i, 0, true)));
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Airport with ICAO: " + searchICAO + " not found.");
            }
        });

        // Bottom panel for create, modify, delete actions
        JPanel actionPanel = new JPanel();
        JButton createAirport = new JButton("Create");
        JButton modifyAirport = new JButton("Modify");
        JButton deleteAirport = new JButton("Delete");
        actionPanel.add(createAirport);
        actionPanel.add(modifyAirport);
        actionPanel.add(deleteAirport);
        tablePanel.add(actionPanel, BorderLayout.SOUTH);

        // Create button action listener
        createAirport.addActionListener(e -> {
            JTextField icaoField = new JTextField();
            JTextField radioFrequencyField = new JTextField();
            JTextField portFuelTypeField = new JTextField();
            JTextField latitudeField = new JTextField();
            JTextField longitudeField = new JTextField();
            JTextField portNameField = new JTextField();

            Object[] message = {
                    "ICAO:", icaoField,
                    "Radio Frequency:", radioFrequencyField,
                    "Fuel Type:", portFuelTypeField,
                    "Latitude:", latitudeField,
                    "Longitude:", longitudeField,
                    "Name:", portNameField,
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Create Airport", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String icao = icaoField.getText().toUpperCase();
                    double radioFrequency = Double.parseDouble(radioFrequencyField.getText());
                    String fuelType = portFuelTypeField.getText();
                    double latitude = Double.parseDouble(latitudeField.getText());
                    double longitude = Double.parseDouble(longitudeField.getText());
                    String name = portNameField.getText();

                    // Additional validation checks can be added as per the business logic
                    if (icao.length() != 4 || !icao.matches("^[A-Z]{4}$")) {
                        JOptionPane.showMessageDialog(null, "ICAO must be 4 uppercase letters.");
                        return;
                    }
                    if (radioFrequency <= 0) {
                        JOptionPane.showMessageDialog(null, "Radio frequency must be positive.");
                        return;
                    }
                    if (latitude < -90 || latitude > 90) {
                        JOptionPane.showMessageDialog(null, "Latitude must be between -90 and 90.");
                        return;
                    }
                    if (longitude < -180 || longitude > 180) {
                        JOptionPane.showMessageDialog(null, "Longitude must be between -180 and 180.");
                        return;
                    }
                    if (name.trim().isEmpty() || name.length() > 36) {
                        JOptionPane.showMessageDialog(null, "Name cannot be empty or exceed 36 characters.");
                        return;
                    }

                    Airport newAirport = new Airport(icao, radioFrequency, "VHF", fuelType, latitude, longitude, name);
                    AirportManager.getInstance().createAirport(newAirport);
                    // Update table model here to reflect new airport

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter valid numerical values for radio frequency, latitude, and longitude.");
                }
            }
        });

        // Modify button action listener
        modifyAirport.addActionListener(e -> {
            int selectedRow = airportsTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Assuming the first column holds the ICAO code
                String icao = (String) airportsTable.getValueAt(selectedRow, 0);
                Airport airportToModify = AirportManager.getInstance().searchAirport(icao);

                if (airportToModify != null) {
                    // Fields for modification, excluding ICAO
                    JTextField radioFrequencyField = new JTextField(
                            String.valueOf(airportToModify.getRadioFrequency()));
                    JTextField fuelTypeField = new JTextField(airportToModify.getFuelType());
                    JTextField latitudeField = new JTextField(String.valueOf(airportToModify.getLatitude()));
                    JTextField longitudeField = new JTextField(String.valueOf(airportToModify.getLongitude()));
                    JTextField nameField = new JTextField(airportToModify.getName());

                    Object[] message = {
                            "Radio Frequency:", radioFrequencyField,
                            "Fuel Type:", fuelTypeField,
                            "Latitude:", latitudeField,
                            "Longitude:", longitudeField,
                            "Name:", nameField,
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, "Modify Airport",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        try {
                            double radioFrequency = Double.parseDouble(radioFrequencyField.getText());
                            String fuelType = fuelTypeField.getText();
                            double latitude = Double.parseDouble(latitudeField.getText());
                            double longitude = Double.parseDouble(longitudeField.getText());
                            String name = nameField.getText();

                            // Validation checks
                            if (radioFrequency < 30 || radioFrequency > 300) {
                                JOptionPane.showMessageDialog(null, "Radio frequency must be positive and between 30 - 300.");
                                return;
                            }
                            if (latitude < -90 || latitude > 90) {
                                JOptionPane.showMessageDialog(null, "Latitude must be between -90 and 90.");
                                return;
                            }
                            if (longitude < -180 || longitude > 180) {
                                JOptionPane.showMessageDialog(null, "Longitude must be between -180 and 180.");
                                return;
                            }
                            if (name.trim().isEmpty() || name.length() > 36) {
                                JOptionPane.showMessageDialog(null, "Name cannot be empty or exceed 36 characters.");
                                return;
                            }

                            // Update the airport details and the manager
                            airportToModify.setRadioFrequency(radioFrequency);
                            airportToModify.setFuelType(fuelType);
                            airportToModify.setLatitude(latitude);
                            airportToModify.setLongitude(longitude);
                            airportToModify.setName(name);
                            // Since we are updating the airport directly, no need to call modifyAirport
                            // here

                            // Update the table model here to reflect the changes
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Please enter valid numerical values for radio frequency, latitude, and longitude.");
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select an airport to modify.");
            }
        });

        // Delete button action listener
        deleteAirport.addActionListener(e -> {
            // Delete the selected airport from the system
            int selectedRow = airportsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String icao = (String) airportsTable.getValueAt(selectedRow, 0);
                Airport airportToDelete = AirportManager.getInstance().searchAirport(icao);
                if (airportToDelete != null) {
                    AirportManager.getInstance().deleteAirport(airportToDelete);
                    // Update table model here to reflect deletion
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select an airport to delete.");
            }
        });

        return tablePanel;
    }

    // Method to create the airport graph panel
    private JPanel createAirportGraph() {
        JPanel graphPanel = new JPanel();
        // Graph to display the airports
        return graphPanel;
    }

    // Method to create the airplanes panel
    private JPanel AirplanesPanel() {
        JPanel panel = new JPanel();
        // Components for creating, modifying, deleting, displaying, or searching
        // airplanes
        // Table to display the list of airplanes
        return panel;
    }

    // Method to create the flight paths panel
    private JPanel FlightPathsPanel() {
        JPanel panel = new JPanel();
        // Components for creating, modifying, deleting, displaying, or searching flight
        // paths
        // Table to display the list of flight paths
        return panel;
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Show the disclaimer message before showing the main GUI
            JOptionPane.showMessageDialog(
                    null,
                    "THIS SOFTWARE IS NOT TO BE USED FOR FLIGHT PLANNING OR NAVIGATIONAL PURPOSE",
                    "WARNING!",
                    JOptionPane.WARNING_MESSAGE);
            FlightSystemGUI gui = new FlightSystemGUI();
            gui.setVisible(true);
        });
    }
}
