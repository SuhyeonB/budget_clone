package com.example.budget_clone.domain.account.service;

import com.example.budget_clone.domain.account.dto.request.CreateAccountRequest;
import com.example.budget_clone.domain.account.dto.response.AccountResponse;
import com.example.budget_clone.domain.account.entity.Account;
import com.example.budget_clone.domain.account.repository.AccountRepository;
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
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;

    @Transactional
    public AccountResponse createAccount (Long userId, CreateAccountRequest dto) {
        User user = userService.findUser(userId);

        Account account = Account.builder()
                .user(user)
                .bank(dto.getBank())
                .accountNumber(dto.getAccountNumber())
                .balance(dto.getBalance())
                .build();

        accountRepository.save(account);

        return AccountResponse.from(account);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(Long userId, String accountNumber) {
        User user = userService.findUser(userId);

        Account account = findAccount(accountNumber);

        if (!account.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("본인의 계좌가 아닙니다.");
        }

        return AccountResponse.from(account);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponse> getAllAccounts (Long userId, Pageable pageable) {
        User user = userService.findUser(userId);

        return accountRepository.findByUserId(userId, pageable)
                .map(AccountResponse::from);
    }

    @Transactional
    public void deleteAccount (Long userId, Long accountId) {
        User user = userService.findUser(userId);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("해당 번호의 계좌는 존재하지 않습니다."));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("본인의 계좌가 아닙니다.");
        }

        accountRepository.delete(account);
    }

    public Account findAccount (String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("해당 번호의 계좌는 존재하지 않습니다."));
    }
}
