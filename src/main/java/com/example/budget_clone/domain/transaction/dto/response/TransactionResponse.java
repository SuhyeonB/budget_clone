package com.example.budget_clone.domain.transaction.dto.response;

import com.example.budget_clone.domain.transaction.entity.Category;
import com.example.budget_clone.domain.transaction.entity.Transaction;
import com.example.budget_clone.domain.transaction.entity.TransactionType;

import java.time.LocalDate;

public record TransactionResponse (Long transactionId, String nickname, TransactionType type, Category category,
                                   String description, String accountNumber, Long amount, LocalDate date) {

    public static TransactionResponse from (Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getUser().getNickname(),
                transaction.getType(),
                transaction.getCategory(),
                transaction.getDescription(),
                transaction.getAccount().getAccountNumber(),
                transaction.getAmount(),
                transaction.getDate()
        );
    }
}
