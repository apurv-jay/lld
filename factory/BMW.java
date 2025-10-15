package com.factory;

public class BMW implements Car {
    private String model;

    public BMW(String model) {
        this.model = model;
    }

    @Override
    public void start() {
        System.out.println("BMW " + model + " engine started with luxury comfort.");
    }

    @Override
    public void stop() {
        System.out.println("BMW " + model + " engine stopped gracefully.");
    }

    @Override
    public void accelerate() {
        System.out.println("BMW " + model + " is accelerating with ultimate driving machine precision.");
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getManufacturer() {
        return "BMW";
    }
}