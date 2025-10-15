package com.InventoryManagement1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum ProductType {
    IPHONE,
    SAMSUNG,
}

// ---------- OBSERVER INTERFACE ----------
interface Observer {
    void update(ProductType productType, int stock);
}

// ---------- EMAIL OBSERVER ----------
class EmailObserver implements Observer {
    private String name;
    private String emailId;

    EmailObserver(String name, String emailId) {
        this.name = name;
        this.emailId = emailId;
    }

    @Override
    public void update(ProductType productType, int stock) {
        System.out.println("📧 Hey " + name + "! " + productType + " stock is now " + stock + " via Email (" + emailId + ")");
    }
}

// ---------- SMS OBSERVER ----------
class SmsObserver implements Observer {
    private String name;
    private String phoneNumber;

    SmsObserver(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void update(ProductType productType, int stock) {
        System.out.println("📱 Hey " + name + "! " + productType + " stock is now " + stock + " via SMS (" + phoneNumber + ")");
    }
}

// ---------- PRODUCT CLASS ----------
class Product {
    private int stock;
    private ProductType typeOfProduct;
    private List<Observer> observers = new ArrayList<>();

    Product(int stock, ProductType typeOfProduct) {
        this.stock = stock;
        this.typeOfProduct = typeOfProduct;
    }

    public int getStock() {
        return stock;
    }

    public ProductType getTypeOfProduct() {
        return typeOfProduct;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyAllObservers() {
        for (Observer obs : observers) {
            obs.update(typeOfProduct, stock);
        }
    }

    public void setStock(int stock) {
        if (this.stock == 0 && stock > 0) {
            notifyAllObservers();
        }
        this.stock += stock;
    }

    public String useStock(int stock) {
        if (this.stock < stock) {
            return "Available stock is less, only " + this.stock + " left.";
        }
        this.stock -= stock;
        if (this.stock == 0) {
            notifyAllObservers();
            return "Stock N/A";
        }
        return "Stock deducted successfully";
    }
}

// ---------- SINGLETON INVENTORY SERVICE ----------
final class InventoryService {
    private static InventoryService instance; // single instance

    private InventoryService() {
        // private constructor
    }

    public static synchronized InventoryService getInstance() {
        if (instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }

    public void addStock(Product product, int stock) {
        product.setStock(stock);
    }

    public void removeStock(Product product, int stock) {
        String result = product.useStock(stock);
        System.out.println(result);
    }
}

// ---------- MAIN APPLICATION ----------
public class InventoryManagement1 {
    public static void main(String[] args) {
        InventoryService inventory = InventoryService.getInstance(); // singleton instance

        // Create products
        Product iphone = new Product(0, ProductType.IPHONE);
        Product samsung = new Product(0, ProductType.SAMSUNG);

        // Create observers
        EmailObserver email1 = new EmailObserver("Jay", "abcd@gmail.com");
        EmailObserver email2 = new EmailObserver("Jiya", "jiya@gmail.com");
        SmsObserver sms1 = new SmsObserver("Dad", "234234");
        SmsObserver sms2 = new SmsObserver("Mom", "23443556");

        // Attach observers
        iphone.addObserver(email1);
        iphone.addObserver(email2);
        iphone.addObserver(sms1);
        iphone.addObserver(sms2);

        samsung.addObserver(email1);
        samsung.addObserver(email2);
        samsung.addObserver(sms1);
        samsung.addObserver(sms2);

        Scanner sc = new Scanner(System.in);

        int i = 0;
        while (i < 50) {
            System.out.println("Press 1 to add product or 2 to remove product:");
            int num = sc.nextInt();

            if (num == 1) {
                System.out.println("Press 1 to add iPhone or 2 for Samsung:");
                int productType = sc.nextInt();
                System.out.println("Enter stock count:");
                int stockCount = sc.nextInt();

                if (productType == 1) {
                    inventory.addStock(iphone, stockCount);
                } else if (productType == 2) {
                    inventory.addStock(samsung, stockCount);
                } else {
                    System.out.println("Invalid product type.");
                }
                i++;
            } else if (num == 2) {
                System.out.println("Press 1 to remove iPhone or 2 for Samsung:");
                int productType = sc.nextInt();
                System.out.println("Enter stock count:");
                int stockCount = sc.nextInt();

                if (productType == 1) {
                    inventory.removeStock(iphone, stockCount);
                } else if (productType == 2) {
                    inventory.removeStock(samsung, stockCount);
                } else {
                    System.out.println("Invalid product type.");
                }
                i++;
            } else {
                System.out.println("Invalid choice, try again.");
            }
        }

        sc.close();
    }
}
