package com.invoiceq.backend.repository;

import com.invoiceq.backend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    Optional<Invoice> findByInvoiceIdAndCompanyName(String invoiceId, String companyName);

    List<Invoice> findByCompanyName(String companyName);

}