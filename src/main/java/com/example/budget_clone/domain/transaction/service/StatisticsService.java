package com.example.budget_clone.domain.transaction.service;

import com.example.budget_clone.domain.transaction.dto.response.CategoryStatResponse;
import com.example.budget_clone.domain.transaction.dto.response.MonthlyStatisticsResponse;
import com.example.budget_clone.domain.transaction.entity.TransactionType;
import com.example.budget_clone.domain.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public MonthlyStatisticsResponse monthlyStatistics(Long userId, int year, int month, String accountNumber) {
        Long totalIncome;
        Long totalExpense;
        List<CategoryStatResponse> categoryStatResponses;

        if (accountNumber != null) {
            totalIncome = transactionRepository.sumByUserIdAndAccountAndTypeAndYearAndMonth(userId, accountNumber, TransactionType.INCOME, year, month);
            totalExpense = transactionRepository.sumByUserIdAndAccountAndTypeAndYearAndMonth(userId, accountNumber, TransactionType.EXPENSE, year, month);
            categoryStatResponses = transactionRepository.sumCategoryStatsByUserIdAndAccountAndYearAndMonth(userId, accountNumber, year, month);
        } else {
            totalIncome = transactionRepository.sumByUserIdAndTypeAndYearAndMonth(userId, TransactionType.INCOME, year, month);
            totalExpense = transactionRepository.sumByUserIdAndTypeAndYearAndMonth(userId, TransactionType.EXPENSE, year, month);
            categoryStatResponses = transactionRepository.sumCategoryStatsByUserIdAndYearAndMonth(userId, year, month);
        }

        return new MonthlyStatisticsResponse(totalIncome, totalExpense, categoryStatResponses);
    }
}
