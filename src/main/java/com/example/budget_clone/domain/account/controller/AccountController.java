package com.example.budget_clone.domain.account.controller;

import com.example.budget_clone.domain.account.dto.request.CreateAccountRequest;
import com.example.budget_clone.domain.account.dto.response.AccountResponse;
import com.example.budget_clone.domain.account.service.AccountService;
import com.example.budget_clone.domain.transaction.dto.response.TransactionResponse;
import com.example.budget_clone.domain.transaction.service.TransactionService;
import com.example.budget_clone.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateAccountRequest dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(userDetails.getUserId(), dto));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String accountNumber
    ) {
        return ResponseEntity.ok(accountService.getAccount(userDetails.getUserId(), accountNumber));
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactionByAccount (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String accountNumber,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(transactionService.getTransactions(userDetails.getUserId(), accountNumber, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<AccountResponse>> getAllAccounts (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(accountService.getAllAccounts(userDetails.getUserId(), pageable));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long accountId
    ) {
        accountService.deleteAccount(userDetails.getUserId(), accountId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
