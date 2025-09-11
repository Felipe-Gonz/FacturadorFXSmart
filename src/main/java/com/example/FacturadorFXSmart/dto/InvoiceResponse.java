package com.example.FacturadorFXSmart.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InvoiceResponse(
        UUID id, LocalDate date, String customer,
        String baseCurrency, BigDecimal baseAmount, BigDecimal taxRate
) {
}
