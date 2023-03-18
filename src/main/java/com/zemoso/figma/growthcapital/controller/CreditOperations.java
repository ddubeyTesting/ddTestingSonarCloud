package com.zemoso.figma.growthcapital.controller;

import com.zemoso.figma.growthcapital.entity.Payment;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface CreditOperations {
    @GetMapping("/creditDetails")
    public ResponseEntity<UserCreditDetails> getUserCreditDetails() throws ResourceNotFoundException;

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getUserPayments() throws ResourceNotFoundException;
}
