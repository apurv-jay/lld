package com.MultiThreading;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;

class Product {
    private final String id;
    private int qty;

    public Product(String id, int qty) {
        this.id = id;
        this.qty = qty;
    }

    public String getId() { return id; }
    public int getQty() { return qty; }

    // decrease quantity (caller must hold lock for the product)
    public boolean decreaseIfAvailable(int amount) {
        if (qty >= amount) {
            // simulate some work/time
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            qty -= amount;
            return true;
        }
        return false;
    }
}

class Inventory {
    // product storage
    private final ConcurrentHashMap<String, Product> products = new ConcurrentHashMap<>();

    // per-product locks map
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public void addProduct(String id, int qty) {
        products.put(id, new Product(id, qty));
    }

    // thread-safe buy using per-product lock
    public boolean buyProduct(String productId, int amount) {
        // get/create lock for this product
        ReentrantLock lock = locks.computeIfAbsent(productId, k -> new ReentrantLock());

        lock.lock();
        try {
            Product p = products.get(productId);
            if (p == null) return false;           // no such product
            return p.decreaseIfAvailable(amount);  // decrease under lock
        } finally {
            lock.unlock();
        }
    }

    public Map<String, Integer> snapshot() {
        Map<String,Integer> snap = new HashMap<>();
        for (Map.Entry<String, Product> e : products.entrySet()) {
            snap.put(e.getKey(), e.getValue().getQty());
        }
        return snap;
    }
}

public class INV1 {
    public static void main(String[] args) throws Exception {
        Inventory inventory = new Inventory();

        // add 10 products, each with quantity 2
        for (int i = 1; i <= 10; i++) {
            inventory.addProduct("P" + i, 2);
        }

        ExecutorService executor = Executors.newFixedThreadPool(6);
        List<Callable<String>> tasks = new ArrayList<>();

        // Create tasks: some threads will try to buy same product, others different products
        tasks.add(makeBuyer(inventory, "P1", 1)); // buyer A -> P1
        tasks.add(makeBuyer(inventory, "P2", 1)); // buyer B -> P2 (should run concurrently with P1)
        tasks.add(makeBuyer(inventory, "P1", 1)); // buyer C -> P1 (will wait for P1's lock)
        tasks.add(makeBuyer(inventory, "P3", 2)); // buyer D -> P3
        tasks.add(makeBuyer(inventory, "P4", 1)); // buyer E -> P4
        tasks.add(makeBuyer(inventory, "P2", 1)); // buyer F -> P2 (may run concurrently with other P2 buyers if qty allows)

        // submit and collect futures
        List<Future<String>> futures = executor.invokeAll(tasks);

        // print results
        for (Future<String> f : futures) {
            System.out.println(f.get());
        }

        // final inventory snapshot
        System.out.println("Final inventory: " + inventory.snapshot());

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }

    private static Callable<String> makeBuyer(Inventory inventory, String productId, int amount) {
        return () -> {
            String threadName = Thread.currentThread().getName();
            long start = System.currentTimeMillis();
            System.out.println(threadName + " trying to buy " + amount + " of " + productId + " at " + start);
            boolean ok = inventory.buyProduct(productId, amount);
            long end = System.currentTimeMillis();
            return threadName + (ok ? " -> SUCCESS " : " -> FAIL ") + productId + " (took " + (end - start) + " ms)";
        };
    }
}
