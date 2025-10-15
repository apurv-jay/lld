package com.splitwise1;

import java.util.*;

public class Splitwise1 {
    public static void main(String[] args) {
        // Create users
        User alice = new User("Alice");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        // Create group
        Group friends = new Group("Friends");
        friends.addUser(alice);
        friends.addUser(bob);
        friends.addUser(charlie);

        // 1:1 group example
        Group aliceBob = new Group("Alice-Bob");
        aliceBob.addUser(alice);
        aliceBob.addUser(bob);

        // Equal split expense in friends group: Alice pays $30 dinner, split equally among 3
        List<User> friendsSplitters = Arrays.asList(alice, bob, charlie);
        SplitStrategy equalStrategy = new EqualSplitStrategy();
        Map<User, Double> equalShares = equalStrategy.computeShares(friendsSplitters, 30.0);
        Expense dinner = new Expense(alice, "Dinner", 30.0, equalShares);
        friends.addExpense(dinner);

        // Unequal split expense in friends: Bob pays $50 groceries, Alice 20, Bob 10, Charlie 20
        Map<User, Double> unequalShares = new HashMap<>();
        unequalShares.put(alice, 20.0);
        unequalShares.put(bob, 10.0);
        unequalShares.put(charlie, 20.0);
        SplitStrategy unequalStrategy = new UnequalSplitStrategy(unequalShares);
        Map<User, Double> computedUnequal = unequalStrategy.computeShares(friendsSplitters, 50.0);
        Expense groceries = new Expense(bob, "Groceries", 50.0, computedUnequal);
        friends.addExpense(groceries);

        // 1:1 equal split: Alice pays $10 coffee, split with Bob
        List<User> abSplitters = Arrays.asList(alice, bob);
        SplitStrategy abEqual = new EqualSplitStrategy();
        Map<User, Double> abShares = abEqual.computeShares(abSplitters, 10.0);
        Expense coffee = new Expense(alice, "Coffee", 10.0, abShares);
        aliceBob.addExpense(coffee);

        // View balance sheets
        System.out.println("=== Friends Group Balance Sheets ===");
        System.out.println("Alice's Balance Sheet:");
        System.out.println(friends.getUserBalanceSheet(alice));
        System.out.println("\nBob's Balance Sheet:");
        System.out.println(friends.getUserBalanceSheet(bob));
        System.out.println("\nCharlie's Balance Sheet:");
        System.out.println(friends.getUserBalanceSheet(charlie));

        System.out.println("\n=== Alice-Bob 1:1 Group ===");
        System.out.println("Alice's Balance Sheet:");
        System.out.println(aliceBob.getUserBalanceSheet(alice));
    }
}

class User {
    private static long nextId = 1L;
    private final long id;
    private final String name;

    public User(String name) {
        this.id = nextId++;
        this.name = name;
    }

    public long getId() { return id; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() { return name; }
}

class Group {
    private static long nextId = 1L;
    private final long id;
    private final String name;
    private final Set<User> users = new HashSet<>();
    private final List<Expense> expenses = new ArrayList<>();
    private final Map<Long, Map<Long, Double>> balances = new HashMap<>(); // userId -> (otherId -> amount owed by first to second)

    public Group(String name) {
        this.id = nextId++;
        this.name = name;
    }

    public void addUser(User user) {
        users.add(user);
        // Initialize balance row/col for this user
        balances.putIfAbsent(user.getId(), new HashMap<>());
        for (User existing : users) {
            if (existing != user) {
                balances.get(user.getId()).putIfAbsent(existing.getId(), 0.0);
                balances.computeIfAbsent(existing.getId(), k -> new HashMap<>()).put(user.getId(), 0.0);
            }
        }
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        // Update balances: each splitter owes payer their share (if not payer)
        User payer = expense.getPayer();
        if (!users.contains(payer)) throw new IllegalArgumentException("Payer not in group");
        for (Map.Entry<User, Double> entry : expense.getShares().entrySet()) {
            User splitter = entry.getKey();
            double share = entry.getValue();
            if (!users.contains(splitter)) throw new IllegalArgumentException("Splitter not in group");
            if (!splitter.equals(payer) && share > 0) {
                long sId = splitter.getId();
                long pId = payer.getId();
                balances.get(sId).put(pId, balances.get(sId).getOrDefault(pId, 0.0) + share);
            }
        }
    }

