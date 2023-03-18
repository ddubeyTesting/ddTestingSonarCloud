package com.zemoso.figma.growthcapital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserCreditDetailsDto {
    private BigDecimal availableCredit;

    private BigDecimal outstandingAmount;

    @NotNull(message = "Currency string should not be empty")
    private String currency;
}
