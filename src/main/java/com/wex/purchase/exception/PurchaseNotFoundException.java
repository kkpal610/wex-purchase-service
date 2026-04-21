package com.wex.purchase.exception;

public class PurchaseNotFoundException extends RuntimeException {
    public PurchaseNotFoundException(String id) {
        super("Purchase not found with id: " + id);
    }
}