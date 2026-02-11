package com.kristag.pft.controller;

import com.kristag.pft.dto.AccountCreateRequest;
import com.kristag.pft.dto.AccountResponse;
import com.kristag.pft.dto.AccountUpdateRequest;
import com.kristag.pft.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(Authentication auth, @Valid @RequestBody AccountCreateRequest req) {
        UUID userId = (UUID) auth.getPrincipal();
        return accountService.create(userId, req);
    }

    @GetMapping
    public List<AccountResponse> list(Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        return accountService.list(userId);
    }

    @GetMapping("/{id}")
    public AccountResponse get(Authentication auth, @PathVariable UUID id) {
        UUID userId = (UUID) auth.getPrincipal();
        return accountService.get(userId, id);
    }

    @PutMapping("/{id}")
    public AccountResponse update(Authentication auth, @PathVariable UUID id, @Valid @RequestBody AccountUpdateRequest req) {
        UUID userId = (UUID) auth.getPrincipal();
        return accountService.update(userId, id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication auth, @PathVariable UUID id) {
        UUID userId = (UUID) auth.getPrincipal();
        accountService.delete(userId, id);
    }
}