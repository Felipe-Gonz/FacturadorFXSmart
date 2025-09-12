package com.example.FacturadorFXSmart.service;

import com.example.FacturadorFXSmart.entity.FxRate;
import com.example.FacturadorFXSmart.repository.FxRateRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FxService {
    private final FxRateRepository repo;

    public  FxService(FxRateRepository repo) {
        this.repo = repo;
    }

    public Map<String, BigDecimal> latest(String base, List<String> targets){
        LocalDate today = LocalDate.now();
        Map<String, BigDecimal> out = new HashMap<>();

        for (String t : targets){
            var opt = repo.findFirstByBaseAndTargetAndRateDate(base, t, today);
            if (opt.isPresent()){
                out.put(t, opt.get().getRate());
            } else {
                BigDecimal rate = BigDecimal.ONE;
                FxRate r = new FxRate();

                r.setRateDate(today);
                r.setBase(base);
                r.setTarget(t);
                r.setRate(rate);
                r.setSource("demo");
                r.setRetrieveAt(OffsetDateTime.now());
                repo.save(r);
                out.put(t, rate);
            }
        }
        return out;
    }
}
