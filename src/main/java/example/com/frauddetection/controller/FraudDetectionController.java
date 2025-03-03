package example.com.frauddetection.controller;

import example.com.frauddetection.service.FraudDetectionService;
import example.com.frauddetection.modal.Transaction;
import example.com.frauddetection.modal.FraudResponse;
import example.com.frauddetection.modal.TransactionStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/fraud-detection")
public class FraudDetectionController {

    private final FraudDetectionService fraudDetectionService;

    @Autowired
    public FraudDetectionController(FraudDetectionService fraudDetectionService) {
        this.fraudDetectionService = fraudDetectionService;
    }

    @PostMapping("/")
    public FraudResponse detectFraud(@RequestBody @Valid Transaction request) {
        int fraudScore = fraudDetectionService.verifyTerminal(request.terminalId());

        fraudScore += fraudDetectionService.verifyAmountAgainstThreadScore(
                request.currency(),
                request.amount(),
                request.treadScore());
        if (fraudScore >= 100) {
            return new FraudResponse(
                    TransactionStatus.REJECTED,
                    "Fraud score is to high.",
                    fraudScore);
        }
        return new FraudResponse(
                TransactionStatus.APPROVED,
                "Transaction is approved.",
                fraudScore);

    }
}
