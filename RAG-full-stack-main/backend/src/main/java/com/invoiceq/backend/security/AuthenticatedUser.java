package com.invoiceq.backend.security;

public record AuthenticatedUser (
        Long userId,
        String companyName,
        String email
){
}