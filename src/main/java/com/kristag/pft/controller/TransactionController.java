package com.kristag.pft.controller;

import com.kristag.pft.domain.enums.CategoryType;
import com.kristag.pft.dto.TransactionCreateRequest;
import com.kristag.pft.dto.TransactionResponse;
import com.kristag.pft.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(Authentication auth, @Valid @RequestBody TransactionCreateRequest req) {
        UUID userId = (UUID) auth.getPrincipal();
        return transactionService.create(userId, req);
    }

    @GetMapping("/{id}")
    public TransactionResponse get(Authentication auth, @PathVariable UUID id) {
        UUID userId = (UUID) auth.getPrincipal();
        return transactionService.get(userId, id);
    }

    @PutMapping("/{id}")
    public TransactionResponse update(Authentication auth, @PathVariable UUID id, @Valid @RequestBody TransactionCreateRequest req) {
        UUID userId = (UUID) auth.getPrincipal();
        return transactionService.update(userId, id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication auth, @PathVariable UUID id) {
        UUID userId = (UUID) auth.getPrincipal();
        transactionService.delete(userId, id);
    }

    @GetMapping
    public Page<TransactionResponse> list(
            Authentication auth,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to,
            @RequestParam(required = false) UUID accountId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) CategoryType type,
            Pageable pageable
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        return transactionService.list(userId, from, to, accountId, categoryId, type, pageable);
    }
}
