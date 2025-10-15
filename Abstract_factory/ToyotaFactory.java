package com.Abstract_factory;


public class ToyotaFactory implements CarFactory {
    
    @Override
    public Car createCar(String model) {
        switch (model.toLowerCase()) {
            case "camry":
                return new ToyotaCar("Camry", "2.5L 4-Cylinder", 25000);
            case "corolla":
                return new ToyotaCar("Corolla", "1.8L 4-Cylinder", 22000);
            case "prius":
                return new ToyotaCar("Prius", "Hybrid 1.8L", 28000);
            case "rav4":
                return new ToyotaCar("RAV4", "2.5L 4-Cylinder AWD", 32000);
            default:
                throw new IllegalArgumentException("Unknown Toyota model: " + model);
        }
    }
}