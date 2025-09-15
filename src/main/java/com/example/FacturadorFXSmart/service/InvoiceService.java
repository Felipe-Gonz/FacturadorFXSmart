package com.example.FacturadorFXSmart.service;

import com.example.FacturadorFXSmart.dto.*;
import com.example.FacturadorFXSmart.entity.Invoice;
import com.example.FacturadorFXSmart.repository.InvoiceRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class InvoiceService {

    private final InvoiceRepository repo;
    private final FxService fx;

    public InvoiceService(InvoiceRepository repo, FxService fx){
        this.repo = repo;
        this.fx = fx;
    }

    public Invoice create(CreateInvoiceRequest r){
        Invoice i = new Invoice();
        i.setDate(r.date());
        i.setCustomer(r.customer());
        i.setBaseCurrency(r.baseCurrency());
        i.setBaseAmount(r.baseAmount());
        i.setTaxRate(r.taxRate());

        return repo.save(i);
    }

    public Page<Invoice> list(LocalDate from, LocalDate to, Pageable p){
        if(from == null || to == null){
            from = LocalDate.now().minusMonths(1);
            to = LocalDate.now();
        }
        return repo.findByDateBetween(from, to, p);
    }

    public ConversionResponse convert(UUID id, String target){
        Invoice i = repo.findById(id).orElseThrow();

        BigDecimal subtotal = i.getBaseAmount();
        BigDecimal tax = subtotal.multiply(i.getTaxRate());
        BigDecimal total = subtotal.add(tax);

        Map<String, BigDecimal> rates = fx.latest(i.getBaseCurrency(), List.of(target));
        BigDecimal rate = rates.getOrDefault(target, BigDecimal.ZERO);

        BigDecimal subtotalT = subtotal.multiply(rate).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal taxT = tax.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal totalT = total.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);

        return new ConversionResponse(
                i.getId(),
                target,
                rate,
                LocalDate.now(),
                subtotal.setScale(2, RoundingMode.HALF_EVEN),
                tax.setScale(2, RoundingMode.HALF_EVEN),
                total.setScale(2, RoundingMode.HALF_EVEN),
                subtotalT, taxT, totalT
        );
    }
}
