package com.kristag.pft.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String name,
        String currency,
        BigDecimal openingBalance,
        Instant createdAt
) {}