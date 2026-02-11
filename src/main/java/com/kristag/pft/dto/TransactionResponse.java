package com.kristag.pft.dto;


import com.kristag.pft.domain.enums.CategoryType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID accountId,
        UUID categoryId,
        BigDecimal amount,
        Instant occurredAt,
        String note,
        CategoryType type
) {}
