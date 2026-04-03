package com.example.budget_clone.domain.account.dto.response;

import com.example.budget_clone.domain.account.entity.Account;

public record AccountResponse(String nickname, String bank, String accountNumber, Long balance) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getUser().getNickname(),
                account.getBank(),
                account.getAccountNumber(),
                account.getBalance()
        );
    }
}
