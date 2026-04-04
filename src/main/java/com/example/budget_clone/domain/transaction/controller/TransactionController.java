package com.example.budget_clone.domain.transaction.controller;

import com.example.budget_clone.domain.transaction.dto.request.CreateTransactionRequest;
import com.example.budget_clone.domain.transaction.dto.request.UpdateTransactionRequest;
import com.example.budget_clone.domain.transaction.dto.response.TransactionResponse;
import com.example.budget_clone.domain.transaction.service.TransactionService;
import com.example.budget_clone.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateTransactionRequest dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(userDetails.getUserId(), dto));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction (
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long transactionId
    ) {
        return ResponseEntity.ok(transactionService.getTransaction(userDetails.getUserId(), transactionId));
    }

    @PatchMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> updateTransaction (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long transactionId,
            @RequestBody UpdateTransactionRequest dto
    ) {
        return ResponseEntity.ok(transactionService.updateTransaction(userDetails.getUserId(), transactionId, dto));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long transactionId
    ) {
        transactionService.deleteTransaction(userDetails.getUserId(), transactionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
