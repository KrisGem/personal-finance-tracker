package com.kristag.pft.dto.report;


import java.math.BigDecimal;
import java.util.List;

public record MonthlyReportResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal net,
        List<TotalItemResponse> totalsByCategory,
        List<TotalItemResponse> totalsByAccount
) {}