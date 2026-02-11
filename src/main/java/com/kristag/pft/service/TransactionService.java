package com.kristag.pft.service;


import com.kristag.pft.domain.entity.Account;
import com.kristag.pft.domain.entity.Category;
import com.kristag.pft.domain.entity.Transaction;
import com.kristag.pft.domain.enums.CategoryType;
import com.kristag.pft.domain.repository.AccountRepository;
import com.kristag.pft.domain.repository.CategoryRepository;
import com.kristag.pft.domain.repository.TransactionRepository;
import com.kristag.pft.dto.TransactionCreateRequest;
import com.kristag.pft.dto.TransactionResponse;
import com.kristag.pft.transaction.TransactionSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    public TransactionResponse create(UUID userId, TransactionCreateRequest req) {
        Account account = accountRepository.findByIdAndUser_Id(req.accountId(), userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        Category category = null;
        if (req.categoryId() != null) {
            category = categoryRepository.findByIdAndUser_Id(req.categoryId(), userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        }

        Transaction tx = new Transaction(account, category, req.amount(), req.occurredAt(), req.note());
        return toResponse(transactionRepository.save(tx));
    }

    public TransactionResponse get(UUID userId, UUID txId) {
        Transaction tx = transactionRepository.findByIdAndAccount_User_Id(txId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
        return toResponse(tx);
    }

    public TransactionResponse update(UUID userId, UUID txId, TransactionCreateRequest req) {
        Transaction tx = transactionRepository.findByIdAndAccount_User_Id(txId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        Account account = accountRepository.findByIdAndUser_Id(req.accountId(), userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        Category category = null;
        if (req.categoryId() != null) {
            category = categoryRepository.findByIdAndUser_Id(req.categoryId(), userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        }

        tx.update(account, category, req.amount(), req.occurredAt(), req.note());
        return toResponse(transactionRepository.save(tx));
    }

    public void delete(UUID userId, UUID txId) {
        Transaction tx = transactionRepository.findByIdAndAccount_User_Id(txId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
        transactionRepository.delete(tx);
    }


    public Page<TransactionResponse> list(
            UUID userId,
            java.time.Instant from,
            java.time.Instant to,
            UUID accountId,
            UUID categoryId,
            CategoryType type,
            Pageable pageable
    ) {
        Specification<Transaction> spec = TransactionSpecs.ownedBy(userId);

        if (from != null) spec = spec.and(TransactionSpecs.occurredFrom(from));
        if (to != null) spec = spec.and(TransactionSpecs.occurredTo(to));
        if (accountId != null) spec = spec.and(TransactionSpecs.hasAccount(accountId));
        if (categoryId != null) spec = spec.and(TransactionSpecs.hasCategory(categoryId));
        if (type != null) spec = spec.and(TransactionSpecs.hasType(type));

        return transactionRepository.findAll(spec, pageable).map(this::toResponse);
    }

    private TransactionResponse toResponse(Transaction tx) {
        UUID categoryId = tx.getCategory() != null ? tx.getCategory().getId() : null;
        CategoryType type = tx.getCategory() != null ? tx.getCategory().getType() : null;

        return new TransactionResponse(
                tx.getId(),
                tx.getAccount().getId(),
                categoryId,
                tx.getAmount(),
                tx.getOccurredAt(),
                tx.getNote(),
                type
        );
    }
}
