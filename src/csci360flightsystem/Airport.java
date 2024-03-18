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
    //ICAO must be 4 characters long. Must start with C for Canada or K for the US
    public void setICAO(String ICAO) {
        if (ICAO.length() == 4 && (ICAO.charAt(0) == 'C' || ICAO.charAt(0) == 'K')) {
            this.ICAO = ICAO;
        } else {
            throw new IllegalArgumentException("ICAO must be 4 characters long and start with C for Canada or K for the US.");
        }
    }
    public double getRadioFrequency() {
        return radioFrequency;
    }
    //radioFrequency must be between 30 and 300 as per VHF
    public void setRadioFrequency(double radioFrequency) {
        if (radioFrequency >= 30 && radioFrequency <= 300) {
            this.radioFrequency = radioFrequency;
        } else {
            throw new IllegalArgumentException("Radio frequency must be between 30 and 300 MHz for VHF");
        }
    }    
    public String getRadioType() {
        return radioType;
    }
    //radioType must be VHF
    public void setRadioType(String radioType) {
        this.radioType = "VHF";
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
    //latitude must be between -90 and 90
    public void setLatitude(double latitude) {
        if (latitude >= -90 && latitude <= 90) {
            this.latitude = latitude;
        } else {
            throw new IllegalArgumentException("Latitude must be between -90 and 90.");
        }
    }
    public double getLongitude() {
        return longitude;
    }
    //longitude must be between -180 and 180
    public void setLongitude(double longitude) {
        if (longitude >= -180 && longitude <= 180) {
            this.longitude = longitude;
        } else {
            throw new IllegalArgumentException("Longitude must be between -180 and 180.");
        }
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
