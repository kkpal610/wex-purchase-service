package com.wex.purchase.service;

import com.wex.purchase.dto.CreatePurchaseResponse;
import com.wex.purchase.entity.PurchaseTransaction;
import com.wex.purchase.exception.PurchaseNotFoundException;
import com.wex.purchase.repository.PurchaseTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CurrencyConversionService {

    private final PurchaseTransactionRepository purchaseRepository;
    private final TreasuryRateService treasuryRateService;
    public CurrencyConversionService(PurchaseTransactionRepository purchaseRepository,
                                     TreasuryRateService treasuryRateService) {
        this.treasuryRateService = treasuryRateService;
        this.purchaseRepository = purchaseRepository;
    }
    public CreatePurchaseResponse getPurchaseInCurrency(String purchaseId, String country) {
        PurchaseTransaction purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new PurchaseNotFoundException(purchaseId));

        Map<String, Object> response = treasuryRateService.getExchangeRate(
                Character.toUpperCase(country.charAt(0)) + country.substring(1).toLowerCase(),
                purchase.getTransactionDate()
        );

        Double exchangeRate = (Double) response.get("exchangeRate");

        Double convertedAmount = Math.round(purchase.getAmountUSD() * exchangeRate * 100.0) / 100.0;

        return new CreatePurchaseResponse(
                purchase.getId(),
                purchase.getDescription(),
                purchase.getTransactionDate(),
                purchase.getAmountUSD(),
                exchangeRate,
                response.get("targetCurrency").toString(),
                country.toUpperCase(),
                convertedAmount
        );
    }
}