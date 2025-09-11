package com.example.FacturadorFXSmart.repository;

import com.example.FacturadorFXSmart.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Page<Invoice> findByDateBetween(LocalDate from, LocalDate to, Pageable pageable);
}
