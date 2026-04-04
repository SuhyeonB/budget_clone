package com.example.budget_clone.domain.transaction.entity;

import com.example.budget_clone.domain.account.entity.Account;
import com.example.budget_clone.domain.user.entity.User;
import com.example.budget_clone.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Category category;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private LocalDate date;

    @Builder

    public Transaction(User user, TransactionType type, Category category,
                       String description, Account account, Long amount, LocalDate date) {
        this.user = user;
        this.type = type;
        this.category = category;
        this.description = description;
        this.account = account;
        this.amount = amount;
        this.date = date;
    }

    public void update(Category category, String description, Long amount, LocalDate date) {
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }
}
