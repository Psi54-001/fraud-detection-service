package example.com.frauddetection.service;

import example.com.frauddetection.modal.Currency;
import example.com.frauddetection.reposotory.FraudDetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class FraudDetectionServiceImpl implements FraudDetectionService {

    private final FraudDetectionRepository repository;

    @Autowired
    public FraudDetectionServiceImpl(FraudDetectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public int verifyTerminal(String terminalId) {
        if (!repository.verifyTerminal(terminalId)) {
            return 50;
        }
        return 0;
    }

    @Override
    public int verifyAmountAgainstThreadScore(Currency currency, double amount, int treadScore) {
        double maxAllowedAmount = calculateMaxAllowedAmount(currency, treadScore);

        if (amount > maxAllowedAmount) {
            return 100;
        }
        return 0;
    }

    private double calculateMaxAllowedAmount(Currency currency, int threatScore) {
        double maxAmount;

        // Rule for restricting transaction amounts based on terminal threat score
        if (threatScore >= 80) {
            maxAmount = 500;
        } else if (threatScore >= 50) {
            maxAmount = 1000;
        } else {
            maxAmount = 2000;
        }

        // Adjust allowed amount based on currency
        if (Currency.USD.equals(currency)) {
            maxAmount *= 1.0;
        } else if (Currency.EUR.equals(currency)) {
            maxAmount *= 1.1;
        } else if (Currency.DKK.equals(currency)) {
            maxAmount *= 0.5;
        } else {
            maxAmount *= 0.8;
        }
        return maxAmount;
    }
}

