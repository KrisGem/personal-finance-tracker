package com.kristag.pft.service;

import com.kristag.pft.domain.entity.Account;
import com.kristag.pft.domain.entity.Category;
import com.kristag.pft.domain.entity.Transaction;
import com.kristag.pft.domain.repository.AccountRepository;
import com.kristag.pft.domain.repository.CategoryRepository;
import com.kristag.pft.domain.repository.TransactionRepository;
import com.kristag.pft.dto.TransactionCreateRequest;
import com.kristag.pft.controller.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock AccountRepository accountRepository;
    @Mock CategoryRepository categoryRepository;
    @Mock TransactionRepository transactionRepository;

    @InjectMocks TransactionService transactionService;
    @Test
    void create_throwsNotFound_whenAccountMissing() {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        when(accountRepository.findByIdAndUser_Id(accountId, userId))
                .thenReturn(Optional.empty());

        TransactionCreateRequest req = new TransactionCreateRequest(
                accountId,
                null,
                new BigDecimal("10.00"),
                Instant.now(),
                "note"
        );

        assertThrows(NotFoundException.class, () -> transactionService.create(userId, req));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void create_success_withoutCategory_savesTransaction() {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        Account account = mock(Account.class);
        when(accountRepository.findByIdAndUser_Id(accountId, userId))
                .thenReturn(Optional.of(account));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        TransactionCreateRequest req = new TransactionCreateRequest(
                accountId,
                null,
                new BigDecimal("12.34"),
                Instant.now(),
                "groceries"
        );

        transactionService.create(userId, req);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction saved = captor.getValue();
        assertSame(account, saved.getAccount());
        assertNull(saved.getCategory());
        assertEquals(new BigDecimal("12.34"), saved.getAmount());
        assertEquals("groceries", saved.getNote());
    }

    @Test
    void create_throwsNotFound_whenCategoryMissing() {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Account account = mock(Account.class);

        when(accountRepository.findByIdAndUser_Id(accountId, userId))
                .thenReturn(Optional.of(account));

        when(categoryRepository.findByIdAndUser_Id(categoryId, userId))
                .thenReturn(Optional.empty());

        TransactionCreateRequest req = new TransactionCreateRequest(
                accountId,
                categoryId,
                new BigDecimal("5.00"),
                Instant.now(),
                "note"
        );

        assertThrows(NotFoundException.class, () -> transactionService.create(userId, req));
        verify(transactionRepository, never()).save(any());
    }
@Test
void create_success_withCategory_savesTransaction() {
    UUID userId = UUID.randomUUID();
    UUID accountId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();

    Account account = mock(Account.class);
    Category category = mock(Category.class);

    when(accountRepository.findByIdAndUser_Id(accountId, userId))
            .thenReturn(Optional.of(account));

    when(categoryRepository.findByIdAndUser_Id(categoryId, userId))
            .thenReturn(Optional.of(category));

    when(transactionRepository.save(any(Transaction.class)))
            .thenAnswer(inv -> inv.getArgument(0));

    TransactionCreateRequest req = new TransactionCreateRequest(
            accountId,
            categoryId,
            new BigDecimal("20.00"),
            Instant.now(),
            "rent"
    );

    transactionService.create(userId, req);


    ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
    verify(transactionRepository).save(captor.capture());

    Transaction saved = captor.getValue();
    assertNotNull(saved);

}
}
