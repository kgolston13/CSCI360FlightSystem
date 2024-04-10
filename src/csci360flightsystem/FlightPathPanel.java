/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: FlightPathPanel.java 
 * Description:  
 * This class is responsible for creating the FlightPath Panel for the flight system.
*/
package csci360flightsystem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class FlightPathPanel extends JPanel {

    // Instance variables
    private DefaultTableModel flightPathTableModel;
    private JTable flightPathsTable;
    private FlightPath selectedFlightPath;
    private JTextField searchField = new JTextField(20);
    private JPanel cardPanel;
    private JPanel graphPanel;
    private CardLayout cardLayout;

    // Constructor
    public FlightPathPanel() {
        setLayout(new BorderLayout());
        initializeToggleButtons();
        initializeCardPanel();
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    // Initialize the toggle buttons for switching between table and graph view
    private void initializeToggleButtons() {
        JPanel togglePanel = new JPanel();
        JToggleButton tableViewButton = new JToggleButton("Table View", true);
        JToggleButton graphViewButton = new JToggleButton("Graph View");

        tableViewButton.addActionListener(e -> cardLayout.show(cardPanel, "Table"));
        graphViewButton.addActionListener(e -> cardLayout.show(cardPanel, "Graph"));

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(tableViewButton);
        buttonGroup.add(graphViewButton);

        togglePanel.add(tableViewButton);
        togglePanel.add(graphViewButton);

        add(togglePanel, BorderLayout.NORTH);
    }

    // Initialize the card panel with table and graph views
    private void initializeCardPanel() {
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        JPanel tablePanel = new JPanel(new BorderLayout());
        initializeTable();
        tablePanel.add(new JScrollPane(flightPathsTable), BorderLayout.CENTER);
        tablePanel.add(createSearchPanel(), BorderLayout.NORTH);

        graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw flight paths on the graph
            }
        };

        initializeGraph(); // Call to initialize graph interactions

        cardPanel.add(tablePanel, "Table");
        cardPanel.add(graphPanel, "Graph");

        add(cardPanel, BorderLayout.CENTER);
    }

    // Create the search panel with a search field and search button
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(new JLabel("Key:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    // Initialize the table with flight paths data
    private void initializeTable() {
        String[] columnNames = { "Key", "Starting Airport", "Ending Airport", "Airplane" };
        flightPathTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        flightPathsTable = new JTable(flightPathTableModel);
        flightPathsTable.getTableHeader().setReorderingAllowed(false);
        loadFlightPathsData();

        flightPathsTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && flightPathsTable.getSelectedRow() != -1) {
                int modelRow = flightPathsTable.convertRowIndexToModel(flightPathsTable.getSelectedRow());
                int key = (Integer) flightPathTableModel.getValueAt(modelRow, 0);
                selectedFlightPath = FlightPath.getInstance().searchFlightPath(key);
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            public void removeUpdate(DocumentEvent e) {
                search();
            }

            public void insertUpdate(DocumentEvent e) {
                search();
            }

            public void search() {
                if (searchField.getText().isEmpty()) {
                    loadFlightPathsData();
                } else {
                    performSearch();
                }
            }
        });
    }

    // Search for a flight path by key
    private void performSearch() {
        String searchText = searchField.getText().trim();
        flightPathTableModel.setRowCount(0); // Clear existing data

        for (FlightPath flightPath : FlightPath.getInstance().getFlightPaths()) {
            String keyString = String.valueOf(flightPath.getKey());

            if (keyString.equals(searchText)) {
                Object[] rowData = {
                        flightPath.getKey(),
                        flightPath.getStartingAirport(),
                        flightPath.getEndingAirport(),
                        flightPath.getAirplane()
                };
                flightPathTableModel.addRow(rowData);
            }
        }
    }

    // Load flight paths data into the table model
    private void loadFlightPathsData() {
        flightPathTableModel.setRowCount(0); // Clear existing data in the table
        Vector<FlightPath> flightPaths = FlightPath.getInstance().getFlightPaths();
        // Iterate over each flight path and add it as a row to the table model
        for (FlightPath flightPath : flightPaths) {
            Object[] rowData = {
                    flightPath.getKey(),
                    flightPath.getStartingAirport(),
                    flightPath.getEndingAirport(),
                    flightPath.getAirplane()
            };
            flightPathTableModel.addRow(rowData);
        }
    }

    // Method to create the graph panel
    private void initializeGraph() {
        graphPanel = new JPanel() {
            Map<Rectangle, FlightPath> flightPathMap = new HashMap<>();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                flightPathMap.clear(); // Clear previous mappings

                // Drawing the graph axes
                int width = getWidth();
                int height = getHeight();

                // Draw Y-axis in the middle of the panel
                int yAxisX = width / 2;
                g.drawLine(yAxisX, 0, yAxisX, height);

                // Draw X-axis in the middle of the panel
                int xAxisY = height / 2;
                g.drawLine(0, xAxisY, width, xAxisY);

                // Label for Y-axis (Latitude)
                g.drawString("Latitude", yAxisX + 5, 15);

                // Label for X-axis (Longitude)
                g.drawString("Longitude", width - 70, xAxisY - 5);

                // Draw the flight paths
                if (selectedFlightPath != null) {
                    drawFlightPath(g, selectedFlightPath, getWidth(), getHeight());
                }
            }

            private void drawFlightPath(Graphics g, FlightPath flightPath, int width, int height) {
                // Ensure the flight path is selected
                if (flightPath == null)
                    return;

                // Populate the list of airports based on the flight path details
                List<Airport> airports = new ArrayList<>();
                airports.add(AirportManager.getInstance().searchAirport(flightPath.getStartingAirport()));
                for (String airportCode : flightPath.getMiddleAirports()) {
                    airports.add(AirportManager.getInstance().searchAirport(airportCode));
                }
                airports.add(AirportManager.getInstance().searchAirport(flightPath.getEndingAirport()));

                // Draw lines between consecutive airports
                for (int i = 0; i < airports.size() - 1; i++) {
                    Airport start = airports.get(i);
                    Airport end = airports.get(i + 1);

                    if (start == null || end == null)
                        continue;

                    int startX = (int) ((start.getLongitude() + 180) * (width / 360.0));
                    int startY = (int) ((-start.getLatitude() + 90) * (height / 180.0));
                    int endX = (int) ((end.getLongitude() + 180) * (width / 360.0));
                    int endY = (int) ((-end.getLatitude() + 90) * (height / 180.0));

                    g.drawLine(startX, startY, endX, endY);
                }
            }
        };
    }

    // Create the action panel with buttons to create, modify, delete and launch
    // flight paths
    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        JButton createButton = new JButton("Create");
        JButton modifyButton = new JButton("Modify");
        JButton deleteButton = new JButton("Delete");
        JButton launchButton = new JButton("Launch");

        createButton.addActionListener(e -> createFlightPath());
        modifyButton.addActionListener(e -> modifyFlightPath());
        deleteButton.addActionListener(e -> deleteFlightPath());
        launchButton.addActionListener(e -> launchFlightPath());

        panel.add(createButton);
        panel.add(modifyButton);
        panel.add(deleteButton);
        panel.add(launchButton);

        return panel;
    }

    // Create a new flight path
    private void createFlightPath() {
        JTextField keyField = new JTextField();
        JTextField startingAirportField = new JTextField();
        JTextField endingAirportField = new JTextField();
        JTextField airplaneKeyField = new JTextField();

        Object[] message = {
                "Key:", keyField,
                "Starting Airport:", startingAirportField,
                "Ending Airport:", endingAirportField,
                "Airplane Key:", airplaneKeyField,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Create New Flight Path",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int key = Integer.parseInt(keyField.getText());
                String startingAirport = startingAirportField.getText();
                String endingAirport = endingAirportField.getText();
                int airplaneKey = Integer.parseInt(airplaneKeyField.getText());

                Airplane airplane = AirplaneManager.getInstance().searchAirplane(airplaneKey);
                if (airplane == null) {
                    JOptionPane.showMessageDialog(this, "Invalid airplane key.");
                    return;
                }

                FlightPath newFlightPath = new FlightPath(key, startingAirport, endingAirport, airplane);
                FlightPath.getInstance().createFlightPath(newFlightPath);
                loadFlightPathsData(); // Refresh the table to show the new flight path
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter numerical values for keys.");
            }
        }
    }

    // Modify an existing flight path
    private void modifyFlightPath() {
        if (selectedFlightPath == null) {
            JOptionPane.showMessageDialog(this, "Please select a flight path to modify.");
            return;
        }

        JTextField keyField = new JTextField(String.valueOf(selectedFlightPath.getKey()));
        JTextField startingAirportField = new JTextField(selectedFlightPath.getStartingAirport());
        JTextField endingAirportField = new JTextField(selectedFlightPath.getEndingAirport());
        JTextField airplaneKeyField = new JTextField(
                selectedFlightPath.getAirplane() != null ? String.valueOf(selectedFlightPath.getAirplane().getKey())
                        : "");

        keyField.setEditable(false); // The key should not be editable

        Object[] message = {
                "Key (not editable):", keyField,
                "Starting Airport:", startingAirportField,
                "Ending Airport:", endingAirportField,
                "Airplane Key:", airplaneKeyField,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Modify Flight Path", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String startingAirport = startingAirportField.getText();
                String endingAirport = endingAirportField.getText();
                int airplaneKey = Integer.parseInt(airplaneKeyField.getText());

                Airplane airplane = AirplaneManager.getInstance().searchAirplane(airplaneKey);
                if (airplane == null) {
                    JOptionPane.showMessageDialog(this, "Invalid airplane key.");
                    return;
                }

                FlightPath updatedFlightPath = new FlightPath(selectedFlightPath.getKey(), startingAirport,
                        endingAirport, airplane);
                // Since the starting or ending airports might have changed, clear the middle
                // airports.
                updatedFlightPath.setMiddleAirports(new ArrayList<>());
                FlightPath.getInstance().modifyFlightPath(selectedFlightPath.getKey(), updatedFlightPath);
                loadFlightPathsData(); // Refresh the table to show the updated flight path
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid airplane key.");
            }
        }
    }

    // Delete an existing flight path
    private void deleteFlightPath() {
        if (selectedFlightPath == null) {
            JOptionPane.showMessageDialog(this, "Please select a flight path to delete.");
            return;
        }

        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected flight path?", "Confirm Deletion",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            FlightPath.getInstance().deleteFlightPath(selectedFlightPath.getKey());
            loadFlightPathsData();
            selectedFlightPath = null; // Clear the selection
        }
    }

    private void launchFlightPath() {
        if (selectedFlightPath == null) {
            JOptionPane.showMessageDialog(this, "Please select a flight path to launch.");
            return;
        }

        selectedFlightPath.launchFlight(selectedFlightPath);
        // Calculate total distance, time and get direction
        AirportManager airportManager = AirportManager.getInstance();
        Airport startingAirport = airportManager.searchAirport(selectedFlightPath.getStartingAirport());
        List<String> middleAirportsCodes = selectedFlightPath.getMiddleAirports();
        Airport endingAirport = airportManager.searchAirport(selectedFlightPath.getEndingAirport());

        StringBuilder radioFrequencies = new StringBuilder();
        radioFrequencies.append("Starting airport radio frequency: ").append(startingAirport.getRadioFrequency())
                .append("\n");
        for (String code : middleAirportsCodes) {
            Airport airport = airportManager.searchAirport(code);
            radioFrequencies.append("Middle airport (").append(code).append(") radio frequency: ")
                    .append(airport.getRadioFrequency()).append("\n");
        }
        radioFrequencies.append("Ending airport radio frequency: ").append(endingAirport.getRadioFrequency())
                .append("\n");

        double totalDistance = 0; // Total distance initialization
        Airport prevAirport = startingAirport;
        for (String airportCode : selectedFlightPath.getMiddleAirports()) {
            Airport nextAirport = airportManager.searchAirport(airportCode);
            if (nextAirport != null && prevAirport != null) {
                totalDistance += AirportManager.calculateDistance(prevAirport, nextAirport);
                prevAirport = nextAirport;
            }
        }
        // Add the distance from the last middle airport to the ending airport
        totalDistance += AirportManager.calculateDistance(prevAirport, endingAirport);

        double time = totalDistance / selectedFlightPath.getAirplane().getAirspeed();
        String direction = FlightPath.direction(FlightPath.calculateHeading(startingAirport, endingAirport));

        String message = "Flight path found. \nTotal distance: " + totalDistance + " km.\n" +
                "Estimated time: " + time + " hours.\n" +
                "Direction: " + direction + "\n" + radioFrequencies;

        JOptionPane.showMessageDialog(this, message);

    }
}