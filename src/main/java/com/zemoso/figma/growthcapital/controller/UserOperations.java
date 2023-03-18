package com.zemoso.figma.growthcapital.controller;

import com.zemoso.figma.growthcapital.dto.UserCreditDetailsDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.entity.User;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserOperations {
    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers();

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) throws ResourceNotFoundException;

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody UserDto userDto, @PathVariable Integer userId) throws ResourceNotFoundException;

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Integer userId) throws ResourceNotFoundException;

    @GetMapping("/users/{userId}/contracts")
    public ResponseEntity<List<Contract>> getUserContracts(@PathVariable Integer userId) throws ResourceNotFoundException;

    @GetMapping("/users/{userId}/cashkicks")
    public ResponseEntity<List<Cashkick>> getUserCashkicks(@PathVariable Integer userId) throws ResourceNotFoundException;

    @PostMapping("/users/{userId}/creditDetails")
    public ResponseEntity<UserCreditDetails> createUserCreditDetails(@PathVariable Integer userId, @Valid @RequestBody UserCreditDetailsDto userCreditDetailsDto) throws ResourceNotFoundException;

    @PutMapping("/users/{userId}/creditDetails")
    public ResponseEntity<UserCreditDetails> updateUserCreditDetails(@PathVariable Integer userId, @RequestBody UserCreditDetailsDto userCreditDetailsDto) throws ResourceNotFoundException;

}