    public Set<User> getUsers() { return new HashSet<>(users); }
    public List<Expense> getExpenses() { return new ArrayList<>(expenses); }
    public Map<Long, Map<Long, Double>> getBalances() { return new HashMap<>(balances); } // Shallow copy for view

    public BalanceSheet getUserBalanceSheet(User user) {
        if (!users.contains(user)) throw new IllegalArgumentException("User not in group");

        double totalPaid = 0.0;
        double totalExpense = 0.0;
        for (Expense e : expenses) {
            if (e.getPayer().equals(user)) {
                totalPaid += e.getAmount();
            }
            Double share = e.getShares().get(user);
            if (share != null) {
                totalExpense += share;
            }
        }

        Map<User, Double> balancesWithOthers = new HashMap<>();
        for (User other : users) {
            if (!other.equals(user)) {
                long uId = user.getId();
                long oId = other.getId();
                double owesOther = balances.getOrDefault(uId, new HashMap<>()).getOrDefault(oId, 0.0);
                double owedByOther = balances.getOrDefault(oId, new HashMap<>()).getOrDefault(uId, 0.0);
                balancesWithOthers.put(other, owesOther - owedByOther); // Positive: user owes other
            }
        }

        return new BalanceSheet(totalPaid, totalExpense, balancesWithOthers);
    }

    @Override
    public String toString() { return name + " (ID: " + id + ")"; }
}

class Expense {
    private static long nextId = 1L;
    private final long id;
    private final User payer;
    private final String description;
    private final double amount;
    private final Map<User, Double> shares;

    public Expense(User payer, String description, double amount, Map<User, Double> shares) {
        this.id = nextId++;
        this.payer = payer;
        this.description = description;
        this.amount = amount;
        this.shares = new HashMap<>(shares); // Defensive copy
    }

    public long getId() { return id; }
    public User getPayer() { return payer; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public Map<User, Double> getShares() { return new HashMap<>(shares); }

    @Override
    public String toString() {
        return String.format("Expense(ID=%d, Payer=%s, Desc=%s, Amount=%.2f, Shares=%s)", id, payer, description, amount, shares);
    }
}

class BalanceSheet {
    private final double totalPaid;
    private final double totalExpense;
    private final Map<User, Double> balancesWithOthers; // Positive: you owe them

    public BalanceSheet(double totalPaid, double totalExpense, Map<User, Double> balancesWithOthers) {
        this.totalPaid = totalPaid;
        this.totalExpense = totalExpense;
        this.balancesWithOthers = new HashMap<>(balancesWithOthers);
    }

    public double getTotalPaid() { return totalPaid; }
    public double getTotalExpense() { return totalExpense; }
    public Map<User, Double> getBalancesWithOthers() { return new HashMap<>(balancesWithOthers); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total Paid: %.2f | Total Expense Share: %.2f\n", totalPaid, totalExpense));
        sb.append("Balances with others:\n");
        for (Map.Entry<User, Double> entry : balancesWithOthers.entrySet()) {
            double net = entry.getValue();
            String direction = net > 0 ? "owes" : "is owed by";
            sb.append(String.format("  %s: %.2f (%s)\n", entry.getKey(), Math.abs(net), direction));
        }
        return sb.toString();
    }
}

interface SplitStrategy {
    Map<User, Double> computeShares(List<User> users, double amount);
}

class EqualSplitStrategy implements SplitStrategy {
    @Override
    public Map<User, Double> computeShares(List<User> users, double amount) {
        Map<User, Double> shares = new HashMap<>();
        if (users.isEmpty()) return shares;
        double share = amount / users.size();
        for (User user : users) {
            shares.put(user, share);
        }
        return shares;
    }
}

class UnequalSplitStrategy implements SplitStrategy {
    private final Map<User, Double> providedShares;

    public UnequalSplitStrategy(Map<User, Double> providedShares) {
        this.providedShares = new HashMap<>(providedShares);
    }

    @Override
    public Map<User, Double> computeShares(List<User> users, double amount) {
        double sum = providedShares.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(sum - amount) > 0.001) { // Tolerance for floating point
            throw new IllegalArgumentException("Provided shares sum (" + sum + ") does not match amount (" + amount + ")");
        }
        // Filter to only users in list (optional validation)
        Map<User, Double> filtered = new HashMap<>();
        for (User user : users) {
            filtered.put(user, providedShares.getOrDefault(user, 0.0));
        }
        return filtered;
    }
}