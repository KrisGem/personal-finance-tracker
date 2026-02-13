package com.kristag.pft.controller;

import com.kristag.pft.dto.report.MonthlyReportResponse;
import com.kristag.pft.service.ReportService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/monthly")
    public MonthlyReportResponse monthly(Authentication auth,
                                         @RequestParam int year,
                                         @RequestParam int month) {
        UUID userId = (UUID) auth.getPrincipal();
        return reportService.getMonthlyReport(userId, year, month);
    }
}
