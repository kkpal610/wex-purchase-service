package com.wex.purchase.service;

import com.wex.purchase.dto.CreatePurchaseResponse;
import com.wex.purchase.entity.PurchaseTransaction;
import com.wex.purchase.exception.CurrencyConversionException;
import com.wex.purchase.exception.PurchaseNotFoundException;
import com.wex.purchase.repository.PurchaseTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyConversionServiceTest {

    @Mock
    private PurchaseTransactionRepository purchaseRepository;

    @Mock
    private TreasuryRateService treasuryRateService;

    @InjectMocks
    private CurrencyConversionService currencyConversionService;

    private PurchaseTransaction testPurchase;
    private final String purchaseId = "test-id";

    @BeforeEach
    void setUp() {
        testPurchase = new PurchaseTransaction("Test Item", LocalDate.of(2024, 1, 15), 100.50);
        testPurchase.setId(purchaseId);
    }

    @Test
    void getPurchaseInCurrency_Success() {
        Map<String, Object> exchangeRateData = new HashMap<>();
        exchangeRateData.put("targetCurrency", "Canadian Dollar");
        exchangeRateData.put("exchangeRate", 0.85);

        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(testPurchase));
        when(treasuryRateService.getExchangeRate("Canada", testPurchase.getTransactionDate()))
                .thenReturn(exchangeRateData);

        CreatePurchaseResponse response = currencyConversionService.getPurchaseInCurrency(purchaseId, "Canada");

        assertNotNull(response);
        assertEquals(purchaseId, response.getId());
        assertEquals("Test Item", response.getDescription());
        assertEquals(100.50, response.getOriginalAmountUSD());
        assertEquals(0.85, response.getExchangeRate());
        assertEquals("CANADA", response.getTargetCountry());  // country parameter
        assertEquals("Canadian Dollar", response.getTargetCurrency());  // from response map
        assertEquals(85.43, response.getConvertedAmount());
    }
    @Test
    void getPurchaseInCurrency_PurchaseNotFound() {
        when(purchaseRepository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(PurchaseNotFoundException.class,
                () -> currencyConversionService.getPurchaseInCurrency("invalid-id", "Canada"));
    }

    @Test
    void getPurchaseInCurrency_NoExchangeRate() {
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(testPurchase));
        when(treasuryRateService.getExchangeRate("XYZ", testPurchase.getTransactionDate()))
                .thenThrow(new CurrencyConversionException("No exchange rate available"));

        assertThrows(CurrencyConversionException.class,
                () -> currencyConversionService.getPurchaseInCurrency(purchaseId, "XYZ"));
    }
}
