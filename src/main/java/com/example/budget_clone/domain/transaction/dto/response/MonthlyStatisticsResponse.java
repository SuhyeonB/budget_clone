package com.example.budget_clone.domain.transaction.dto.response;

import java.util.List;

public record MonthlyStatisticsResponse(Long totalIncome, Long totalExpense, List<CategoryStatResponse> categoryStats) {
}
