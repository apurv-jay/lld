package com.factory;

public class CarFactory {

    // Factory method to create cars based on manufacturer and model
    public static Car createCar(String manufacturer, String model) { //since the create car method is static so it is used directyl
        if (manufacturer == null || manufacturer.trim().isEmpty()) {
            throw new IllegalArgumentException("Manufacturer cannot be null or empty");
        }

        switch (manufacturer.toLowerCase()) {
            case "toyota":
                return new Toyota(model);
            case "honda":
                return new Honda(model);
            case "bmw":
                return new BMW(model);
            default:
                throw new IllegalArgumentException("Unknown manufacturer: " + manufacturer);
        }
    }

    // Overloaded method for convenience with default models
    public static Car createCar(String manufacturer) {
        switch (manufacturer.toLowerCase()) {
            case "toyota":
                return new Toyota("Camry");
            case "honda":
                return new Honda("Civic");
            case "bmw":
                return new BMW("3 Series");
            default:
                throw new IllegalArgumentException("Unknown manufacturer: " + manufacturer);
        }
    }
}