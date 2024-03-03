/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: Airport.java 
 * Description:  
 * This file contains all the attributes and constructors necessary to create an airport object.
*/ 
package csci360flightsystem;

public class Airport {
    //Attributes of the Airport class
    //ICAO, radioType, radioFrequency, fuelType, latitude, longitude, name

    //The International Civil Aviation Organization (ICAO) code of an airport, represented by type string.
    public String ICAO;
    //The radio frequency an airport uses, represented by type double.
    public double radioFrequency;
    //The type of radio frequency an airport uses, represented by type string.
    public String radioType;
    //The type of fuel an airport uses, represented by type string.
    public String fuelType;
    //The latitude of an airport, represented by type double.
    public double latitude;
    //The longitude of an airport, represented by type double.
    public double longitude;
    //The name of an airport, represented by type string.
    public String name;

    // Constructors for the Airport class
    public Airport(String ICAO,  double radioFrequency, String radioType, String fuelType, double latitude, double longitude, String name) {
        this.ICAO = ICAO;
        this.radioFrequency = radioFrequency;
        this.radioType = radioType;
        this.fuelType = fuelType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    // Getters and setters for the Airport class
    public String getICAO() {
        return ICAO;
    }
    public void setICAO(String ICAO) {
        this.ICAO = ICAO;
    }
    public double getRadioFrequency() {
        return radioFrequency;
    }
    public void setRadioFrequency(double radioFrequency) {
        this.radioFrequency = radioFrequency;
    }
    public String getRadioType() {
        return radioType;
    }
    public void setRadioType(String radioType) {
        this.radioType = radioType;
    }
    public String getFuelType() {
        return fuelType;
    }
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
