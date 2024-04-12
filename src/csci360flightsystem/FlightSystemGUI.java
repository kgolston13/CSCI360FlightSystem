/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: FlightSystemGUI.java 
 * Description:  
 * This class is responsible for creating the GUI for the flight system.
*/
package csci360flightsystem;

import javax.swing.*;
import java.awt.*;

public class FlightSystemGUI extends JFrame {
	
	// Instance variables
	private static final long serialVersionUID = 1L;
    // Panels for different parts of the system
    private AirportPanel airportPanel = new AirportPanel();
    private AirplanePanel airplanePanel = new AirplanePanel();
    private FlightPathPanel flightPathPanel = new FlightPathPanel();

    // Main panel to hold different panels
    private JPanel mainPanel;

    // Constructor to initialize the GUI
    public FlightSystemGUI() {
        setTitle("CSCI 360 Flight System");
        setSize(800, 600); // Set the initial size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(createMenuBar());

        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(airportPanel, "Airports");
        mainPanel.add(airplanePanel, "Airplanes");
        mainPanel.add(flightPathPanel, "FlightPaths");
        setContentPane(mainPanel);
        showPanel("Airports"); // Start with the Airport panel visible
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
