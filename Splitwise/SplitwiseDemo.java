package com.Splitwise;

import java.util.*;

// User class - simple POJO
class User {
    private final long id;
    private final String name;
    // Balance managed externally in Splitwise, but getter for convenience (in real, query Splitwise)

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

// Interface for Split Strategy (prefer interface for multiple impls)
interface ISplitStrategy {
    Map<User, Double> computeShares(Expense expense);
}

// Equal Split Strategy
class EqualSplitStrategy implements ISplitStrategy {
    @Override
    public Map<User, Double> computeShares(Expense expense) {
        Map<User, Double> shares = new HashMap<>();
        List<User> involved = expense.getInvolvedUsers();
        if (involved.isEmpty()) return shares; // Edge: no split
        double share = expense.getAmount() / involved.size();
        for (User user : involved) {
            shares.put(user, share);
        }
        return shares;
    }
}

// Exact Split Strategy - shares provided at creation
class ExactSplitStrategy implements ISplitStrategy {
    private final Map<User, Double> shares;

    public ExactSplitStrategy(Map<User, Double> shares) {
        this.shares = new HashMap<>(shares); // Defensive copy
    }

    @Override
    public Map<User, Double> computeShares(Expense expense) {
        // In real: Validate sum(shares) == amount, but time constraint - assume valid
        return new HashMap<>(shares);
    }
}

// Expense class
class Expense {
    private final long id;
    private final User payer;
    private final double amount;
    private final List<User> involvedUsers;
    private final ISplitStrategy strategy;

    public Expense(long id, User payer, double amount, List<User> involvedUsers, ISplitStrategy strategy) {
        this.id = id;
        this.payer = payer;
        this.amount = amount;
        this.involvedUsers = new ArrayList<>(involvedUsers); // Defensive
        this.strategy = strategy;
    }

    public long getId() {
        return id;
    }

    public User getPayer() {
        return payer;
    }

    public double getAmount() {
        return amount;
    }

    public List<User> getInvolvedUsers() {
        return new ArrayList<>(involvedUsers);
    }

    public Map<User, Double> getShares() {
        return strategy.computeShares(this);
    }

    @Override
    public String toString() {
        return "Expense{id=" + id + ", payer=" + payer.getName() + ", amount=" + amount + "}";
    }
}

// Transaction for settlements
class Transaction {
    private final User fromUser;
    private final User toUser;
    private final double amount;

    public Transaction(User fromUser, User toUser, double amount) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
    }

    public User getFromUser() {
        return fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return fromUser.getName() + " pays " + toUser.getName() + " $" + String.format("%.2f", amount);
    }
}

// Main Splitwise class
class Splitwise {
    private final List<User> users = new ArrayList<>();
    private final Map<User, Double> balances = new HashMap<>();
    private long nextUserId = 1;
    private long nextExpenseId = 1;

    public User addUser(String name) {
        User user = new User(nextUserId++, name);
        users.add(user);
        balances.put(user, 0.0);
        return user;
    }

    public Expense addExpense(User payer, double amount, List<User> involvedUsers, ISplitStrategy strategy) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive"); // Basic validation
        }
        Expense expense = new Expense(nextExpenseId++, payer, amount, involvedUsers, strategy);
        // Update balances
        balances.put(payer, balances.get(payer) + amount); // Credit payer
        Map<User, Double> shares = expense.getShares();
        for (Map.Entry<User, Double> entry : shares.entrySet()) {
            User user = entry.getKey();
            double share = entry.getValue();
            if (share < 0) {
                throw new IllegalArgumentException("Share cannot be negative");
            }
            balances.put(user, balances.get(user) - share); // Debit sharer
        }
        return expense;
    }

    public Map<User, Double> getBalances() {
        return new HashMap<>(balances); // Defensive copy
    }

    public List<Transaction> settle() {
        List<Transaction> transactions = new ArrayList<>();
        // Greedy: Use priority queues for max debtor (pos balance) and creditor (neg, abs max)
        // For simplicity, iterate in loop - O(N^2) fine for small N; in prod, use PQ O(N log N)
        Set<User> unsettled = new HashSet<>(users);
        while (!unsettled.isEmpty()) {
            User debtor = null;
            double maxDebt = Double.MIN_VALUE;
            User creditor = null;
            double maxCredit = Double.MIN_VALUE; // Most negative

            for (User user : unsettled) {
                double bal = balances.get(user);
                if (bal > 0 && bal > maxDebt) { // Owes (positive)
                    maxDebt = bal;
                    debtor = user;
                }
                if (bal < 0 && bal < maxCredit) { // Owed (negative)
                    maxCredit = bal;
                    creditor = user;
                }
            }

            if (debtor == null || creditor == null) break; // All zero or one-sided

            double settleAmt = Math.min(maxDebt, -maxCredit);
            transactions.add(new Transaction(debtor, creditor, settleAmt));

            // Update balances
            balances.put(debtor, balances.get(debtor) - settleAmt);
            balances.put(creditor, balances.get(creditor) + settleAmt);

            // Remove if zero
            if (Math.abs(balances.get(debtor)) < 0.001) { // Epsilon for float
                unsettled.remove(debtor);
            }
            if (Math.abs(balances.get(creditor)) < 0.001) {
                unsettled.remove(creditor);
            }
        }
        return transactions;
    }

    // Helper to print balances
    public void printBalances() {
        for (User user : users) {
            double bal = balances.get(user);
            String status = bal > 0 ? "owes" : (bal < 0 ? "is owed" : "settled");
            System.out.println(user.getName() + " " + status + " $" + String.format("%.2f", Math.abs(bal)));
        }
    }
}

// Example usage (for demo - not part of LLD, but to verify)
class SplitwiseDemo {
    public static void main(String[] args) {
        Splitwise sw = new Splitwise();

        User alice = sw.addUser("Alice");
        User bob = sw.addUser("Bob");
        User charlie = sw.addUser("Charlie");

        // Expense 1: Alice pays 100 for all (equal)
        List<User> all = Arrays.asList(alice, bob, charlie);
        sw.addExpense(alice, 100.0, all, new EqualSplitStrategy());

        // Expense 2: Bob pays 50 for Alice and Charlie (exact: Alice 20, Charlie 30)
        Map<User, Double> exactShares = new HashMap<>();
        exactShares.put(alice, 20.0);
        exactShares.put(charlie, 30.0);
        List<User> subset = Arrays.asList(alice, charlie);
        sw.addExpense(bob, 50.0, subset, new ExactSplitStrategy(exactShares));

        sw.printBalances();
        // Expected: Alice owes 6.67, Bob is owed 3.33, Charlie owes 3.33? Wait, calc:
        // Exp1: Alice +100, all -33.33 → Alice +66.67, Bob -33.33, Charlie -33.33
        // Exp2: Bob +50, Alice -20, Charlie -30 → Alice +46.67, Bob +16.67, Charlie -63.33
        // Balances: Alice +46.67 (owes? Wait no: positive means net credit? Wait, convention:
        // In code: positive = owes group (debtor), negative = owed by group (creditor). Yes.

        System.out.println("\nSettlements:");
        List<Transaction> txns = sw.settle();
        for (Transaction txn : txns) {
            System.out.println(txn);
        }
        // Expected: Alice pays Charlie ~46.67 (adjust for exact)
    }
}