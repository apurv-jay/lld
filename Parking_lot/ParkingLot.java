package com.Parking_lot;
import com.sun.source.tree.Tree;

import java.util.*;

enum Type{
    car,bike;
}
class Vehicle{
    private Type typeOfVehicle;
    private String id;
    Vehicle(Type typeOfVehicle, String id)
    {
        this.id=id;
        this.typeOfVehicle=typeOfVehicle;
    }
    Type getTypeOfVehicle(){
        return this.typeOfVehicle;
    }
    String getId(){
        return this.id;
    }
}

class ParkingSpot{
    private Type parkingSpotType;
    private Vehicle vehicle;
    private boolean isAvailable;
    private int spotId;
    ParkingSpot(Type parkingSpotType,Vehicle vehicle,boolean isAvailable,int spotId){
        this.isAvailable = isAvailable;
        this.parkingSpotType = parkingSpotType;
        this.spotId = spotId;
        this.vehicle = vehicle;
    }

    public Type getParkingSpotType() {
        return parkingSpotType;
    }

    public void setParkingSpotType(Type parkingSpotType) {
        this.parkingSpotType = parkingSpotType;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }
}

class Floor{
    private int floorNumber;
    private TreeMap<Integer,List<ParkingSpot>> parking= new TreeMap<>();
    void addToParking(int floorNumber,ParkingSpot parkingSpot){
        if (!parking.containsKey(floorNumber)) {
            parking.put(floorNumber, new ArrayList<>());
        }
        parking.get(floorNumber).add(parkingSpot);
    }

}

public class ParkingLot {
    public static void main(String [] args){
        System.out.println("HELLO WORLD");
    }
}





























