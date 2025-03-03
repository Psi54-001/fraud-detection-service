package example.com.frauddetection.reposotory;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FraudDetectionRepositoryImplMock implements FraudDetectionRepository {

    private static final Set<String> ALLOWED_TERMINALS = Set.of("T1", "T2", "T3", "T4", "T5");
    @Override
    public boolean verifyTerminal(String terminalId) {
        return ALLOWED_TERMINALS.contains(terminalId);
    }
}
