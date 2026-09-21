package com.invoiceq.backend.controller;

import com.invoiceq.backend.entity.Invoice;
import com.invoiceq.backend.security.AuthenticatedUser;
import com.invoiceq.backend.service.InvoiceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{invoiceId}")
    public Invoice getInvoice(
            @PathVariable String invoiceId,
            @AuthenticationPrincipal AuthenticatedUser user) {

        return invoiceService.getInvoiceByIdAndCompany(
                invoiceId,
                user.companyName()
        );
    }

    @GetMapping
    public List<Invoice> getInvoices(
            @AuthenticationPrincipal AuthenticatedUser user) {

        return invoiceService.getInvoicesByCompany(
                user.companyName()
        );
    }
}