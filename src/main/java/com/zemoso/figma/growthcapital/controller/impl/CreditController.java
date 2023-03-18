package com.zemoso.figma.growthcapital.controller.impl;

import com.zemoso.figma.growthcapital.controller.CreditOperations;
import com.zemoso.figma.growthcapital.entity.Payment;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CreditController implements CreditOperations {

    @Autowired
    private CreditService creditService;

    @GetMapping("/creditDetails")
    public ResponseEntity<UserCreditDetails> getUserCreditDetails() throws ResourceNotFoundException {
        return new ResponseEntity<>(creditService.getUserCreditDetails(), HttpStatus.OK);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getUserPayments() throws ResourceNotFoundException {
        return new ResponseEntity<>(creditService.getUserPayments(), HttpStatus.OK);
    }
}
