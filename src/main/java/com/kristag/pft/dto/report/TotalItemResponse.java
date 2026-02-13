package com.kristag.pft.dto.report;

import java.math.BigDecimal;
import java.util.UUID;

public record TotalItemResponse(UUID id, String name, BigDecimal total) {}
