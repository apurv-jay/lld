package com.vendingMachine;

import java.util.HashMap;
import java.util.Map;

// Product class
class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
}

// State interface
interface State {
    void insertCoin(VendingMachine machine, double amount);
    void selectProduct(VendingMachine machine, Product product);
    void dispenseProduct(VendingMachine machine);
    double returnChange(VendingMachine machine);
}

// IdleState class
class IdleState implements State {
    @Override
    public void insertCoin(VendingMachine machine, double amount) {
        machine.setBalance(machine.getBalance() + amount);
        machine.setState(new CoinInsertedState());
        System.out.println("Coin inserted: " + amount);
    }

    @Override
    public void selectProduct(VendingMachine machine, Product product) {
        System.out.println("Insert coins first.");
    }

    @Override
    public void dispenseProduct(VendingMachine machine) {
        System.out.println("Insert coins and select a product first.");
    }

    @Override
    public double returnChange(VendingMachine machine) {
        System.out.println("No coins to return.");
        return 0;
    }
}

// CoinInsertedState class
class CoinInsertedState implements State {
    @Override
    public void insertCoin(VendingMachine machine, double amount) {
        machine.setBalance(machine.getBalance() + amount);
        System.out.println("Additional coin inserted: " + amount);
    }

    @Override
    public void selectProduct(VendingMachine machine, Product product) {
        machine.setSelectedProduct(product);
        machine.setState(new ProductSelectedState());
        System.out.println("Product selected: " + product.getName());
    }

    @Override
    public void dispenseProduct(VendingMachine machine) {
        System.out.println("Select a product first.");
    }

    @Override
    public double returnChange(VendingMachine machine) {
        double change = machine.getBalance();
        machine.setBalance(0);
        machine.setState(new IdleState());
        System.out.println("Change returned: " + change);
        return change;
    }
}

// ProductSelectedState class
class ProductSelectedState implements State {
    @Override
    public void insertCoin(VendingMachine machine, double amount) {
        System.out.println("Product already selected. Proceed to dispense or return change.");
    }

    @Override
    public void selectProduct(VendingMachine machine, Product product) {
        System.out.println("Product already selected. Proceed to dispense.");
    }

    @Override
    public void dispenseProduct(VendingMachine machine) {
        Product product = machine.getSelectedProduct();
        if (product == null) {
            System.out.println("No product selected.");
            return;
        }
        if (machine.getBalance() < product.getPrice()) {
            System.out.println("Insufficient balance. Please insert more coins.");
            return;
        }
        if (machine.getProductStock(product) <= 0) {
            System.out.println("Product out of stock.");
            return;
        }
        machine.setBalance(machine.getBalance() - product.getPrice());
        machine.setProductStock(product, machine.getProductStock(product) - 1);
        System.out.println("Product dispensed: " + product.getName());
        machine.setSelectedProduct(null);
        machine.setState(new IdleState());
    }

    @Override
    public double returnChange(VendingMachine machine) {
        double change = machine.getBalance();
        machine.setBalance(0);
        machine.setState(new IdleState());
        System.out.println("Change returned: " + change);
        return change;
    }
}

// VendingMachine class
public class VendingMachine {
    private State state;
    private Map<Product, Integer> products = new HashMap<>();
    private double balance = 0;
    private Product selectedProduct;

    public VendingMachine() {
        this.state = new IdleState();
    }

    public void addProduct(Product product, int stock) {
        products.put(product, stock);
    }

    public int getProductStock(Product product) {
        return products.getOrDefault(product, 0);
    }

    public void setProductStock(Product product, int stock) {
        products.put(product, stock);
    }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public Product getSelectedProduct() { return selectedProduct; }
    public void setSelectedProduct(Product product) { this.selectedProduct = product; }

    public void setState(State state) { this.state = state; }
    public State getState() { return state; }

    public void insertCoin(double amount) {
        state.insertCoin(this, amount);
    }

    public void selectProduct(Product product) {
        state.selectProduct(this, product);
    }

    public void dispenseProduct() {
        state.dispenseProduct(this);
    }

    public double returnChange() {
        return state.returnChange(this);
    }

    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();
        Product coke = new Product("Coke", 1.5);
        Product chips = new Product("Chips", 1.0);
        vm.addProduct(coke, 5);
        vm.addProduct(chips, 3);

        vm.insertCoin(1.0);  // Coin inserted: 1.0
        vm.selectProduct(coke);  // Product selected: Coke
        vm.insertCoin(0.5);  // Additional coin inserted: 0.5
        vm.dispenseProduct();  // Product dispensed: Coke
        vm.returnChange();  // Change returned: 0.0
    }
}