package com.Abstract_factory;



public interface CarFactory {
    Car createCar(String model);
    
    // Default method for common car ordering process
    default Car orderCar(String model) {
        Car car = createCar(model);
        
        // Common operations for all cars
        System.out.println("Manufacturing car...");
        System.out.println("Quality testing...");
        System.out.println("Car ready for delivery!");
        
        return car;
    }
}