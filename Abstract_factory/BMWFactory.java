package com.Abstract_factory;

public class BMWFactory implements CarFactory {
    
    @Override
    public Car createCar(String model) {
        switch (model.toLowerCase()) {
            case "320i":
                return new BMWCar("320i", "2.0L Twin Turbo", 35000);
            case "330i":
                return new BMWCar("330i", "2.0L Twin Turbo", 42000);
            case "x3":
                return new BMWCar("X3", "2.0L Twin Turbo AWD", 45000);
            case "m3":
                return new BMWCar("M3", "3.0L Twin Turbo V6", 75000);
            default:
                throw new IllegalArgumentException("Unknown BMW model: " + model);
        }
    }
}