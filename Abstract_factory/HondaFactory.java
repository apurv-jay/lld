package com.Abstract_factory;


public class HondaFactory implements CarFactory {
    
    @Override
    public Car createCar(String model) {
        switch (model.toLowerCase()) {
            case "civic":
                return new HondaCar("Civic", "1.5L Turbo", 23000);
            case "accord":
                return new HondaCar("Accord", "2.0L Turbo", 26000);
            case "crv":
                return new HondaCar("CR-V", "1.5L Turbo AWD", 30000);
            case "pilot":
                return new HondaCar("Pilot", "3.5L V6 AWD", 38000);
            default:
                throw new IllegalArgumentException("Unknown Honda model: " + model);
        }
    }
}