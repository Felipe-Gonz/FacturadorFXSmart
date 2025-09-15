package com.example.FacturadorFXSmart.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 120)
    private String Customer;

    @Column(name = "base_currency", nullable = false, length = 3, columnDefinition = "char(3)")
    private String baseCurrency;

    @Column(name = "base_amount", nullable = false, precision = 19, scale = 6)
    private BigDecimal baseAmount;

    @Column(name = "tax_rate", nullable = false, precision = 5, scale = 4)
    private BigDecimal taxRate;

    @Column(name = "create_at", nullable = false)
    private OffsetDateTime createAt;

    @PrePersist
    void pre(){
        if (id == null) id = UUID.randomUUID();
        if (createAt == null) createAt = OffsetDateTime.now();
        if (baseCurrency != null) baseCurrency = baseCurrency.toLowerCase();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public OffsetDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(OffsetDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", date=" + date +
                ", Customer='" + Customer + '\'' +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", baseAmount=" + baseAmount +
                ", taxRate=" + taxRate +
                ", createAt=" + createAt +
                '}';
    }

    public Invoice() {
    }

    public Invoice(UUID id, LocalDate date, String customer, String baseCurrency, BigDecimal baseAmount, BigDecimal taxRate, OffsetDateTime createAt) {
        this.id = id;
        this.date = date;
        Customer = customer;
        this.baseCurrency = baseCurrency;
        this.baseAmount = baseAmount;
        this.taxRate = taxRate;
        this.createAt = createAt;
    }


}
