package example.com.frauddetection.modal;

public record FraudResponse(
        TransactionStatus status,
        String rejectionMessage,
        int fraudScore
) {}
