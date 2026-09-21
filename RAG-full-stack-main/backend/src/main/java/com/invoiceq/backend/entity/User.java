package com.invoiceq.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String name;

    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name="company_id", nullable = false)
    private Company company;
}
