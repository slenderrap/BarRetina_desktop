package com.project.Views.UiComponents;

import org.json.JSONObject;

import com.project.Utils.UtilsProduct;

public class CommandProduct {
    private Product product;
    private int quantity;
    private ProductStatus.Status status;
    private int quantityPaid;

    public CommandProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.status = ProductStatus.Status.PENDING;
        this.quantityPaid = 0;
    }

    public CommandProduct(JSONObject json) {
        this.product = UtilsProduct.getInstance().getProduct(json.getInt("id_producte"));
        this.quantity = json.getInt("quantitat");
        try {
            Object status = json.get("estat");
            if (status instanceof String) {
                this.status = ProductStatus.valueOf(status.toString());
            } else {
                this.status = ProductStatus.Status.PENDING;
            }
        } catch (IllegalArgumentException e) {
            this.status = ProductStatus.Status.PENDING;
        }
        this.quantityPaid = json.getInt("quantitat_pagada");
    }

    // Getters and setters
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public ProductStatus.Status getStatus() { return status; }
    public void setStatus(ProductStatus.Status status) { this.status = status; }
    
    public int getQuantityPaid() { return quantityPaid; }
    public void setQuantityPaid(int quantityPaid) { this.quantityPaid = quantityPaid; }
} 