/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: AirplanePanel.java 
 * Description:  
 * This class is responsible for creating the Airplane Panel for the flight system.
*/
package csci360flightsystem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class AirplanePanel extends JPanel {

    // Instance variables
    private DefaultTableModel airplaneTableModel;
    private JTable airplanesTable;
    private Airplane selectedAirplane;
    private JTextField searchField = new JTextField(20);

    public AirplanePanel() {
        setLayout(new BorderLayout());
        add(createSearchPanel(), BorderLayout.NORTH);
        initializeTable();
        add(new JScrollPane(airplanesTable), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    // Method to create the search panel
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
        String[] columnNames = { "Airspeed", "Fuel Burn", "Fuel Capacity", "Fuel Type", "Key", "Make", "Model",
                "Type" };
        airplaneTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        airplanesTable = new JTable(airplaneTableModel);
        airplanesTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
        loadAirplanesData();

        airplanesTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && airplanesTable.getSelectedRow() != -1) {
                int modelRow = airplanesTable.convertRowIndexToModel(airplanesTable.getSelectedRow());
                int Key = (int) airplaneTableModel.getValueAt(modelRow, 4);
                selectedAirplane = AirplaneManager.getInstance().searchAirplane(Key);
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
                    loadAirplanesData();
                } else {
                    performSearch();
                }
            }
        });
    }

    // Method to perform a search
    private void performSearch() {
        String searchText = searchField.getText().trim();
        airplaneTableModel.setRowCount(0); // Clear existing data

        Vector<Airplane> airplanes = AirplaneManager.getInstance().getAirplanes();
        for (Airplane airplane : airplanes) {
            String keyString = String.valueOf(airplane.getKey());
            if (keyString.contains(searchText)) {
                Object[] rowData = {
                        airplane.getAirspeed(),
                        airplane.getFuelBurn(),
                        airplane.getFuelCapacity(),
                        airplane.getFuelType(),
                        airplane.getKey(),
                        airplane.getMake(),
                        airplane.getModel(),
                        airplane.getType()
                };
                airplaneTableModel.addRow(rowData);
            }
        }
    }

    // Method to load airplane data into the table
    private void loadAirplanesData() {
        airplaneTableModel.setRowCount(0); // Clear existing data

        Vector<Airplane> airplanes = AirplaneManager.getInstance().getAirplanes();
        for (Airplane airplane : airplanes) {
            Object[] rowData = {
                    airplane.getAirspeed(),
                    airplane.getFuelBurn(),
                    airplane.getFuelCapacity(),
                    airplane.getFuelType(),
                    airplane.getKey(),
                    airplane.getMake(),
                    airplane.getModel(),
                    airplane.getType()
            };
            airplaneTableModel.addRow(rowData);
        }
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        JButton createButton = new JButton("Create");
        JButton modifyButton = new JButton("Modify");
        JButton deleteButton = new JButton("Delete");

        createButton.addActionListener(e -> createAirplane());
        modifyButton.addActionListener(e -> modifyAirplane());
        deleteButton.addActionListener(e -> deleteAirplane());

        panel.add(createButton);
        panel.add(modifyButton);
        panel.add(deleteButton);
        return panel;
    }

    // Method to create an airplane
    private void createAirplane() {
        JTextField airspeedField = new JTextField();
        JTextField fuelBurnField = new JTextField();
        JTextField fuelCapacityField = new JTextField();
        JTextField fuelTypeField = new JTextField();
        JTextField keyField = new JTextField();
        JTextField makeField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField typeField = new JTextField();

        Object[] message = {
                "Airspeed:", airspeedField,
                "Fuel Burn:", fuelBurnField,
                "Fuel Capacity:", fuelCapacityField,
                "Fuel Type:", fuelTypeField,
                "Key:", keyField,
                "Make:", makeField,
                "Model:", modelField,
                "Type:", typeField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Create New Airplane", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double airspeed = Double.parseDouble(airspeedField.getText());
                double fuelBurn = Double.parseDouble(fuelBurnField.getText());
                double fuelCapacity = Double.parseDouble(fuelCapacityField.getText());
                String fuelType = fuelTypeField.getText();
                int key = Integer.parseInt(keyField.getText());
                String make = makeField.getText();
                String model = modelField.getText();
                String type = typeField.getText();

                // Create and add the new airplane
                Airplane newAirplane = new Airplane(airspeed, fuelBurn, fuelCapacity, fuelType, key, make, model, type);
                AirplaneManager.getInstance().createAirplane(newAirplane);
                loadAirplanesData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numerical values for airspeed, fuel burn, fuel capacity, and key");
            }
        }
    }

    // Method to modify an airplane
    private void modifyAirplane() {
        if (selectedAirplane == null) {
            JOptionPane.showMessageDialog(this, "Please select an airplane to modify.");
            return;
        }

        // Populating fields with selected airplane's data
        JTextField airspeedField = new JTextField(String.valueOf(selectedAirplane.getAirspeed()));
        JTextField fuelBurnField = new JTextField(String.valueOf(selectedAirplane.getFuelBurn()));
        JTextField fuelCapacityField = new JTextField(String.valueOf(selectedAirplane.getFuelCapacity()));
        JTextField fuelTypeField = new JTextField(selectedAirplane.getFuelType());
        JTextField makeField = new JTextField(selectedAirplane.getMake());
        JTextField modelField = new JTextField(selectedAirplane.getModel());
        JTextField typeField = new JTextField(selectedAirplane.getType());
        JTextField keyField = new JTextField(String.valueOf(selectedAirplane.getKey()));
        keyField.setEditable(false); // Key is not editable

        Object[] message = {
                "Airspeed:", airspeedField,
                "Fuel Burn:", fuelBurnField,
                "Fuel Capacity:", fuelCapacityField,
                "Fuel Type:", fuelTypeField,
                "Make:", makeField,
                "Model:", modelField,
                "Type:", typeField,
                "Key (not editable):", keyField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Modify Airplane", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Updating airplane details
            try {
                double airspeed = Double.parseDouble(airspeedField.getText());
                double fuelBurn = Double.parseDouble(fuelBurnField.getText());
                double fuelCapacity = Double.parseDouble(fuelCapacityField.getText());
                String fuelType = fuelTypeField.getText();
                String make = makeField.getText();
                String model = modelField.getText();
                String type = typeField.getText();

                selectedAirplane.setAirspeed(airspeed);
                selectedAirplane.setFuelBurn(fuelBurn);
                selectedAirplane.setFuelCapacity(fuelCapacity);
                selectedAirplane.setFuelType(fuelType);
                selectedAirplane.setMake(make);
                selectedAirplane.setModel(model);
                selectedAirplane.setType(type);

                AirplaneManager.getInstance().modifyAirplane(selectedAirplane.getKey(), selectedAirplane);
                loadAirplanesData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numerical values.");
            }
        }
    }

    // Method to delete an airplane
    private void deleteAirplane() {
        if (selectedAirplane == null) {
            JOptionPane.showMessageDialog(this, "Please select an airplane to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected airplane?", "Delete Airplane",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            AirplaneManager.getInstance().deleteAirplane(selectedAirplane);
            loadAirplanesData();
            selectedAirplane = null; // Reset the selection
        }
    }

}
