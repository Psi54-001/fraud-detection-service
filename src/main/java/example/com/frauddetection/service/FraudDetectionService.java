package example.com.frauddetection.service;

import example.com.frauddetection.modal.Currency;

public interface FraudDetectionService {

    /**
     * Rule used to determine if a terminal is known or unknown. When unknown the fraud score is returned with higher value used to adjust the tread score
     *
     * @param terminalId id for terminals committing transaction
     * @return an integer that represents the chance of transaction being fraud
     */
    int verifyTerminal(String terminalId);

    /**
     * Rule used to determine if the current amount is allowed based on the terminals thread score
     *
     * @param currency   currency type
     * @param amount     currency amount
     * @param treadScore terminal thread score
     * @return fraud score
     */
    int verifyAmountAgainstThreadScore(Currency currency, double amount, int treadScore);
}
