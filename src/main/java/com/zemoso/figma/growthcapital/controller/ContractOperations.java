package com.zemoso.figma.growthcapital.controller;

import com.zemoso.figma.growthcapital.dto.ContractDto;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface ContractOperations {
    @GetMapping("/contracts")
    public ResponseEntity<Object> getAllContracts();

    @PostMapping("/contracts")
    public ResponseEntity<Contract> createContract(@Valid @RequestBody ContractDto contractDto);

    @GetMapping("/contracts/{contractId}")
    public ResponseEntity<Contract> getContractById(@PathVariable Integer contractId) throws ResourceNotFoundException;

    @PutMapping("/contracts/{contractId}")
    public ResponseEntity<Contract> updateContract(@RequestBody ContractDto contractDto, @PathVariable Integer contractId) throws ResourceNotFoundException;

    @DeleteMapping("/contracts/{contractId}")
    public ResponseEntity<HttpStatus> deleteContractById(@PathVariable Integer contractId) throws ResourceNotFoundException;
}
