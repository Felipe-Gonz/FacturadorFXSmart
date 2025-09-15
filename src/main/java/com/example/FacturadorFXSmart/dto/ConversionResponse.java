package com.example.FacturadorFXSmart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Peticion para crear una Conversion")
public record ConversionResponse(
        UUID invoiceId, String targetCurrency, BigDecimal rate, LocalDate rateDate,
        BigDecimal subtotalBase, BigDecimal taxBase, BigDecimal totalBase,
        BigDecimal subtotalTarget, BigDecimal taxTarget, BigDecimal totalTarget
) {
}
