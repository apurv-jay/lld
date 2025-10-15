package com.strategy;

public class StrategyPatternDemo {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext();

        System.out.println("== Using Credit Card ==");
        context.setPaymentStrategy(new CreditCardPayment(
                "1234567890123456", "Alice Kumar", "123", "12/27"
        ));
        context.pay(1500.00);

        System.out.println("\n== Switching to PayPal ==");
        context.setPaymentStrategy(new PayPalPayment("alice.k@example.com"));
        context.pay(2400.50);

        System.out.println("\n== Switching to Crypto ==");
        context.setPaymentStrategy(new CryptoPayment("wallet-abc-xyz-123"));
        context.pay(3500.75);
    }
}
