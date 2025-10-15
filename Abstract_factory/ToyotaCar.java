package com.Abstract_factory;



public class ToyotaCar implements Car {
    private String model;
    private String engine;
    private double price;
    
    public ToyotaCar(String model, String engine, double price) {
        this.model = model;
        this.engine = engine;
        this.price = price;
    }
    
    @Override
    public void start() {
        System.out.println("Toyota " + model + " starting with reliable " + engine + " engine");
    }
    
    @Override
    public void stop() {
        System.out.println("Toyota " + model + " stopped safely");
    }
    
    @Override
    public void displayInfo() {
        System.out.println("=== Toyota Car ===");
        System.out.println("Model: " + model);
        System.out.println("Engine: " + engine);
        System.out.println("Price: $" + price);
        System.out.println("Features: Reliability, Fuel Efficiency, Safety");
    }
    
    @Override
    public String getModel() { return model; }
    
    @Override
    public String getEngine() { return engine; }
    
    @Override
    public double getPrice() { return price; }
}