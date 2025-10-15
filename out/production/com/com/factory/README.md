```mermaid
classDiagram
%% Interface - Product
class Car {
<<interface>>
+start() void
+stop() void
+accelerate() void
+getModel() String
+getManufacturer() String
}

    %% Concrete Products
    class Toyota {
        -model: String
        +Toyota(model: String)
        +start() void
        +stop() void
        +accelerate() void
        +getModel() String
        +getManufacturer() String
    }
    
    class Honda {
        -model: String
        +Honda(model: String)
        +start() void
        +stop() void
        +accelerate() void
        +getModel() String
        +getManufacturer() String
    }
    
    class BMW {
        -model: String
        +BMW(model: String)
        +start() void
        +stop() void
        +accelerate() void
        +getModel() String
        +getManufacturer() String
    }
    
    %% Factory Class - Creator
    class CarFactory {
        <<static>>
        +createCar(manufacturer: String, model: String)$ Car
        +createCar(manufacturer: String)$ Car
    }
    
    %% Client
    class CarFactoryDemo {
        +main(args: String[])$ void
    }
    
    %% Implementation relationships
    Car <|.. Toyota : implements
    Car <|.. Honda : implements  
    Car <|.. BMW : implements
    
    %% Factory creates products
    CarFactory ..> Car : returns
    CarFactory ..> Toyota : creates
    CarFactory ..> Honda : creates
    CarFactory ..> BMW : creates
    
    %% Client uses factory and interface
    CarFactoryDemo --> CarFactory : calls createCar()
    CarFactoryDemo --> Car : uses returned object
    
    %% Styling
    class Car {
        <<Product Interface>>
    }
    class CarFactory {
        <<Factory/Creator>>
    }
    class CarFactoryDemo {
        <<Client>>
    }