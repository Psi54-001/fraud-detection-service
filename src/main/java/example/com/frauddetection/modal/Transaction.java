package example.com.frauddetection.modal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Transaction(
        @NotNull @Min(0) Double amount,
        @NotNull Currency currency,
        @NotBlank String terminalId,
        @NotNull @Min(0) @Max(100) Integer treadScore,
        @NotBlank String cardNumber
) {}
