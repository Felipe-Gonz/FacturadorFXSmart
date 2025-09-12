package com.example.FacturadorFXSmart.controller;

import com.example.FacturadorFXSmart.dto.*;
import com.example.FacturadorFXSmart.entity.Invoice;
import com.example.FacturadorFXSmart.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

public class InvoiceController {
    private final InvoiceService svc;

    public InvoiceController(InvoiceService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> create(@Valid @RequestBody CreateInvoiceRequest req){
        Invoice i = svc.create(req);
        var dto = new InvoiceResponse(i.getId(), i.getDate(), i.getCustomer(), i.getBaseCurrency(), i.getBaseAmount(), i.getTaxRate());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public Page<InvoiceResponse> list(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
        @PageableDefault(size = 10, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        return svc.list(from, to, pageable).map(i -> new InvoiceResponse(i.getId(), i.getDate(), i.getCustomer(), i.getBaseCurrency(), i.getBaseAmount(), i.getTaxRate()));
    }

    @GetMapping("/{id}/convert")
    public ConversionResponse convert(@PathVariable UUID id, @RequestParam("target") String target){
        return svc.convert(id, target.toUpperCase());
    }
}
