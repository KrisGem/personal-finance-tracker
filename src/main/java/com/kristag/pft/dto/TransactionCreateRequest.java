package com.kristag.pft.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionCreateRequest(
        @NotNull UUID accountId,
        UUID categoryId,
        @NotNull @Positive BigDecimal amount,
        @NotNull Instant occurredAt,
        @Size(max = 255) String note
) {}