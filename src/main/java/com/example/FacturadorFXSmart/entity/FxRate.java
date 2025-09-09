package com.example.FacturadorFXSmart.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "fx_rate")
@IdClass(FxRate.class)
public class FxRate {
    @Id
    @Column(name = "rate_date") private LocalDate rateDate;
    @Id @Column(length = 3) private String base;
    @Id @Column(length = 3) private String target;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal rate;

    @Column(nullable = false, length = 40)
    private String source;

    @Column(name = "retrieved_at", nullable = false)
    private OffsetDateTime retrieveAt;

    public LocalDate getRateDate() {
        return rateDate;
    }

    public void setRateDate(LocalDate rateDate) {
        this.rateDate = rateDate;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public OffsetDateTime getRetrieveAt() {
        return retrieveAt;
    }

    public void setRetrieveAt(OffsetDateTime retrieveAt) {
        this.retrieveAt = retrieveAt;
    }

    @Override
    public String toString() {
        return "FxRate{" +
                "rateDate=" + rateDate +
                ", base='" + base + '\'' +
                ", target='" + target + '\'' +
                ", rate=" + rate +
                ", source='" + source + '\'' +
                ", retrieveAt=" + retrieveAt +
                '}';
    }

    public FxRate() {
    }

    public FxRate(LocalDate rateDate, String base, String target, BigDecimal rate, String source, OffsetDateTime retrieveAt) {
        this.rateDate = rateDate;
        this.base = base;
        this.target = target;
        this.rate = rate;
        this.source = source;
        this.retrieveAt = retrieveAt;
    }
}
