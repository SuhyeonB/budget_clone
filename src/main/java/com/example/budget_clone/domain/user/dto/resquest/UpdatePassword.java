package com.example.budget_clone.domain.user.dto.resquest;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePassword {
    private String oldPassword;
    private String newPassword;
}
