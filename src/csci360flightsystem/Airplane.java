/* Programmers: Group 7 (Appa, Jacob, Keenan, and Lance)
 * Program file name: Airplane.java 
 * Description:  
 * This file contains all the attributes and constructors necessary to create an airplane object.
*/ 
package csci360flightsystem;

public class Airplane {
    //Attributes of the Airplane class
    //airspeed, fuelBurn, fuelCapacity, fuelType, key, make, model, type

    //The speed of an aircraft, represented by type double.
    public double airspeed;
    //The rate at which an aircraft consumes fuel, represented by type double.
    public double fuelBurn;
    //The amount of fuel an aircraft can hold, represented by type double.
    public double fuelCapacity;
    //The type of fuel an airplane uses, represented by type string
    public double fuelType;
    //The unique identifier of an airplane, represented by type int.
    public int key;
    //The make of an airplane, represented by type string.
    public String make;
    //The model of an airplane, represented by type string.
    public String model;
    //The type of an airplane, represented by type string.
    public String type;

    // Constructors for the Airplane class
    public Airplane(double airspeed, double fuelBurn, double fuelCapacity, double fuelType, int key, String make, String model, String type) {
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
    public void setAirspeed(double airspeed) {
        this.airspeed = airspeed;
    }
    public double getFuelBurn() {
        return fuelBurn;
    }
    public void setFuelBurn(double fuelBurn) {
        this.fuelBurn = fuelBurn;
    }
    public double getFuelCapacity() {
        return fuelCapacity;
    }
    public void setFuelCapacity(double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }
    public double getFuelType() {
        return fuelType;
    }
    public void setFuelType(double fuelType) {
        this.fuelType = fuelType;
    }
    public int getKey() {
        return key;
    }
    public void setKey(int key) {
        this.key = key;
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
}
