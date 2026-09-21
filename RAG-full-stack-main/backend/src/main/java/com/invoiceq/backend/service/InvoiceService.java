package com.invoiceq.backend.service;

import com.invoiceq.backend.entity.Invoice;
import com.invoiceq.backend.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice getInvoiceByIdAndCompany(String invoiceId, String companyName) {
        return invoiceRepository.findByInvoiceIdAndCompanyName(invoiceId, companyName)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    public List<Invoice> getInvoicesByCompany(String companyName) {
        return invoiceRepository.findByCompanyName(companyName);
    }
}