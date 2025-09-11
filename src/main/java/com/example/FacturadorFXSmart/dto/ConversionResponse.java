package com.example.FacturadorFXSmart.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ConversionResponse(
        UUID invoiceId, String targetCurrency, BigDecimal rate, LocalDate rateDate,
        BigDecimal subtotalBase, BigDecimal taxBase, BigDecimal totalBase,
        BigDecimal subtotalTarget, BigDecimal taxTarget, BigDecimal totalTarget
) {
}
