package com.zemoso.figma.growthcapital.controller.impl;

import com.zemoso.figma.growthcapital.controller.ContractOperations;
import com.zemoso.figma.growthcapital.dto.ContractDto;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.response.ResponseHandler;
import com.zemoso.figma.growthcapital.service.ContractService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContractController implements ContractOperations {
    @Autowired
    private ContractService contractService;

    @GetMapping("/contracts")
    public ResponseEntity<Object> getAllContracts() {
        return ResponseHandler.generateResponse("Successfull Operation", HttpStatus.OK, contractService.getAllContracts());
    }

    @PostMapping("/contracts")
    public ResponseEntity<Contract> createContract(@Valid @RequestBody ContractDto contractDto) {
        return new ResponseEntity<>(contractService.createNewContract(contractDto), HttpStatus.CREATED);
    }

    @GetMapping("/contracts/{contractId}")
    public ResponseEntity<Contract> getContractById(@PathVariable Integer contractId) throws ResourceNotFoundException {
        return new ResponseEntity<>(contractService.getContractById(Long.valueOf(contractId)), HttpStatus.OK);
    }

    @PutMapping("/contracts/{contractId}")
    public ResponseEntity<Contract> updateContract(@RequestBody ContractDto contractDto, @PathVariable Integer contractId) throws ResourceNotFoundException {
        return new ResponseEntity<>(contractService.updateContract(contractDto, Long.valueOf(contractId)), HttpStatus.OK);
    }

    @DeleteMapping("/contracts/{contractId}")
    public ResponseEntity<HttpStatus> deleteContractById(@PathVariable Integer contractId) throws ResourceNotFoundException {
        contractService.deleteContractById(Long.valueOf(contractId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
