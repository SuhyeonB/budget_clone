package com.example.budget_clone.domain.transaction.dto.request;

import com.example.budget_clone.domain.transaction.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateTransactionRequest {

    private Category category;
    private String description;
    private Long amount;
    private LocalDate date;
}
