package com.Inventorymanagementgrok;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.Random;

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

// ---------- SYNCHRONIZED LIST FOR OBSERVERS ----------
class SynchronizedObserverList {
    private final List<Observer> observers = new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    public void add(Observer observer) {
        lock.lock();
        try {
            observers.add(observer);
        } finally {
            lock.unlock();
        }
    }

    public void notifyAllObservers(ProductType productType, int stock) {
        lock.lock();
        try {
            for (Observer obs : observers) {
                // Notify in a separate thread to avoid deadlocks
                new Thread(() -> obs.update(productType, stock)).start();
            }
        } finally {
            lock.unlock();
        }
    }
}

// ---------- THREAD-SAFE PRODUCT CLASS ----------
class Product {
    private volatile int stock; // volatile for visibility across threads
    private final ProductType typeOfProduct;
    private final SynchronizedObserverList observers;

    Product(int stock, ProductType typeOfProduct) {
        this.stock = stock;
        this.typeOfProduct = typeOfProduct;
        this.observers = new SynchronizedObserverList();
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

    // Thread-safe stock addition
    public synchronized void setStock(int additionalStock) {
        boolean shouldNotify = (this.stock == 0 && additionalStock > 0);
        this.stock += additionalStock;

        // Double-check for notification condition after update
        if (shouldNotify || (this.stock > 0 && additionalStock > 0)) {
            notifyObservers();
        }
    }

    // Thread-safe stock removal with atomic check-and-update
    public synchronized String useStock(int requestedStock) {
        if (this.stock < requestedStock) {
            return "Available stock is less, only " + this.stock + " left.";
        }

        this.stock -= requestedStock;
        boolean shouldNotify = (this.stock == 0);

        if (shouldNotify) {
            notifyObservers();
            return "Stock N/A";
        }

        return "Stock deducted successfully";
    }

    // Thread-safe notification
    private void notifyObservers() {
        observers.notifyAllObservers(typeOfProduct, stock);
    }
}

// ---------- THREAD-SAFE SINGLETON INVENTORY SERVICE ----------
class InventoryService {
    private static volatile InventoryService instance; // volatile for double-checked locking
    private static final Lock lock = new ReentrantLock();

    private InventoryService() {
        // private constructor
    }

    // Double-checked locking for thread-safe singleton
    public static InventoryService getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new InventoryService();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    // Thread-safe stock addition
    public void addStock(Product product, int stock) {
        if (stock < 0) {
            System.out.println("Cannot add negative stock");
            return;
        }
        product.setStock(stock);
        System.out.println("Added " + stock + " units of " + product.getTypeOfProduct() +
                ". Current stock: " + product.getStock());
    }

    // Thread-safe stock removal
    public void removeStock(Product product, int stock) {
        if (stock < 0) {
            System.out.println("Cannot remove negative stock");
            return;
        }
        String result = product.useStock(stock);
        System.out.println("Remove operation: " + result);
        System.out.println("Current stock of " + product.getTypeOfProduct() + ": " + product.getStock());
    }

    // Method to get current stock for monitoring
    public int getCurrentStock(Product product) {
        return product.getStock();
    }
}

// ---------- CONCURRENT CLIENT CLASS ----------
class ConcurrentClient implements Runnable {
    private final InventoryService inventory;
    private final Product product;
    private final String clientName;
    private final int operationsCount;
    private final boolean isAddingStock;

    public ConcurrentClient(InventoryService inventory, Product product, String clientName,
                            int operationsCount, boolean isAddingStock) {
        this.inventory = inventory;
        this.product = product;
        this.clientName = clientName;
        this.operationsCount = operationsCount;
        this.isAddingStock = isAddingStock;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < operationsCount; i++) {
            try {
                Thread.sleep(random.nextInt(100)); // Random delay to simulate real-world timing

                int stockAmount = random.nextInt(10) + 1; // Random stock between 1-10

                System.out.println("[" + Thread.currentThread().getName() + "] " +
                        clientName + " attempting to " +
                        (isAddingStock ? "add" : "remove") + " " + stockAmount +
                        " units of " + product.getTypeOfProduct());

                if (isAddingStock) {
                    inventory.addStock(product, stockAmount);
                } else {
                    inventory.removeStock(product, stockAmount);
                }

                // Print current state after operation
                System.out.println("[" + Thread.currentThread().getName() + "] " +
                        clientName + ": " + product.getTypeOfProduct() +
                        " stock is now " + product.getStock());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

// ---------- MAIN APPLICATION WITH CONCURRENCY ----------
public class InventoryManagement1 {
    public static void main(String[] args) {
        InventoryService inventory = InventoryService.getInstance(); // Thread-safe singleton

        // Create products
        Product iphone = new Product(0, ProductType.IPHONE);
        Product samsung = new Product(0, ProductType.SAMSUNG);

        // Create observers
        EmailObserver email1 = new EmailObserver("Jay", "abcd@gmail.com");
        EmailObserver email2 = new EmailObserver("Jiya", "jiya@gmail.com");
        SmsObserver sms1 = new SmsObserver("Dad", "234234");
        SmsObserver sms2 = new SmsObserver("Mom", "23443556");

        // Attach observers (thread-safe)
        iphone.addObserver(email1);
        iphone.addObserver(email2);
        iphone.addObserver(sms1);
        iphone.addObserver(sms2);

        samsung.addObserver(email1);
        samsung.addObserver(email2);
        samsung.addObserver(sms1);
        samsung.addObserver(sms2);

        System.out.println("=== Inventory Management System with Concurrency ===");
        System.out.println("Starting concurrent operations...");

        // Create multiple concurrent clients
        int numClients = 5;
        Thread[] iphoneClients = new Thread[numClients];
        Thread[] samsungClients = new Thread[numClients];

        // Create concurrent clients for iPhone
        for (int i = 0; i < numClients; i++) {
            boolean isAdding = (i % 2 == 0); // Alternate between adding and removing
            String clientName = "iPhoneClient" + i;
            iphoneClients[i] = new Thread(new ConcurrentClient(inventory, iphone, clientName,
                    10, isAdding), clientName);
            iphoneClients[i].start();
        }

        // Create concurrent clients for Samsung
        for (int i = 0; i < numClients; i++) {
            boolean isAdding = (i % 2 == 0); // Alternate between adding and removing
            String clientName = "SamsungClient" + i;
            samsungClients[i] = new Thread(new ConcurrentClient(inventory, samsung, clientName,
                    10, isAdding), clientName);
            samsungClients[i].start();
        }

        // Wait for all threads to complete
        try {
            for (Thread client : iphoneClients) {
                client.join();
            }
            for (Thread client : samsungClients) {
                client.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread interrupted");
        }

        // Final stock report
        System.out.println("\n=== FINAL STOCK REPORT ===");
        System.out.println("iPhone stock: " + iphone.getStock());
        System.out.println("Samsung stock: " + samsung.getStock());

        // Interactive mode (single-threaded for simplicity)
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Interactive Mode (Single-threaded) ===");
        System.out.println("Press 1 to add product or 2 to remove product (or 0 to exit):");

        int choice;
        while ((choice = sc.nextInt()) != 0) {
            if (choice == 1) {
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
            } else if (choice == 2) {
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
            } else {
                System.out.println("Invalid choice, try again.");
            }

            System.out.println("Current stocks - iPhone: " + iphone.getStock() +
                    ", Samsung: " + samsung.getStock());
            System.out.println("Press 1 to add, 2 to remove, or 0 to exit:");
        }

        sc.close();
        System.out.println("Inventory Management System terminated.");
    }
}