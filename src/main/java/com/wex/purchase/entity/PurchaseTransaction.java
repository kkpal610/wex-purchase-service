package com.wex.purchase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "purchase_transactions")
public class PurchaseTransaction {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String id;

    @NotBlank(message = "Description is required")
    @Size(max = 50, message = "Description must not exceed 50 characters")
    @Column(length = 50, nullable = false)
    private String description;

    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    @Column(nullable = false)
    private LocalDate transactionDate;

    @NotNull(message = "Purchase amount is required")
    @Positive(message = "Purchase amount must be positive")
    @Column(nullable = false)
    private Double amountUSD;

    public PurchaseTransaction() {
        this.id = UUID.randomUUID().toString();
    }

    public PurchaseTransaction(String description, LocalDate transactionDate, Double amountUSD) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.transactionDate = transactionDate;
        this.amountUSD = Math.round(amountUSD * 100.0) / 100.0;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
    public Double getAmountUSD() { return amountUSD; }
    public void setAmountUSD(Double amountUSD) {
        this.amountUSD = Math.round(amountUSD * 100.0) / 100.0;
    }
}
