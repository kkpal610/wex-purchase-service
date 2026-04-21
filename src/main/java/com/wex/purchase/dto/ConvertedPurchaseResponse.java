package com.wex.purchase.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ConvertedPurchaseResponse(
        UUID id,
        String description,
        LocalDate transactionDate,
        BigDecimal originalUsdAmount,
        String country,
        String currency,
        BigDecimal exchangeRateUsed,
        LocalDate exchangeRateDate,
        BigDecimal convertedAmount
) {
}
