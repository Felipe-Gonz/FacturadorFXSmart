package com.example.FacturadorFXSmart.controller;

import com.example.FacturadorFXSmart.dto.*;
import com.example.FacturadorFXSmart.entity.Invoice;
import com.example.FacturadorFXSmart.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/v1/invoices")
@Tag(name = "Invoices", description = "Gestion de facturas")
public class InvoiceController {
    private final InvoiceService svc;

    public InvoiceController(InvoiceService svc) {
        this.svc = svc;
    }

    @Operation(summary = "Crear factura")
    @ApiResponse(responseCode = "201", description = "Factura creada",
            content = @Content(schema = @Schema(implementation = InvoiceResponse.class)))
    @PostMapping
    public ResponseEntity<InvoiceResponse> create(@Valid @RequestBody CreateInvoiceRequest req){
        Invoice i = svc.create(req);
        var dto = new InvoiceResponse(i.getId(), i.getDate(), i.getCustomer(), i.getBaseCurrency(), i.getBaseAmount(), i.getTaxRate());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Listar facturas (paginado)")
    @ApiResponse(responseCode = "200", description = "Pagina de facturas")
    @GetMapping
    public Page<InvoiceResponse> list(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
        @PageableDefault(size = 10, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        return svc.list(from, to, pageable).map(i -> new InvoiceResponse(i.getId(), i.getDate(), i.getCustomer(), i.getBaseCurrency(), i.getBaseAmount(), i.getTaxRate()));
    }

    @Operation(summary = "Convertir total de una factura a otra moneda")
    @ApiResponse(responseCode = "200", description = "Conversion realizada",
            content = @Content(schema = @Schema(implementation = ConversionResponse.class)))
    @GetMapping("/{id}/convert")
    public ConversionResponse convert(@PathVariable UUID id, @RequestParam("target") String target){
        return svc.convert(id, target.toUpperCase());
    }
}
