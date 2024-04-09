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
import java.util.Vector;

public class FlightPathPanel extends JPanel {

    // Instance variables
    private DefaultTableModel flightPathTableModel;
    private JTable flightPathsTable;
    private JPanel cardPanel;
    private JPanel graphPanel;
    private CardLayout cardLayout;
    private FlightPath selectedFlightPath;
    private JTextField searchField = new JTextField(20);

    // Constructor to initialize the FlightPathPanel
    public FlightPathPanel() {
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

    // Method to initialize the card panel
    private void initializeCardPanel() {
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // Create the table panel and add the table and search panel to it
        JPanel tablePanel = new JPanel(new BorderLayout());
        initializeTable();
        tablePanel.add(new JScrollPane(flightPathsTable), BorderLayout.CENTER);
        tablePanel.add(createSearchPanel(), BorderLayout.NORTH); // Add search panel only to the table panel

        initializeGraph();

        cardPanel.add(tablePanel, "Table");
        cardPanel.add(graphPanel, "Graph");

        add(cardPanel, BorderLayout.CENTER);
    }

    //Method to create search panel
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(new JLabel("Key:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    // Method to initialize the table
    private void initializeTable() {
        String[] columnNames = { "Key", "Starting Airport", "Middle Airports", "Ending Airport", "Airplane", "Heading", "Distance", "Time", "Possibility" };
        flightPathTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        flightPathsTable = new JTable(flightPathTableModel);
        flightPathsTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
        loadFlightPathsData();

        flightPathsTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && flightPathsTable.getSelectedRow() != -1) {
                int modelRow = flightPathsTable.convertRowIndexToModel(flightPathsTable.getSelectedRow());
                int Key = (int) flightPathTableModel.getValueAt(modelRow, 0);
                selectedFlightPath = FlightPath.searchFlightPath(Key);
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

    // Method to perform a search
    private void performSearch() {
        String searchText = searchField.getText().trim().toUpperCase();
        flightPathTableModel.setRowCount(0); // Clear existing data

        Vector<FlightPath> flightPaths = flightPaths.getFlightPaths();
        for (FlightPath flightPath : flightPaths) {
            if (flightPath.getKey().contains(searchText)) {
                Object[] rowData = {
                        flightPath.getKey(),
                        flightPath.getStartingAirport(),
                        flightPath.getMiddleAirports(),
                        flightPath.getEndingAirport(),
                        flightPath.getAirplane(),
                        flightPath.getHeading(),
                        flightPath.getDistance(),
                        flightPath.getTime(),
                        flightPath.getPossibility()
                };
                flightPathTableModel.addRow(rowData);
            }
        }
    }

    // Method to initialize the graph
    private void initializeGraph() {
        graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the flight paths on the graph
            }
        };
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        JButton createButton = new JButton("Create");
        JButton modifyButton = new JButton("Modify");
        JButton deleteButton = new JButton("Delete");
        // Add action listeners for these buttons
        panel.add(createButton);
        panel.add(modifyButton);
        panel.add(deleteButton);
        return panel;
    }
}
