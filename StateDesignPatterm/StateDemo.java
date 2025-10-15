package com.StateDesignPatterm;

// StateDemo.java
// Single-file example of the State pattern (can be split into separate files)

interface State {
    void insertCoin();
    void ejectCoin();
    void pressButton();
    void dispense();
}

class VendingMachine {
    State noCoinState;
    State hasCoinState;
    State soldState;
    State soldOutState;

    State currentState;
    int count; // number of items left

    public VendingMachine(int itemCount) {
        noCoinState = new NoCoinState(this);
        hasCoinState = new HasCoinState(this);
        soldState = new SoldState(this);
        soldOutState = new SoldOutState(this);

        this.count = itemCount;
        if (itemCount > 0) currentState = noCoinState;
        else currentState = soldOutState;
    }

    // Actions delegated to current state
    public void insertCoin() {
        currentState.insertCoin();
    }

    public void ejectCoin() {
        currentState.ejectCoin();
    }

    public void pressButton() {
        currentState.pressButton();
        // pressing button may cause dispense() to be called by the state
    }

    void setState(State state) {
        this.currentState = state;
    }

    void releaseItem() {
        if (count > 0) {
            System.out.println("A snack comes rolling out...");
            count--;
        }
    }

    int getCount() {
        return count;
    }

    State getNoCoinState() { return noCoinState; }
    State getHasCoinState() { return hasCoinState; }
    State getSoldState() { return soldState; }
    State getSoldOutState() { return soldOutState; }

    @Override
    public String toString() {
        return "VendingMachine{items=" + count + ", state=" + currentState.getClass().getSimpleName() + "}";
    }
}

/* Concrete states */
class NoCoinState implements State {
    private VendingMachine machine;

    NoCoinState(VendingMachine m) { this.machine = m; }

    @Override
    public void insertCoin() {
        System.out.println("Coin inserted.");
        machine.setState(machine.getHasCoinState());
    }

    @Override
    public void ejectCoin() {
        System.out.println("You haven't inserted a coin.");
    }

    @Override
    public void pressButton() {
        System.out.println("You pressed the button but there's no coin.");
    }

    @Override
    public void dispense() {
        System.out.println("Insert coin first.");
    }
}

class HasCoinState implements State {
    private VendingMachine machine;

    HasCoinState(VendingMachine m) { this.machine = m; }

    @Override
    public void insertCoin() {
        System.out.println("You can't insert another coin.");
    }

    @Override
    public void ejectCoin() {
        System.out.println("Coin returned.");
        machine.setState(machine.getNoCoinState());
    }

    @Override
    public void pressButton() {
        System.out.println("Button pressed...");
        machine.setState(machine.getSoldState());
        machine.currentState.dispense();
    }

    @Override
    public void dispense() {
        System.out.println("No item dispensed.");
    }
}

class SoldState implements State {
    private VendingMachine machine;

    SoldState(VendingMachine m) { this.machine = m; }

    @Override
    public void insertCoin() {
        System.out.println("Please wait, we're already giving you a snack.");
    }

    @Override
    public void ejectCoin() {
        System.out.println("Sorry, you already pressed the button.");
    }

    @Override
    public void pressButton() {
        System.out.println("Pressing again won't help.");
    }

    @Override
    public void dispense() {
        machine.releaseItem();
        if (machine.getCount() > 0) {
            machine.setState(machine.getNoCoinState());
        } else {
            System.out.println("Oops, that was the last one.");
            machine.setState(machine.getSoldOutState());
        }
    }
}

class SoldOutState implements State {
    private VendingMachine machine;

    SoldOutState(VendingMachine m) { this.machine = m; }

    @Override
    public void insertCoin() {
        System.out.println("Machine is sold out. Can't accept coin.");
    }

    @Override
    public void ejectCoin() {
        System.out.println("You didn't insert a coin.");
    }

    @Override
    public void pressButton() {
        System.out.println("No item to dispense.");
    }

    @Override
    public void dispense() {
        System.out.println("No item dispensed.");
    }
}

/* Demo */
public class StateDemo {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine(2); // start with 2 items
        System.out.println(vm);

        // typical successful purchase
        vm.insertCoin();
        vm.pressButton();
        System.out.println(vm);

        // try to press button without coin
        vm.pressButton();

        // buy second item
        vm.insertCoin();
        vm.pressButton();
        System.out.println(vm);

        // try to buy when sold out
        vm.insertCoin();
        vm.ejectCoin();
        vm.pressButton();
    }
}
