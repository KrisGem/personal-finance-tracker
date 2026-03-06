package com.kristag.pft.controller;

import com.kristag.pft.domain.enums.CategoryType;
import com.kristag.pft.dto.TransactionCreateRequest;
import com.kristag.pft.dto.TransactionResponse;
import com.kristag.pft.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.Instant;
import java.util.UUID;

@Validated
@Tag(name = "Transactions", description = "Create, view, update, delete and search transactions")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Create transaction")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(Authentication auth, @Valid @RequestBody TransactionCreateRequest req) {
        UUID userId = UUID.fromString(auth.getName());
        return transactionService.create(userId, req);
    }

    @Operation(summary = "Get transaction by id")
    @GetMapping("/{id}")
    public TransactionResponse get(
            Authentication auth,
            @Parameter(description = "Transaction id") @PathVariable @NotNull UUID id
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return transactionService.get(userId, id);
    }

    @Operation(summary = "Update transaction")
    @PutMapping("/{id}")
    public TransactionResponse update(
            Authentication auth,
            @Parameter(description = "Transaction id") @PathVariable UUID id,
            @Valid @RequestBody TransactionCreateRequest req
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return transactionService.update(userId, id, req);
    }

    @Operation(summary = "Delete transaction")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            Authentication auth,
            @Parameter(description = "Transaction id") @PathVariable UUID id
    ) {
        UUID userId = UUID.fromString(auth.getName());
        transactionService.delete(userId, id);
    }

    @Operation(summary = "List/search transactions")
    @GetMapping
    public Page<TransactionResponse> list(
            Authentication auth,
            @Parameter(description = "Filter: from (inclusive), ISO-8601 instant", example = "2026-03-01T00:00:00Z")
            @RequestParam(required = false) Instant from,

            @Parameter(description = "Filter: to (inclusive), ISO-8601 instant", example = "2026-03-31T23:59:59Z")
            @RequestParam(required = false) Instant to,

            @Parameter(description = "Filter by account id")
            @RequestParam(required = false) UUID accountId,

            @Parameter(description = "Filter by category id")
            @RequestParam(required = false) UUID categoryId,

            @Parameter(description = "Filter by category type (e.g. INCOME/EXPENSE)")
            @RequestParam(required = false) CategoryType type,

            @Parameter(hidden = true) Pageable pageable
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return transactionService.list(userId, from, to, accountId, categoryId, type, pageable);
    }
}