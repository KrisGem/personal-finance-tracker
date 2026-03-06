package com.kristag.pft.controller;

import com.kristag.pft.dto.AccountCreateRequest;
import com.kristag.pft.dto.AccountResponse;
import com.kristag.pft.dto.AccountUpdateRequest;
import com.kristag.pft.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
@Validated
@Tag(name = "Accounts", description = "Manage user accounts (e.g. cash, bank, savings)")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create account")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(Authentication auth, @Valid @RequestBody AccountCreateRequest req) {
        UUID userId = UUID.fromString(auth.getName());
        return accountService.create(userId, req);
    }

    @Operation(summary = "List accounts")
    @GetMapping
    public List<AccountResponse> list(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        return accountService.list(userId);
    }

    @Operation(summary = "Get account by id")
    @GetMapping("/{id}")
    public AccountResponse get(
            Authentication auth,
            @Parameter(description = "Account id") @PathVariable @NotNull UUID id
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return accountService.get(userId, id);
    }

    @Operation(summary = "Update account")
    @PutMapping("/{id}")
    public AccountResponse update(
            Authentication auth,
            @Parameter(description = "Account id") @PathVariable UUID id,
            @Valid @RequestBody AccountUpdateRequest req
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return accountService.update(userId, id, req);
    }

    @Operation(summary = "Delete account")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            Authentication auth,
            @Parameter(description = "Account id") @PathVariable UUID id
    ) {
        UUID userId = UUID.fromString(auth.getName());
        accountService.delete(userId, id);
    }
}