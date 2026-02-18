// Pour Q5
package org.example.model;

import java.math.BigDecimal;

public record InvoiceTaxSummary(
        int id,
        BigDecimal totalHT,
        BigDecimal totalTVA,
        BigDecimal totalTTC
) {}
