package com.Abstract_factory;


public class CarDealership {
    
    public static void main(String[] args) {
        System.out.println("=== Welcome to Car Dealership ===\n");
        
        try {
            // Using Toyota Factory
            System.out.println("--- Toyota Orders ---");
            CarFactory toyotaFactory = CarFactoryProvider.getFactory(CarManufacturerType.TOYOTA);
            
            Car camry = toyotaFactory.orderCar("camry");
            camry.displayInfo();
            camry.start();
            camry.stop();
            System.out.println();
            
            Car prius = toyotaFactory.orderCar("prius");
            prius.displayInfo();
            System.out.println();
            
            // Using Honda Factory
            System.out.println("--- Honda Orders ---");
            CarFactory hondaFactory = CarFactoryProvider.getFactory(CarManufacturerType.HONDA);
            
            Car civic = hondaFactory.orderCar("civic");
            civic.displayInfo();
            civic.start();
            civic.stop();
            System.out.println();
            
            // Using BMW Factory
            System.out.println("--- BMW Orders ---");
            CarFactory bmwFactory = CarFactoryProvider.getFactory(CarManufacturerType.BMW);
            
            Car m3 = bmwFactory.orderCar("m3");
            m3.displayInfo();
            m3.start();
            m3.stop();
            System.out.println();
            
            // Demonstrating polymorphism
            System.out.println("--- Polymorphism Demo ---");
            Car[] cars = {
                toyotaFactory.createCar("corolla"),
                hondaFactory.createCar("accord"),
                bmwFactory.createCar("320i")
            };
            
            for (Car car : cars) {
                car.displayInfo();
                System.out.println("Price: $" + car.getPrice());
                System.out.println();
            }
            
            // Demonstrating that all factories implement the same interface
            System.out.println("--- Factory Interface Demo ---");
            CarFactory[] factories = {
                new ToyotaFactory(),
                new HondaFactory(),
                new BMWFactory()
            };
            
            String[] models = {"camry", "civic", "320i"};
            
            for (int i = 0; i < factories.length; i++) {
                Car car = factories[i].createCar(models[i]);
                System.out.println("Created: " + car.getModel() + " - $" + car.getPrice());
            }
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}