package com.example.budget_clone.domain.account.entity;

import com.example.budget_clone.domain.user.entity.User;
import com.example.budget_clone.global.entity.Timestamped;
import com.example.budget_clone.global.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "accounts")
public class Account extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String bank;

    @Column(unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private Long balance = 0L;

    @Builder
    public Account(User user, String bank, String accountNumber, Long balance) {
        this.user = user;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void deposit(Long amount) {
        this.balance += amount;
    }

    public void withdraw(Long amount) {
        if (this.balance < amount) {
            throw new BadRequestException("잔액이 부족합니다.");
        }
        this.balance -= amount;
    }
}
