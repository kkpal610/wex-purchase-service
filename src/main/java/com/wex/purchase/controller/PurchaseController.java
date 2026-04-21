package com.wex.purchase.controller;

import com.wex.purchase.dto.ConvertedPurchaseResponse;
import com.wex.purchase.dto.CreatePurchaseRequest;
import com.wex.purchase.dto.CreatePurchaseResponse;
import com.wex.purchase.entity.PurchaseTransaction;
import com.wex.purchase.service.CurrencyConversionService;
import com.wex.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final CurrencyConversionService currencyConversionService;

    public PurchaseController(PurchaseService purchaseService,
                              CurrencyConversionService currencyConversionService){
        this.currencyConversionService = currencyConversionService;
        this.purchaseService = purchaseService;
    }
    @PostMapping
    public ResponseEntity<PurchaseTransaction> storePurchase(@Valid @RequestBody CreatePurchaseRequest request) {
        PurchaseTransaction purchase = new PurchaseTransaction(
                request.getDescription(),
                request.getTransactionDate(),
                request.getAmountUSD()
        );
        PurchaseTransaction savedPurchase = purchaseService.storePurchase(purchase);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPurchase);
    }

    @GetMapping("/{id}/convert")
    public ResponseEntity<CreatePurchaseResponse> getPurchaseConverted(
            @PathVariable String id,
            @RequestParam String country) {
        CreatePurchaseResponse response = currencyConversionService.getPurchaseInCurrency(id, country);
        return ResponseEntity.ok(response);
    }
}