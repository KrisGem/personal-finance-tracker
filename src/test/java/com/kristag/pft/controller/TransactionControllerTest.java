package com.kristag.pft.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kristag.pft.controller.error.NotFoundException;
import com.kristag.pft.domain.enums.CategoryType;
import com.kristag.pft.dto.TransactionCreateRequest;
import com.kristag.pft.dto.TransactionResponse;
import com.kristag.pft.security.JwtAuthFilter;
import com.kristag.pft.security.SecurityConfig;
import com.kristag.pft.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import com.kristag.pft.controller.error.ApiExceptionHandler;
import org.springframework.context.annotation.Import;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(
        controllers = TransactionController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = { SecurityConfig.class, JwtAuthFilter.class }
        )
)
@AutoConfigureMockMvc
@Import(ApiExceptionHandler.class)
class TransactionControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    TransactionService transactionService;

    @Test
    void create_returns201_whenValid() throws Exception {
        UUID accountId = UUID.randomUUID();

        TransactionCreateRequest req = new TransactionCreateRequest(
                accountId, null, new BigDecimal("10.00"), Instant.now(), "note"
        );

        TransactionResponse resp = new TransactionResponse(
                UUID.randomUUID(),
                accountId,
                null,
                new BigDecimal("10.00"),
                req.occurredAt(),
                "note",
                CategoryType.EXPENSE
        );

        when(transactionService.create(any(UUID.class), any(TransactionCreateRequest.class)))
                .thenReturn(resp);

        mockMvc.perform(post("/api/transactions")
                        .with(user("70c2281f-87cf-4c39-ba3a-707e36f4add0"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());

        verify(transactionService, times(1)).create(any(UUID.class), any(TransactionCreateRequest.class));
    }

    @Test
    void create_returns404_whenAccountNotFound() throws Exception {
        UUID accountId = UUID.randomUUID();

        TransactionCreateRequest req = new TransactionCreateRequest(
                accountId, null, new BigDecimal("10.00"), Instant.now(), "note"
        );

        when(transactionService.create(any(UUID.class), any(TransactionCreateRequest.class)))
                .thenThrow(new NotFoundException("Account not found"));

        mockMvc.perform(post("/api/transactions")
                        .with(user("663c2de6-f7d2-4f76-bb5b-72cac7687f13"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());

        verify(transactionService, times(1)).create(any(UUID.class), any(TransactionCreateRequest.class));
    }
}