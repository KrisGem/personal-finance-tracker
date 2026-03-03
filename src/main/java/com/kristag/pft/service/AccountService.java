package com.kristag.pft.service;

import com.kristag.pft.controller.error.NotFoundException;
import com.kristag.pft.domain.entity.Account;
import com.kristag.pft.domain.entity.User;
import com.kristag.pft.domain.repository.AccountRepository;
import com.kristag.pft.domain.repository.UserRepository;
import com.kristag.pft.dto.AccountCreateRequest;
import com.kristag.pft.dto.AccountResponse;
import com.kristag.pft.dto.AccountUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public AccountResponse create(UUID userId, AccountCreateRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        Account account = new Account(
                user,
                req.name(),
                req.currency().toUpperCase(),
                req.openingBalance()
        );

        return toResponse(accountRepository.save(account));
    }

    public List<AccountResponse> list(UUID userId) {
        return accountRepository.findAllByUser_Id(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public AccountResponse get(UUID userId, UUID accountId) {
        Account acc = accountRepository.findByIdAndUser_Id(accountId, userId)
                .orElseThrow(() -> new NotFoundException("Account not found: " + accountId));
        return toResponse(acc);
    }

    public AccountResponse update(UUID userId, UUID accountId, AccountUpdateRequest req) {
        Account acc = accountRepository.findByIdAndUser_Id(accountId, userId)
                .orElseThrow(() -> new NotFoundException("Account not found: " + accountId));

        acc.update(req.name(), req.currency().toUpperCase(), req.openingBalance());
        return toResponse(accountRepository.save(acc));
    }

    public void delete(UUID userId, UUID accountId) {
        Account acc = accountRepository.findByIdAndUser_Id(accountId, userId)
                .orElseThrow(() -> new NotFoundException("Account not found: " + accountId));
        accountRepository.delete(acc);
    }

    private AccountResponse toResponse(Account a) {
        return new AccountResponse(
                a.getId(),
                a.getName(),
                a.getCurrency(),
                a.getOpeningBalance(),
                a.getCreatedAt()
        );
    }
}