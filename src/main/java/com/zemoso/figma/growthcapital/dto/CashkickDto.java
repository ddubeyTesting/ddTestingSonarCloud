package com.zemoso.figma.growthcapital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CashkickDto {

    @NotNull(message = "User id for the cash kick should not be empty")
    private Long userId;

    @NotNull(message = "Cash kick name should not be empty")
    private String name;

    @NotNull(message = "Contracts list should not be empty")
    private List<Long> contractIds;

    private String status;
}
