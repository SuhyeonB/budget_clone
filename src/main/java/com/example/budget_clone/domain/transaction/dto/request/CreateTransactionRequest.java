package com.example.budget_clone.domain.transaction.dto.request;

import com.example.budget_clone.domain.transaction.entity.Category;
import com.example.budget_clone.domain.transaction.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTransactionRequest {
    private TransactionType type;
    private Category category;
    private String description;
    private String accountNumber;
    private Long amount;
    private LocalDate date;
}
