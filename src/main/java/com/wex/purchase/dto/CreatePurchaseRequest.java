package com.wex.purchase.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CreatePurchaseRequest {

    @NotBlank(message = "Description is required")
    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;

    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDate transactionDate;

    @NotNull(message = "Purchase amount is required")
    @Positive(message = "Purchase amount must be positive")
    private Double amountUSD;

    // Getters and Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
    public Double getAmountUSD() { return amountUSD; }
    public void setAmountUSD(Double amountUSD) { this.amountUSD = amountUSD; }
}