/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: FlightPathPanel.java 
 * Description:  
 * This class is responsible for creating the FlightPath Panel for the flight system.
*/
package csci360flightsystem;

import javax.swing.*;
import java.awt.*;

public class FlightPathPanel extends JPanel {
    private JTable flightPathsTable;
    private JPanel graphPanel;

    public FlightPathPanel() {
        setLayout(new BorderLayout());
        initializeTable();
        add(new JScrollPane(flightPathsTable), BorderLayout.CENTER);
        initializeGraph();
        add(graphPanel, BorderLayout.EAST);
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    private void initializeTable() {
        // Implementation for flight paths
    }

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
