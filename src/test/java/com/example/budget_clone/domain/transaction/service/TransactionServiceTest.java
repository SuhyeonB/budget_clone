package com.example.budget_clone.domain.transaction.service;

import com.example.budget_clone.domain.account.entity.Account;
import com.example.budget_clone.domain.account.service.AccountService;
import com.example.budget_clone.domain.transaction.dto.request.CreateTransactionRequest;
import com.example.budget_clone.domain.transaction.dto.response.TransactionResponse;
import com.example.budget_clone.domain.transaction.entity.Category;
import com.example.budget_clone.domain.transaction.entity.TransactionType;
import com.example.budget_clone.domain.transaction.repository.TransactionRepository;
import com.example.budget_clone.domain.user.entity.User;
import com.example.budget_clone.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private AccountService accountService;

    @Test
    @DisplayName("success_to_income_transaction")
    void createTransaction_INCOME_balance_increase() {
        // given
        User user = User.builder()
                .email("test@test.com")
                .password("password1234")
                .nickname("tester")
                .build();
        Account account = Account.builder()
                .user(user)
                .bank("KB")
                .accountNumber("1234567890")
                .balance(500000L)
                .build();
        CreateTransactionRequest dto = CreateTransactionRequest.builder()
                .type(TransactionType.INCOME)
                .category(null)
                .description("3월 월급")
                .accountNumber("1234567890")
                .amount(500000L)
                .date(LocalDate.now())
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(account, "id", 1L);

        when(userService.findUser(1L)).thenReturn(user);
        when(accountService.findAccount("1234567890")).thenReturn(account);

        // when
        TransactionResponse response = transactionService.createTransaction(1L, dto);

        // then
        assertThat(account.getBalance()).isEqualTo(1000000L);
    }

    @Test
    @DisplayName("success_to_expense_transaction")
    void createTransaction_EXPENSE_balance_decrease() {
        // given
        User user = User.builder().email("test@test.com").password("password1234").nickname("tester").build();
        Account account = Account.builder().user(user).bank("KB").accountNumber("12345").balance(10000L).build();

        CreateTransactionRequest dto = CreateTransactionRequest.builder()
                .type(TransactionType.EXPENSE)
                .category(Category.COFFEE)
                .description("아이스 아메리키노")
                .accountNumber("12345")
                .amount(1800L)
                .date(LocalDate.now())
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(account, "id", 1L);

        when(userService.findUser(1L)).thenReturn(user);
        when(accountService.findAccount("12345")).thenReturn(account);

        // when
        TransactionResponse response = transactionService.createTransaction(1L, dto);

        // then
        assertThat(account.getBalance()).isEqualTo(8200L);
    }

    @Test
    void createTransaction_fail() {
    }
}