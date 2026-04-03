package com.example.budget_clone.domain.account.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAccountRequest {

    private String bank;

    @Pattern(regexp = "^[0-9]+$")
    private String accountNumber;

    @Min(0)
    private Long balance;
}
