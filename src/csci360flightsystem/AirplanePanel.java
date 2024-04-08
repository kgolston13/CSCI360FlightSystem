/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: AirplanePanel.java 
 * Description:  
 * This class is responsible for creating the Airplane Panel for the flight system.
*/
package csci360flightsystem;

import javax.swing.*;
import java.awt.*;

public class AirplanePanel extends JPanel {
    private JTable airplanesTable;

    public AirplanePanel() {
        setLayout(new BorderLayout());
        initializeTable();
        add(new JScrollPane(airplanesTable), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    private void initializeTable() {
        // Similar to AirportPanel but for airplanes
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
