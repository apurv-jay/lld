package com.Abstract_factory;


public class HondaCar implements Car {
    private String model;
    private String engine;
    private double price;
    
    public HondaCar(String model, String engine, double price) {
        this.model = model;
        this.engine = engine;
        this.price = price;
    }
    
    @Override
    public void start() {
        System.out.println("Honda " + model + " starting with efficient " + engine + " engine");
    }
    
    @Override
    public void stop() {
        System.out.println("Honda " + model + " engine stopped");
    }
    
    @Override
    public void displayInfo() {
        System.out.println("=== Honda Car ===");
        System.out.println("Model: " + model);
        System.out.println("Engine: " + engine);
        System.out.println("Price: $" + price);
        System.out.println("Features: Performance, Technology, Durability");
    }
    
    @Override
    public String getModel() { return model; }
    
    @Override
    public String getEngine() { return engine; }
    
    @Override
    public double getPrice() { return price; }
}