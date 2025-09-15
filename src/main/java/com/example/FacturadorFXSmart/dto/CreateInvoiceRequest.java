package com.example.FacturadorFXSmart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Peticion para crear una factura")
public record CreateInvoiceRequest(
        @NotNull LocalDate date,
        @NotBlank @Size(max = 120) String customer,
        @Pattern(regexp = "^[A-Z]{3}$") String baseCurrency,
        @DecimalMin("0.00")BigDecimal baseAmount,
        @DecimalMin("0.00000") @DecimalMax("1.0000") BigDecimal taxRate
        ) {
}
