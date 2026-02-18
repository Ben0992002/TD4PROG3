// Pour Q3
package org.example.model;

import java.math.BigDecimal;

public record InvoiceStatusTotals(
        BigDecimal totalPaid,
        BigDecimal totalConfirmed,
        BigDecimal totalDraft
) {}
