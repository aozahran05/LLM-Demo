package com.invoiceq.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name="invoices")
@Getter
@Setter
public class Invoice {

    @Id
    @Column(name="invoice_id")
    private String invoiceId;

    @Column(name="company_name")
    private String companyName;

    private BigDecimal amount;

}