package com.kristag.pft.controller;

import com.kristag.pft.dto.report.MonthlyReportResponse;
import com.kristag.pft.service.ReportService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;

@Validated
@Tag(name = "Reports", description = "Reporting endpoints (monthly summaries, totals)")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Monthly report", description = "Returns a monthly summary for the authenticated user.")
    @GetMapping("/monthly")
    public MonthlyReportResponse monthly(
            Authentication auth,
            @Parameter(description = "Year", example = "2026") @RequestParam int year,
            @Parameter(description = "Month (1-12)", example = "3") @RequestParam int month
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        return reportService.getMonthlyReport(userId, year, month);
    }
}