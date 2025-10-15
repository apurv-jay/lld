package com.strategy;

public class CreditCardPayment implements PaymentStrategy {
    private final String cardNumber;
    private final String cardHolder;
    private final String cvv;
    private final String expiryDate;

    public CreditCardPayment(String cardNumber, String cardHolder, String cvv, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using Credit Card [" +
                cardHolder + ", card ending " +
                cardNumber.substring(cardNumber.length()-4) + "]");
    }
}
