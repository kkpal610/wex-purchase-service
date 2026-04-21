package com.wex.purchase.service;

import com.wex.purchase.dto.CreatePurchaseRequest;
import com.wex.purchase.dto.CreatePurchaseResponse;
import com.wex.purchase.entity.PurchaseTransaction;
import com.wex.purchase.repository.PurchaseTransactionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class PurchaseService {

    private final PurchaseTransactionRepository purchaseRepository;
    public PurchaseService(PurchaseTransactionRepository purchaseRepository){
        this.purchaseRepository = purchaseRepository;
    }

    public PurchaseTransaction storePurchase(@Valid PurchaseTransaction purchase) {
        return purchaseRepository.save(purchase);
    }

    public PurchaseTransaction getPurchaseById(String id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found with id: " + id));
    }
}
