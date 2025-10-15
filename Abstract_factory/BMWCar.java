package com.Abstract_factory;

public class BMWCar implements Car {
    private String model;
    private String engine;
    private double price;
    
    public BMWCar(String model, String engine, double price) {
        this.model = model;
        this.engine = engine;
        this.price = price;
    }
    
    @Override
    public void start() {
        System.out.println("BMW " + model + " starting with powerful " + engine + " engine");
        System.out.println("Ultimate driving machine activated!");
    }
    
    @Override
    public void stop() {
        System.out.println("BMW " + model + " stopped with precision braking");
    }
    
    @Override
    public void displayInfo() {
        System.out.println("=== BMW Car ===");
        System.out.println("Model: " + model);
        System.out.println("Engine: " + engine);
        System.out.println("Price: $" + price);
        System.out.println("Features: Luxury, Performance, Innovation");
    }
    
    @Override
    public String getModel() { return model; }
    
    @Override
    public String getEngine() { return engine; }
    
    @Override
    public double getPrice() { return price; }
}