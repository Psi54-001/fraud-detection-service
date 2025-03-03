package example.com.frauddetection.service;

import example.com.frauddetection.modal.Currency;
import example.com.frauddetection.reposotory.FraudDetectionRepositoryImplMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class FraudDetectionServiceTest {

    @Mock
    FraudDetectionRepositoryImplMock repository;
    @InjectMocks
    FraudDetectionServiceImpl fraudDetectionService;

    @Test
    public void verifyAmountAgainstThreadScore_givenHighTreadScoreAndAmountHigherThenMaxAllowed_thenThrowServiceException() {
        Integer fraudScore = assertDoesNotThrow(
                () -> fraudDetectionService.verifyAmountAgainstThreadScore(Currency.USD, 600, 85));

        assertEquals(100, fraudScore);
    }

    @Test
    public void verifyAmountAgainstThreadScore_givenMediumTreadScoreAndAmountHigherThenMaxAllowed_thenThrowServiceException() {
        Integer fraudScore = assertDoesNotThrow(
                () -> fraudDetectionService.verifyAmountAgainstThreadScore(Currency.USD, 1100, 50));

        assertEquals(100, fraudScore);
    }

    @Test
    public void verifyAmountAgainstThreadScore_givenLowTreadScoreAndAmountHigherThenMaxAllowed_thenThrowServiceException() {
        Integer fraudScore = assertDoesNotThrow(
                () -> fraudDetectionService.verifyAmountAgainstThreadScore(Currency.USD, 2100, 49));

        assertEquals(100, fraudScore);
    }

    @Test
    public void verifyAmountAgainstThreadScore_givenHighTreadScoreAndAmountLowerThenMaxAllowed_thenReturnFraudScore() {
        Integer fraudScore = assertDoesNotThrow(
                () -> fraudDetectionService.verifyAmountAgainstThreadScore(Currency.USD, 400, 85));

        assertEquals(0, fraudScore);
    }

    @Test
    public void verifyAmountAgainstThreadScore_givenMediumTreadScoreAndAmountLowerThenMaxAllowed_thenReturnFraudScore() {
        Integer fraudScore = assertDoesNotThrow(
                () -> fraudDetectionService.verifyAmountAgainstThreadScore(Currency.USD, 900, 50));

        assertEquals(0, fraudScore);
    }

    @Test
    public void verifyAmountAgainstThreadScore_givenLowTreadScoreAndAmountLowerThenMaxAllowed_thenReturnFraudScore() {
        Integer fraudScore = assertDoesNotThrow(
                () -> fraudDetectionService.verifyAmountAgainstThreadScore(Currency.USD, 1900, 49));

        assertEquals(0, fraudScore);
    }

    @Test
    public void verifyTerminal_givenTerminalIsKnown_thenReturnFraudScoreOfZero() {
        String terminalId = "terminalId";
        doReturn(true)
                .when(repository)
                .verifyTerminal(eq(terminalId));
        Integer fraudScore = fraudDetectionService.verifyTerminal(terminalId);

        assertEquals(0, fraudScore);
    }

    @Test
    public void verifyTerminal_givenTerminalIsUnknown_thenReturnFraudScoreOf50() {
        String terminalId = "terminalId";
        doReturn(false)
                .when(repository)
                .verifyTerminal(eq(terminalId));
        Integer fraudScore = fraudDetectionService.verifyTerminal(terminalId);

        assertEquals(50, fraudScore);
    }

}