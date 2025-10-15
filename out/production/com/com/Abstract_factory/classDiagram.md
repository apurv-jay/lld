```mermaid
classDiagram
    %% Product Interface and Implementations
    class Car {
        <<interface>>
        +start()
        +stop()
        +displayInfo()
        +getModel() String
        +getEngine() String
        +getPrice() double
    }
    
    class ToyotaCar {
        -String model
        -String engine
        -double price
        +start()
        +stop()
        +displayInfo()
        +getModel() String
        +getEngine() String
        +getPrice() double
    }
    
    class HondaCar {
        -String model
        -String engine
        -double price
        +start()
        +stop()
        +displayInfo()
        +getModel() String
        +getEngine() String
        +getPrice() double
    }
    
    class BMWCar {
        -String model
        -String engine
        -double price
        +start()
        +stop()
        +displayInfo()
        +getModel() String
        +getEngine() String
        +getPrice() double
    }
    
    %% Factory Interface and Implementations
    class CarFactory {
        <<interface>>
        +createCar(String model) Car
        +orderCar(String model) Car
    }
    
    class ToyotaFactory {
        +createCar(String model) Car
    }
    
    class HondaFactory {
        +createCar(String model) Car
    }
    
    class BMWFactory {
        +createCar(String model) Car
    }
    
    %% Factory Provider and Client
    class CarFactoryProvider {
        <<utility>>
        +getFactory(CarManufacturerType) CarFactory
    }
    
    class CarManufacturerType {
        <<enumeration>>
        TOYOTA
        HONDA
        BMW
    }
    
    class CarDealership {
        <<client>>
        +main(String[] args)
    }
    
    %% Relationships
    Car <|.. ToyotaCar : implements
    Car <|.. HondaCar : implements
    Car <|.. BMWCar : implements
    
    CarFactory <|.. ToyotaFactory : implements
    CarFactory <|.. HondaFactory : implements
    CarFactory <|.. BMWFactory : implements
    
    ToyotaFactory ..> ToyotaCar : creates
    HondaFactory ..> HondaCar : creates
    BMWFactory ..> BMWCar : creates
    
    CarDealership --> CarFactoryProvider : uses
    CarFactoryProvider --> CarFactory : returns
    CarFactoryProvider --> CarManufacturerType : uses
    CarDealership --> Car : works with
    
    %% Notes
    note for Car "Product Interface - Defines contract for all cars"
    note for CarFactory "Factory Interface - Defines contract for all factories"
    note for CarFactoryProvider "Factory Provider - Returns appropriate factory based on manufacturer type"
    note for CarDealership "Client - Uses factories to create cars without knowing concrete classes"