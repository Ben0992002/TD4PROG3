package org.example.model;

import java.math.BigDecimal;

public record InvoiceTotal(
        int id,
        String customerName,
        String status,
        BigDecimal totalAmount
) {}
