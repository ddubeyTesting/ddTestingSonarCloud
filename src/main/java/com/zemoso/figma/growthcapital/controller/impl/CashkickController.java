package com.zemoso.figma.growthcapital.controller.impl;

import com.zemoso.figma.growthcapital.controller.CashkickOperations;
import com.zemoso.figma.growthcapital.dto.CashkickDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.response.ResponseHandler;
import com.zemoso.figma.growthcapital.service.CashkickService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CashkickController implements CashkickOperations {
    @Autowired
    private CashkickService cashkickService;

    @GetMapping("/cashkicks")
    public ResponseEntity<Object> getAllCashkicks() {
        return ResponseHandler.generateResponse("Successfull Operation", HttpStatus.OK, cashkickService.getAllCashkicks());
    }

    @PostMapping("/cashkicks")
    public ResponseEntity<Cashkick> createCashkick(@Valid @RequestBody CashkickDto cashkickDto) throws ResourceNotFoundException {
        return new ResponseEntity<>(cashkickService.createNewCashkick(cashkickDto), HttpStatus.CREATED);
    }

    @GetMapping("/cashkicks/{cashkickId}")
    public ResponseEntity<Cashkick> getCashkickById(@PathVariable Integer cashkickId) throws ResourceNotFoundException {
        return new ResponseEntity<>(cashkickService.getCashkickById(Long.valueOf(cashkickId)), HttpStatus.OK);
    }

    @PutMapping("/cashkicks/{cashkickId}")
    public ResponseEntity<Cashkick> updateCashkick(@RequestBody CashkickDto cashkickDto, @PathVariable Integer cashkickId) throws ResourceNotFoundException {
        return new ResponseEntity<>(cashkickService.updateCashkick(cashkickDto, Long.valueOf(cashkickId)), HttpStatus.OK);
    }

    @DeleteMapping("/cashkicks/{cashkickId}")
    public ResponseEntity<HttpStatus> deleteCashkickById(@PathVariable Integer cashkickId) throws ResourceNotFoundException {
        cashkickService.deleteCashkickById(Long.valueOf(cashkickId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
