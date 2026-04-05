package com.example.budget_clone.domain.transaction.repository;

import com.example.budget_clone.domain.account.entity.Account;
import com.example.budget_clone.domain.transaction.dto.response.CategoryStatResponse;
import com.example.budget_clone.domain.transaction.entity.Transaction;
import com.example.budget_clone.domain.transaction.entity.TransactionType;
import com.example.budget_clone.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByUserAndAccount(User user, Account account, Pageable pageable);

    // 전체 계좌 기준
    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = :type AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
    Long sumByUserIdAndTypeAndYearAndMonth(@Param("userId") Long userId, @Param("type") TransactionType type,
                                           @Param("year") int year, @Param("month") int month);

    // 특정 계좌 기준
    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.account.accountNumber = :accountNumber AND t.type = :type AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
    Long sumByUserIdAndAccountAndTypeAndYearAndMonth(@Param("userId") Long userId, @Param("accountNumber") String accountNumber,
                                                     @Param("type") TransactionType type, @Param("year") int year, @Param("month") int month);

    // 전체 계좌 카테고리별
    @Query("SELECT new com.example.budget_clone.domain.transaction.dto.response.CategoryStatResponse(t.category, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = com.example.budget_clone.domain.transaction.entity.TransactionType.EXPENSE AND YEAR(t.date) = :year AND MONTH(t.date) = :month " +
            "GROUP BY t.category")
    List<CategoryStatResponse> sumCategoryStatsByUserIdAndYearAndMonth(@Param("userId") Long userId,
                                                                       @Param("year") int year, @Param("month") int month);

    // 특정 계좌 카테고리별
    @Query("SELECT new com.example.budget_clone.domain.transaction.dto.response.CategoryStatResponse(t.category, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.account.accountNumber = :accountNumber AND t.type = com.example.budget_clone.domain.transaction.entity.TransactionType.EXPENSE AND YEAR(t.date) = :year AND MONTH(t.date) = :month " +
            "GROUP BY t.category")
    List<CategoryStatResponse> sumCategoryStatsByUserIdAndAccountAndYearAndMonth(@Param("userId") Long userId, @Param("accountNumber") String accountNumber,
                                                                                 @Param("year") int year, @Param("month") int month);
}
