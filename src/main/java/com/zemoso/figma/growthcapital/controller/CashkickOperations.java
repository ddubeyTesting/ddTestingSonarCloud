package com.zemoso.figma.growthcapital.controller;

import com.zemoso.figma.growthcapital.dto.CashkickDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface CashkickOperations {
    @GetMapping("/cashkicks")
    public ResponseEntity<Object> getAllCashkicks();

    @PostMapping("/cashkicks")
    public ResponseEntity<Cashkick> createCashkick(@Valid @RequestBody CashkickDto cashkickDto) throws ResourceNotFoundException;

    @GetMapping("/cashkicks/{cashkickId}")
    public ResponseEntity<Cashkick> getCashkickById(@PathVariable Integer cashkickId) throws ResourceNotFoundException;

    @PutMapping("/cashkicks/{cashkickId}")
    public ResponseEntity<Cashkick> updateCashkick(@RequestBody CashkickDto cashkickDto, @PathVariable Integer cashkickId) throws ResourceNotFoundException;

    @DeleteMapping("/cashkicks/{cashkickId}")
    public ResponseEntity<HttpStatus> deleteCashkickById(@PathVariable Integer cashkickId) throws ResourceNotFoundException;
}
