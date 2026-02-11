package com.kristag.pft.dto;

import com.kristag.pft.domain.enums.CategoryType;

import java.time.Instant;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        CategoryType type,
        Instant createdAt
) {}
