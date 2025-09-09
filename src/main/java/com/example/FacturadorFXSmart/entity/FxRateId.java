package com.example.FacturadorFXSmart.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class FxRateId implements Serializable {

    private LocalDate rateDate;
    private String base;
    private String target;

    public FxRateId() {}

    @Override public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof FxRateId that)) return false;
        return Objects.equals(rateDate, that.rateDate) &&
                Objects.equals(base, that.base) &&
                Objects.equals(target, that.target);
    }
    
    @Override public int hashCode() {
        return Objects.hash(rateDate, base, target);
    }
}
