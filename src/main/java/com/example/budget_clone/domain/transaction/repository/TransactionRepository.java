package com.example.budget_clone.domain.transaction.repository;

import com.example.budget_clone.domain.account.entity.Account;
import com.example.budget_clone.domain.transaction.entity.Transaction;
import com.example.budget_clone.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByUserAndAccount(User user, Account account, Pageable pageable);
}
