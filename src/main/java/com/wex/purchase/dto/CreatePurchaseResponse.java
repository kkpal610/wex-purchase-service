package com.wex.purchase.dto;

import java.time.LocalDate;

public class CreatePurchaseResponse {
    private String id;
    private String description;
    private LocalDate transactionDate;
    private Double originalAmountUSD;
    private Double exchangeRate;
    private String targetCountry;
    private String targetCurrency;
    private Double convertedAmount;

    public CreatePurchaseResponse(String id, String description, LocalDate transactionDate,
                            Double originalAmountUSD, Double exchangeRate,
                            String targetCurrency, String targetCountry, Double convertedAmount) {
        this.id = id;
        this.description = description;
        this.transactionDate = transactionDate;
        this.originalAmountUSD = originalAmountUSD;
        this.exchangeRate = exchangeRate;
        this.targetCurrency = targetCurrency;
        this.targetCountry = targetCountry;
        this.convertedAmount = convertedAmount;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
    public Double getOriginalAmountUSD() { return originalAmountUSD; }
    public void setOriginalAmountUSD(Double originalAmountUSD) { this.originalAmountUSD = originalAmountUSD; }
    public Double getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(Double exchangeRate) { this.exchangeRate = exchangeRate; }
    public String getTargetCurrency() { return targetCurrency; }
    public void setTargetCurrency(String targetCurrency) { this.targetCurrency = targetCurrency; }
    public String getTargetCountry() { return targetCountry; }
    public void setTargetCountry(String targetCountry) { this.targetCountry = targetCountry; }
    public Double getConvertedAmount() { return convertedAmount; }
    public void setConvertedAmount(Double convertedAmount) { this.convertedAmount = convertedAmount; }
}