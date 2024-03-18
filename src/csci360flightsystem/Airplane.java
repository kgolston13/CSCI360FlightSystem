/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: Airplane.java 
 * Description:  
 * This file contains all the attributes and constructors necessary to create an airplane object.
*/
package csci360flightsystem;

public class Airplane {
    // Attributes of the Airplane class
    // airspeed, fuelBurn, fuelCapacity, fuelType, key, make, model, type

    // The speed of an aircraft, represented by type double.
    public double airspeed;
    // The rate at which an aircraft consumes fuel, represented by type double.
    public double fuelBurn;
    // The amount of fuel an aircraft can hold, represented by type double.
    public double fuelCapacity;
    // The type of fuel an airplane uses, represented by type string
    public String fuelType;
    // The unique identifier of an airplane, represented by type int.
    public int key;
    // The make of an airplane, represented by type string.
    public String make;
    // The model of an airplane, represented by type string.
    public String model;
    // The type of an airplane, represented by type string.
    public String type;

    // Constructors for the Airplane class
    public Airplane(double airspeed, double fuelBurn, double fuelCapacity, String fuelType, int key, String make,
            String model, String type) {
        this.airspeed = airspeed;
        this.fuelBurn = fuelBurn;
        this.fuelCapacity = fuelCapacity;
        this.fuelType = fuelType;
        this.key = key;
        this.make = make;
        this.model = model;
        this.type = type;
    }

    // Getters and setters for the Airplane class
    public double getAirspeed() {
        return airspeed;
    }

    // airspeed cannot be negative
    public void setAirspeed(double airspeed) {
        if (airspeed > 0) {
            this.airspeed = airspeed;
        } else {
            throw new IllegalArgumentException("Airspeed must be positive");
        }
    }

    public double getFuelBurn() {
        return fuelBurn;
    }

    // fuelBurn cannot be negative
    public void setFuelBurn(double fuelBurn) {
        if (fuelBurn > 0) {
            this.fuelBurn = fuelBurn;
        } else {
            throw new IllegalArgumentException("Fuel burn cannot be negative");
        }
    }

    public double getFuelCapacity() {
        return fuelCapacity;
    }

    // fuelCapacity cannot be negative
    public void setFuelCapacity(double fuelCapacity) {
        if (fuelCapacity > 0) {
            this.fuelCapacity = fuelCapacity;
        } else {
            throw new IllegalArgumentException("Fuel capacity cannot be negative");
        }
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public int getKey() {
        return key;
    }

    // key cannot be negative and must be unique
    public void setKey(int key) {
        if (key >= 0) {
            this.key = key;
        } else {
            throw new IllegalArgumentException("Key cannot be negative");
        }
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // equals method to compare two airplanes
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Airplane))
            return false;
        Airplane airplane = (Airplane) obj;
        return key == airplane.key;
    }

    // hashCode method to return the hash code of the key attribute
    @Override
    public int hashCode() {
        return key;
    }

    // toString method to return a string representation of an airplane
    @Override
    public String toString() {
        return "Airplane [airspeed=" + airspeed + ", fuelBurn=" + fuelBurn + ", fuelCapacity=" + fuelCapacity
                + ", fuelType=" + fuelType + ", key=" + key + ", make=" + make + ", model=" + model + ", type=" + type
                + "]";
    }

}
