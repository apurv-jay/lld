package com.factory;

public class Honda implements Car {
    private String model;

    public Honda(String model) {
        this.model = model;
    }

    @Override
    public void start() {
        System.out.println("Honda " + model + " engine started with key ignition.");
    }

    @Override
    public void stop() {
        System.out.println("Honda " + model + " engine stopped.");
    }

    @Override
    public void accelerate() {
        System.out.println("Honda " + model + " is accelerating with VTEC power.");
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getManufacturer() {
        return "Honda";
    }
}