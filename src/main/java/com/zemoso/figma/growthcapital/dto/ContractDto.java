package com.zemoso.figma.growthcapital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ContractDto {
    @NotBlank(message = "Name should not be empty")
    private String name;

    @NotBlank(message = "Contact type should not be empty")
    private String type;

    @NotNull(message = "contract term length should not be empty")
    private Integer termLength;

    @NotNull(message = "Contract type per payment should not be empty")
    private BigDecimal perPayment;

    @NotNull(message = "Rate percentage should not be empty")
    private Integer rate;
}
