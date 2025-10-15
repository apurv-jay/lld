package com.factory;
import com.factory.Car;

public class Toyota implements Car {
    private String model;

    public Toyota(String model) {
        this.model = model;
    }

    @Override
    public void start() {
        System.out.println("Toyota " + model + " engine started with push button start.");
    }

    @Override
    public void stop() {
        System.out.println("Toyota " + model + " engine stopped.");
    }

    @Override
    public void accelerate() {
        System.out.println("Toyota " + model + " is accelerating smoothly with hybrid technology.");
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getManufacturer() {
        return "Toyota";
    }
}