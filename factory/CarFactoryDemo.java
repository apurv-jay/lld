package com.factory;

public class CarFactoryDemo {
    public static void main(String[] args) {
        System.out.println("=== Factory Design Pattern Demo ===\n");

        try {
            // Creating cars using factory with specific models
            Car toyota = CarFactory.createCar("Toyota", "Prius");
            Car honda = CarFactory.createCar("Honda", "Accord");
            Car bmw = CarFactory.createCar("BMW", "X5");

            // Demonstrating polymorphism - all objects treated as Car interface
            Car[] cars = {toyota, honda, bmw};

            for (Car car : cars) {
                System.out.println("--- " + car.getManufacturer() + " " + car.getModel() + " ---");
                car.start();
                car.accelerate();
                car.stop();
                System.out.println();
            }

            // Creating cars with default models
            System.out.println("=== Creating cars with default models ===");
            Car defaultToyota = CarFactory.createCar("Toyota");
            Car defaultHonda = CarFactory.createCar("Honda");

            System.out.println("Default Toyota: " + defaultToyota.getModel());
            System.out.println("Default Honda: " + defaultHonda.getModel());

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Demonstrating error handling
        System.out.println("\n=== Error Handling Demo ===");
        try {
            Car invalidCar = CarFactory.createCar("Ferrari", "458");
        } catch (IllegalArgumentException e) {
            System.err.println("Expected error: " + e.getMessage());
        }
    }
}