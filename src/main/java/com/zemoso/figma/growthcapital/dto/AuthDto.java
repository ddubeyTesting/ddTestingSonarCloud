package com.zemoso.figma.growthcapital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDto {

    @NotNull(message = "Email address required")
    private String email;

    @NotNull(message = "Password required")
    private String password;
}
