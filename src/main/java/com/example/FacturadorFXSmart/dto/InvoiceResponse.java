package com.example.FacturadorFXSmart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Respuesta de factura creada")
public record InvoiceResponse(
        UUID id, LocalDate date, String customer,
        String baseCurrency, BigDecimal baseAmount, BigDecimal taxRate
) {
}
