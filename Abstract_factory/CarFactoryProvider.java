package com.Abstract_factory;


public class CarFactoryProvider {
    
    public static CarFactory getFactory(CarManufacturerType type) {
        switch (type) {
            case TOYOTA:
                return new ToyotaFactory();
            case HONDA:
                return new HondaFactory();
            case BMW:
                return new BMWFactory();
            default:
                throw new IllegalArgumentException("Unknown manufacturer type: " + type);
        }
    }
}