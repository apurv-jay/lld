package com.ATM;

import java.util.Scanner;

// State interface
interface State {
    void insertCard(ATM machine);
    void enterPin(ATM machine, int pin);
    void selectTransaction(ATM machine, String type);
    void performTransaction(ATM machine, double amount);
    void ejectCard(ATM machine);
}

// IdleState class
class IdleState implements State {
    @Override
    public void insertCard(ATM machine) {
        machine.setHasCard(true);
        machine.setState(new HasCardState());
        System.out.println("Card inserted. Please enter PIN.");
    }

    @Override
    public void enterPin(ATM machine, int pin) {
        System.out.println("Insert card first.");
    }

    @Override
    public void selectTransaction(ATM machine, String type) {
        System.out.println("Insert card and enter PIN first.");
    }

    @Override
    public void performTransaction(ATM machine, double amount) {
        System.out.println("Insert card and enter PIN first.");
    }

    @Override
    public void ejectCard(ATM machine) {
        System.out.println("No card to eject.");
    }
}

// HasCardState class
class HasCardState implements State {
    @Override
    public void insertCard(ATM machine) {
        System.out.println("Card already inserted.");
    }

    @Override
    public void enterPin(ATM machine, int pin) {
        if (pin == machine.getPin()) {
            machine.setState(new AuthenticatedState());
            System.out.println("PIN correct. Select transaction.");
        } else {
            System.out.println("Invalid PIN. Try again.");
        }
    }

    @Override
    public void selectTransaction(ATM machine, String type) {
        System.out.println("Enter PIN first.");
    }

    @Override
    public void performTransaction(ATM machine, double amount) {
        System.out.println("Enter PIN first.");
    }

    @Override
    public void ejectCard(ATM machine) {
        machine.setHasCard(false);
        machine.setState(new IdleState());
        System.out.println("Card ejected.");
    }
}

// AuthenticatedState class
class AuthenticatedState implements State {
    private String transactionType;

    @Override
    public void insertCard(ATM machine) {
        System.out.println("Card already inserted.");
    }

    @Override
    public void enterPin(ATM machine, int pin) {
        System.out.println("PIN already entered.");
    }

    @Override
    public void selectTransaction(ATM machine, String type) {
        if (type.equalsIgnoreCase("withdraw") || type.equalsIgnoreCase("deposit") || type.equalsIgnoreCase("balance")) {
            this.transactionType = type;
            if (type.equalsIgnoreCase("balance")) {
                performTransaction(machine, 0); // No amount needed for balance
            } else {
                System.out.println("Enter amount:");
            }
        } else {
            System.out.println("Invalid transaction type.");
        }
    }

    @Override
    public void performTransaction(ATM machine, double amount) {
        if (transactionType == null) {
            System.out.println("Select transaction first.");
            return;
        }
        switch (transactionType.toLowerCase()) {
            case "withdraw":
                if (amount > machine.getBalance()) {
                    System.out.println("Insufficient funds.");
                } else {
                    machine.setBalance(machine.getBalance() - amount);
                    System.out.println("Withdrew $" + amount + ". New balance: $" + machine.getBalance());
                }
                break;
            case "deposit":
                machine.setBalance(machine.getBalance() + amount);
                System.out.println("Deposited $" + amount + ". New balance: $" + machine.getBalance());
                break;
            case "balance":
                System.out.println("Balance: $" + machine.getBalance());
                break;
        }
        transactionType = null; // Reset for next transaction
    }

    @Override
    public void ejectCard(ATM machine) {
        machine.setHasCard(false);
        machine.setState(new IdleState());
        System.out.println("Card ejected. Session ended.");
    }
}

// ATM class
public class ATM {
    private State state;
    private double balance = 1000.0;
    private int pin = 1234;
    private boolean hasCard = false;

    public ATM() {
        this.state = new IdleState();
    }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public int getPin() { return pin; }
    public boolean hasCard() { return hasCard; }
    public void setHasCard(boolean hasCard) { this.hasCard = hasCard; }
    public void setState(State state) { this.state = state; }

    public void insertCard() { state.insertCard(this); }
    public void enterPin(int pin) { state.enterPin(this, pin); }
    public void selectTransaction(String type) { state.selectTransaction(this, type); }
    public void performTransaction(double amount) { state.performTransaction(this, amount); }
    public void ejectCard() { state.ejectCard(this); }

    public static void main(String[] args) {
        ATM atm = new ATM();
        Scanner scanner = new Scanner(System.in);

        // Simulate user interaction
        System.out.println("ATM Simulation. Commands: insert, pin <num>, select <type>, perform <amount>, eject, exit");
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) break;

            String[] parts = input.split(" ");
            switch (parts[0].toLowerCase()) {
                case "insert":
                    atm.insertCard();
                    break;
                case "pin":
                    if (parts.length > 1) atm.enterPin(Integer.parseInt(parts[1]));
                    break;
                case "select":
                    if (parts.length > 1) atm.selectTransaction(parts[1]);
                    break;
                case "perform":
                    if (parts.length > 1) atm.performTransaction(Double.parseDouble(parts[1]));
                    break;
                case "eject":
                    atm.ejectCard();
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }
        scanner.close();
    }
}