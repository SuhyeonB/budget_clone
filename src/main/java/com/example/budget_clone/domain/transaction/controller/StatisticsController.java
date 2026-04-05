package com.example.budget_clone.domain.transaction.controller;

import com.example.budget_clone.domain.transaction.dto.response.MonthlyStatisticsResponse;
import com.example.budget_clone.domain.transaction.service.StatisticsService;
import com.example.budget_clone.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticsController {
    
    private final StatisticsService statisticsService;
    
    @GetMapping
    public ResponseEntity<MonthlyStatisticsResponse> monthlyStatistics (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) String accountNumber
    ) {
        return ResponseEntity.ok(statisticsService.monthlyStatistics(userDetails.getUserId(), year, month, accountNumber));
    }
}
