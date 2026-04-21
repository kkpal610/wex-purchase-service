package com.wex.purchase.service;

import com.wex.purchase.entity.PurchaseTransaction;
import com.wex.purchase.repository.PurchaseTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock
    private PurchaseTransactionRepository purchaseRepository;

    @InjectMocks
    private PurchaseService purchaseService;

    private PurchaseTransaction testPurchase;

    @BeforeEach
    void setUp() {
        testPurchase = new PurchaseTransaction("Test Purchase", LocalDate.now(), 99.99);
        testPurchase.setId("test-id");
    }

    @Test
    void storePurchase_Success() {
        when(purchaseRepository.save(any(PurchaseTransaction.class))).thenReturn(testPurchase);

        PurchaseTransaction saved = purchaseService.storePurchase(testPurchase);

        assertNotNull(saved);
        assertEquals("Test Purchase", saved.getDescription());
        assertEquals(99.99, saved.getAmountUSD());
    }

    @Test
    void getPurchaseById_Success() {
        when(purchaseRepository.findById("test-id")).thenReturn(java.util.Optional.of(testPurchase));

        PurchaseTransaction found = purchaseService.getPurchaseById("test-id");

        assertNotNull(found);
        assertEquals("test-id", found.getId());
    }
}