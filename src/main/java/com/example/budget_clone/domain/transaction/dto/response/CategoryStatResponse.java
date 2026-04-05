package com.example.budget_clone.domain.transaction.dto.response;

import com.example.budget_clone.domain.transaction.entity.Category;

public record CategoryStatResponse(Category category, Long amount) {
}
