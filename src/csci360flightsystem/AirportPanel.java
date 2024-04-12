/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: AirportPanel.java 
 * Description:  
 * This class is responsible for creating the Airport Panel for the flight system.
*/
package csci360flightsystem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AirportPanel extends JPanel {

    // Instance variables
	private static final long serialVersionUID = 1L;
    private DefaultTableModel airportTableModel;
    private JTable airportsTable;
    private JPanel cardPanel;
    private JPanel graphPanel;
    private CardLayout cardLayout;
    private Airport selectedAirport;
    private JTextField searchField = new JTextField(20);

    // Constructor to initialize the Airport Panel
    public AirportPanel() {
        setLayout(new BorderLayout());
        initializeToggleButtons();
        initializeCardPanel();
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    // Method to initialize the toggle buttons
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

    private void initializeCardPanel() {
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // Create the table panel and add the table and search panel to it
        JPanel tablePanel = new JPanel(new BorderLayout());
        initializeTable();
        tablePanel.add(new JScrollPane(airportsTable), BorderLayout.CENTER);
        tablePanel.add(createSearchPanel(), BorderLayout.NORTH); // Add search panel only to the table panel

        initializeGraph();

        cardPanel.add(tablePanel, "Table");
        cardPanel.add(graphPanel, "Graph");

        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(new JLabel("ICAO:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    private void initializeTable() {
        String[] columnNames = { "ICAO", "Radio Frequency", "Fuel Type", "Latitude", "Longitude", "Name" };
        airportTableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        airportsTable = new JTable(airportTableModel);
        airportsTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
        loadAirportsData();

        airportsTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && airportsTable.getSelectedRow() != -1) {
                int modelRow = airportsTable.convertRowIndexToModel(airportsTable.getSelectedRow());
                String icao = (String) airportTableModel.getValueAt(modelRow, 0);
                selectedAirport = AirportManager.getInstance().searchAirport(icao);
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
                    loadAirportsData();
                } else {
                    performSearch();
                }
            }
        });
    }

    // Method to perform a search
    private void performSearch() {
        String searchText = searchField.getText().trim().toUpperCase();
        airportTableModel.setRowCount(0); // Clear existing data

        Vector<Airport> airports = AirportManager.getInstance().getAirports();
        for (Airport airport : airports) {
            if (airport.getICAO().contains(searchText)) {
                Object[] rowData = {
                        airport.getICAO(),
                        airport.getRadioFrequency(),
                        airport.getFuelType(),
                        airport.getLatitude(),
                        airport.getLongitude(),
                        airport.getName()
                };
                airportTableModel.addRow(rowData);
            }
        }
    }

    // Method to load airport data into the table
    private void loadAirportsData() {
        airportTableModel.setRowCount(0); // Clear existing data
        Vector<Airport> airports = AirportManager.getInstance().getAirports();
        for (Airport airport : airports) {
            Object[] rowData = new Object[] {
                    airport.getICAO(),
                    airport.getRadioFrequency(),
                    airport.getFuelType(),
                    airport.getLatitude(),
                    airport.getLongitude(),
                    airport.getName()
            };
            airportTableModel.addRow(rowData);
        }
    }

    // Method to initialize the graph panel
    private void initializeGraph() {
        graphPanel = new JPanel() {
            private static final long serialVersionUID = 1L;
			Map<Rectangle, Airport> airportMap = new HashMap<>();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                airportMap.clear(); // Clear previous mappings

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

                // Draw the airports
                int dotDiameter = 10; // Increased dot size for better interaction
                for (Airport airport : AirportManager.getInstance().getAirports()) {
                    // Transform longitude and latitude to x and y coordinates
                    int x = (int) ((airport.getLongitude() + 180) * (width / 360.0)) - dotDiameter / 2;
                    int y = (int) ((-airport.getLatitude() + 90) * (height / 180.0)) - dotDiameter / 2;
                    g.fillOval(x, y, dotDiameter, dotDiameter); // Draw the airport as a larger dot
                    airportMap.put(new Rectangle(x, y, dotDiameter, dotDiameter), airport);
                }
            }
        };
    }

    // Method to create the action panel
    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        JButton createButton = new JButton("Create");
        JButton modifyButton = new JButton("Modify");
        JButton deleteButton = new JButton("Delete");

        createButton.addActionListener(e -> createAirport());
        modifyButton.addActionListener(e -> modifyAirport());
        deleteButton.addActionListener(e -> deleteAirport());

        panel.add(createButton);
        panel.add(modifyButton);
        panel.add(deleteButton);
        return panel;
    }

    // Method to create a new airport
    private void createAirport() {
        JTextField icaoField = new JTextField();
        JTextField radioFrequencyField = new JTextField();
        JTextField fuelTypeField = new JTextField();
        JTextField latitudeField = new JTextField();
        JTextField longitudeField = new JTextField();
        JTextField nameField = new JTextField();

        Object[] message = {
                "ICAO:", icaoField,
                "Radio Frequency:", radioFrequencyField,
                "Fuel Type:", fuelTypeField,
                "Latitude:", latitudeField,
                "Longitude:", longitudeField,
                "Name:", nameField,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Create New Airport", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String icao = icaoField.getText();
                double radioFrequency = Double.parseDouble(radioFrequencyField.getText());
                String fuelType = fuelTypeField.getText();
                double latitude = Double.parseDouble(latitudeField.getText());
                double longitude = Double.parseDouble(longitudeField.getText());
                String name = nameField.getText();

                // Create and add the new airport
                Airport newAirport = new Airport(icao, radioFrequency, "VHF", fuelType, latitude, longitude, name);
                AirportManager.getInstance().createAirport(newAirport);
                loadAirportsData();
                graphPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numerical values for radio frequency, latitude, and longitude.");
            }
        }
    }

    // Method to modify an existing airport
    private void modifyAirport() {
        if (selectedAirport == null) {
            JOptionPane.showMessageDialog(this, "Please select an airport to modify.");
            return;
        }

        JTextField icaoField = new JTextField(selectedAirport.getICAO());
        JTextField radioFrequencyField = new JTextField(String.valueOf(selectedAirport.getRadioFrequency()));
        JTextField fuelTypeField = new JTextField(selectedAirport.getFuelType());
        JTextField latitudeField = new JTextField(String.valueOf(selectedAirport.getLatitude()));
        JTextField longitudeField = new JTextField(String.valueOf(selectedAirport.getLongitude()));
        JTextField nameField = new JTextField(selectedAirport.getName());

        icaoField.setEditable(false); // ICAO should not be editable

        Object[] message = {
                "ICAO:", icaoField,
                "Radio Frequency:", radioFrequencyField,
                "Fuel Type:", fuelTypeField,
                "Latitude:", latitudeField,
                "Longitude:", longitudeField,
                "Name:", nameField,
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Modify Airport", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double radioFrequency = Double.parseDouble(radioFrequencyField.getText());
                String fuelType = fuelTypeField.getText();
                double latitude = Double.parseDouble(latitudeField.getText());
                double longitude = Double.parseDouble(longitudeField.getText());
                String name = nameField.getText();

                // Update the airport details
                selectedAirport.setRadioFrequency(radioFrequency);
                selectedAirport.setFuelType(fuelType);
                selectedAirport.setLatitude(latitude);
                selectedAirport.setLongitude(longitude);
                selectedAirport.setName(name);

                AirportManager.getInstance().modifyAirport(selectedAirport.getICAO(), selectedAirport);
                loadAirportsData();
                graphPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numerical values for radio frequency, latitude, and longitude.");
            }
        }
    }

    // Method to delete an existing airport
    private void deleteAirport() {
        if (selectedAirport == null) {
            JOptionPane.showMessageDialog(this, "Please select an airport to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete " + selectedAirport.getName() + "?", "Delete Airport",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            AirportManager.getInstance().deleteAirport(selectedAirport);
            loadAirportsData();
            graphPanel.repaint();
            selectedAirport = null; // Clear the selection
        }
    }

}
