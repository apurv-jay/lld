package com.uber;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Location{
    int start_location;
    int end_location;

    public Location(int start_location, int end_location) {
        this.start_location = start_location;
        this.end_location = end_location;
    }
    int distance(){
        return Math.abs(start_location-end_location);
    }
}
class Rider{
    String riderName;
    Location riderLocation;

    public Rider(String riderName, Location location) {
        this.riderName = riderName;
        this.riderLocation = location;
    }
}

class Driver{
    String driverName;
    Location driverLocation;
    public Driver(String driverName, Location driverLocation)
    {
        this.driverName = driverName;
        this.driverLocation=driverLocation;
    }
}
interface FindDriver{
    Driver theChosenDriver(List<Driver>driver,Rider rider);
}
class CloseByDriver implements FindDriver{
    @Override
    public Driver theChosenDriver(List<Driver> driver, Rider rider) {
        Driver driverval = null;
        int mini=Integer.MAX_VALUE;
        int riderDistance = rider.riderLocation.distance();
        for(Driver driver1:driver){
            Location driverLocation = driver1.driverLocation;
            int driverDistance=driverLocation.distance();
            int val = Math.abs(riderDistance-driverDistance);
            if(val < mini)
            {
                mini = val;
                driverval = driver1;
            }
        }
        return driverval;
    }
}

interface PricingStrategy{
    double priceOfRide(Location pickoff, Location drop);
}

class BasicPay implements PricingStrategy{
    @Override
    public double priceOfRide(Location pickoff, Location drop) {
        int pickOffPoint = pickoff.distance();
        int dropOff = drop.distance();
        return Math.abs(dropOff-pickOffPoint);
    }
}
class DifficultPay implements PricingStrategy{
    @Override
    public double priceOfRide(Location pickoff, Location drop) {
        int pickOffPoint = pickoff.distance();
        int dropOff = drop.distance();
        return Math.abs(dropOff-pickOffPoint)+100;
    }
}

class UberManager{
    List<Driver>driverList = new ArrayList<>();
    void addToDriverList(Driver driver) {
        driverList.add(driver);
    }
    Driver findDriver(Rider rider)
    {
        FindDriver object = new CloseByDriver();
        Driver driver = object.theChosenDriver(driverList,rider);
        return driver;
    }
    double findPrice(Rider rider,Driver driver)
    {
        PricingStrategy object1 = new BasicPay();
        double price = object1.priceOfRide(rider.riderLocation,driver.driverLocation);
        return price;
    }

}


public class UberSystem {
    public static void main(String[] args)
    {
        System.out.println("Enter Number of Driver you want");
        UberManager uberManager = new UberManager();
        Scanner sc = new Scanner(System.in);
        int driverNumber = sc.nextInt();
        for(int i=0;i<driverNumber;i++)
        {
            System.out.println("enter driver name and driver location");
            String name = sc.next();
            int x= sc.nextInt();
            int y = sc.nextInt();
            Location driverLocation = new Location(x,y);
            Driver driver = new Driver(name,driverLocation);
            uberManager.addToDriverList(driver);
        }
        Location riderLocation = new Location(5,10);
        Rider rider = new Rider("Jay",riderLocation);
        Driver driver = uberManager.findDriver(rider);
        double price = uberManager.findPrice(rider,driver);
        System.out.println("Ride Confirmed : " + driver.driverName + " " + price);




    }
}
