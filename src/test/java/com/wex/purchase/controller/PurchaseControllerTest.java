package com.wex.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.purchase.dto.CreatePurchaseRequest;
import com.wex.purchase.entity.PurchaseTransaction;
import com.wex.purchase.repository.PurchaseTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PurchaseTransactionRepository purchaseRepository;

    private PurchaseTransaction savedPurchase;

    @BeforeEach
    void setUp() {
        purchaseRepository.deleteAll();

        PurchaseTransaction purchase = new PurchaseTransaction("Integration Test Item", LocalDate.of(2024, 1, 15), 100.00);
        savedPurchase = purchaseRepository.save(purchase);
    }

    @Test
    void storePurchase_Success() throws Exception {
        CreatePurchaseRequest request = new CreatePurchaseRequest();
        request.setDescription("New Purchase");
        request.setTransactionDate(LocalDate.now());
        request.setAmountUSD(75.50);

        mockMvc.perform(post("/api/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("New Purchase"))
                .andExpect(jsonPath("$.amountUSD").value(75.50))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void storePurchase_InvalidDescriptionTooLong() throws Exception {
        CreatePurchaseRequest request = new CreatePurchaseRequest();
        request.setDescription("This description is way too long and exceeds the fifty character limit that is required");
        request.setTransactionDate(LocalDate.now());
        request.setAmountUSD(100.00);

        mockMvc.perform(post("/api/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void storePurchase_NegativeAmount() throws Exception {
        CreatePurchaseRequest request = new CreatePurchaseRequest();
        request.setDescription("Test");
        request.setTransactionDate(LocalDate.now());
        request.setAmountUSD(-10.00);

        mockMvc.perform(post("/api/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void storePurchase_FutureDate() throws Exception {
        CreatePurchaseRequest request = new CreatePurchaseRequest();
        request.setDescription("Test");
        request.setTransactionDate(LocalDate.now().plusDays(1));
        request.setAmountUSD(100.00);

        mockMvc.perform(post("/api/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPurchaseConverted_PurchaseNotFound() throws Exception {
        mockMvc.perform(get("/api/purchases/invalid-id/convert")
                        .param("currency", "EUR"))
                .andExpect(status().isNotFound());
    }
}