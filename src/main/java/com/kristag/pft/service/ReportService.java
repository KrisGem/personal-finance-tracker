package com.kristag.pft.service;

import com.kristag.pft.dto.report.MonthlyReportResponse;
import com.kristag.pft.dto.report.TotalItemResponse;
import com.kristag.pft.domain.repository.TransactionRepository;
import com.kristag.pft.domain.repository.projection.IdNameTotalProjection;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    private final TransactionRepository txRepo;

    public ReportService(TransactionRepository txRepo) {
        this.txRepo = txRepo;
    }

    public MonthlyReportResponse getMonthlyReport(UUID userId, int year, int month) {
        if (month < 1 || month > 12) throw new IllegalArgumentException("month must be 1-12");

        LocalDate startDate = LocalDate.of(year, month, 1);
        Instant start = startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = startDate.plusMonths(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        BigDecimal income = txRepo.sumIncomeForPeriod(userId, start, end);
        BigDecimal expense = txRepo.sumExpenseForPeriod(userId, start, end);
        BigDecimal net = income.subtract(expense);

        List<TotalItemResponse> byAccount = map(txRepo.expenseTotalsByAccount(userId, start, end));

        List<TotalItemResponse> byCategory = new ArrayList<>(map(txRepo.expenseTotalsByCategory(userId, start, end)));
        BigDecimal uncategorized = txRepo.expenseTotalUncategorized(userId, start, end);
        if (uncategorized.compareTo(BigDecimal.ZERO) > 0) {
            byCategory.add(new TotalItemResponse(null, "Uncategorized", uncategorized));
        }

        return new MonthlyReportResponse(income, expense, net, byCategory, byAccount);
    }

    private List<TotalItemResponse> map(List<IdNameTotalProjection> rows) {
        return rows.stream()
                .map(r -> new TotalItemResponse(r.getId(), r.getName(), r.getTotal()))
                .toList();
    }
}