package com.example.budget_clone.domain.transaction.service;

import com.example.budget_clone.domain.account.entity.Account;
import com.example.budget_clone.domain.account.service.AccountService;
import com.example.budget_clone.domain.transaction.dto.request.CreateTransactionRequest;
import com.example.budget_clone.domain.transaction.dto.request.UpdateTransactionRequest;
import com.example.budget_clone.domain.transaction.dto.response.TransactionResponse;
import com.example.budget_clone.domain.transaction.entity.Transaction;
import com.example.budget_clone.domain.transaction.entity.TransactionType;
import com.example.budget_clone.domain.transaction.repository.TransactionRepository;
import com.example.budget_clone.domain.user.entity.User;
import com.example.budget_clone.domain.user.service.UserService;
import com.example.budget_clone.global.exception.ForbiddenException;
import com.example.budget_clone.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final AccountService accountService;

    @Transactional
    public TransactionResponse createTransaction(Long userId, CreateTransactionRequest dto) {
        User user = userService.findUser(userId);
        Account account = accountService.findAccount(dto.getAccountNumber());

        if (!account.getUser().getId().equals(userId)) {
            throw new ForbiddenException("본인의 게좌만 접근할 수 있습니다.");
        }

        if (dto.getType().equals(TransactionType.INCOME)) {
            account.deposit(dto.getAmount());
        } else {
            account.withdraw(dto.getAmount());
        }

        Transaction transaction = Transaction.builder()
                .user(user)
                .type(dto.getType())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .account(account)
                .amount(dto.getAmount())
                .date(dto.getDate())
                .build();

        transactionRepository.save(transaction);

        return TransactionResponse.from(transaction);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("해당 트랜잭션을 찾을 수 없습니다."));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new ForbiddenException("본인의 트랜잭션만 접근할 수 있습니다.");
        }

        return TransactionResponse.from(transaction);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactions(Long userId, String accountNumber, Pageable pageable) {
        User user = userService.findUser(userId);
        Account account = accountService.findAccount(accountNumber);

        if (!account.getUser().getId().equals(userId)) {
            throw new ForbiddenException("본인의 게좌만 접근할 수 있습니다.");
        }

        return transactionRepository.findByUserAndAccount(user, account, pageable)
                .map(TransactionResponse::from);
    }

    @Transactional
    public TransactionResponse updateTransaction(Long userId, Long transactionId, UpdateTransactionRequest dto) {
        User user = userService.findUser(userId);
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("해당 트랜잭션을 찾을 수 없습니다."));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new ForbiddenException("본인의 트랜잭션만 접근할 수 있습니다.");
        }

        Account account = accountService.findAccount(transaction.getAccount().getAccountNumber());

        if (transaction.getType().equals(TransactionType.INCOME)) {
            account.withdraw(transaction.getAmount());
            account.deposit(dto.getAmount());
        } else {
            account.deposit(transaction.getAmount());
            account.withdraw(dto.getAmount());
        }

        transaction.update(dto.getCategory(), dto.getDescription(), dto.getAmount(), dto.getDate());

        return TransactionResponse.from(transaction);
    }

    @Transactional
    public void deleteTransaction (Long userId, Long transactionId) {
        User user = userService.findUser(userId);
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("해당 트랜잭션을 찾을 수 없습니다."));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new ForbiddenException("본인의 트랜잭션만 접근할 수 있습니다.");
        }

        Account account = accountService.findAccount(transaction.getAccount().getAccountNumber());

        if (transaction.getType().equals(TransactionType.INCOME)) {
            account.withdraw(transaction.getAmount());
        } else {
            account.deposit(transaction.getAmount());
        }

        transactionRepository.delete(transaction);
    }
}
