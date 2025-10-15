```mermaid
classDiagram
direction LR

    PaymentStrategy <|.. CreditCardPayment
    PaymentStrategy <|.. PayPalPayment
    PaymentStrategy <|.. CryptoPayment

    class PaymentStrategy {
      <<interface>>
      + pay(amount: double)
    }

    class CreditCardPayment {
      - cardNumber: String
      - cardHolder: String
      - cvv: String
      - expiryDate: String
      + pay(amount: double)
    }

    class PayPalPayment {
      - email: String
      + pay(amount: double)
    }

    class CryptoPayment {
      - walletAddress: String
      + pay(amount: double)
    }

    class PaymentFactory {
      + createPayment(type: String, params…): PaymentStrategy
    }

    class PaymentContext {
      - strategy: PaymentStrategy
      + setPaymentStrategy(PaymentStrategy)
      + pay(amount: double)
    }

    PaymentFactory --> PaymentStrategy : returns
    PaymentContext --> PaymentFactory : uses to get strategy
    PaymentContext --> PaymentStrategy : delegates pay()
