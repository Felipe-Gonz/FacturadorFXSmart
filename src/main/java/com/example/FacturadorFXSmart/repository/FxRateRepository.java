package com.example.FacturadorFXSmart.repository;

import com.example.FacturadorFXSmart.entity.FxRate;
import com.example.FacturadorFXSmart.entity.FxRateId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface FxRateRepository extends JpaRepository<FxRate, FxRateId> {
    Optional<FxRate> findFirstByBaseAndTargetAndRateDate(String base, String target, LocalDate date);
}
