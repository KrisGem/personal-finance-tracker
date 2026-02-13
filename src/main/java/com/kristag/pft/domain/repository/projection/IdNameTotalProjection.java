package com.kristag.pft.domain.repository.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface IdNameTotalProjection {
    UUID getId();
    String getName();
    BigDecimal getTotal();
}