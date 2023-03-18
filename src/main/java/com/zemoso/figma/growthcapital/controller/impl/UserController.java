package com.zemoso.figma.growthcapital.controller.impl;

import com.zemoso.figma.growthcapital.controller.UserOperations;
import com.zemoso.figma.growthcapital.dto.UserCreditDetailsDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.Cashkick;
import com.zemoso.figma.growthcapital.entity.Contract;
import com.zemoso.figma.growthcapital.entity.User;
import com.zemoso.figma.growthcapital.entity.UserCreditDetails;
import com.zemoso.figma.growthcapital.exceptions.ResourceNotFoundException;
import com.zemoso.figma.growthcapital.response.ResponseHandler;
import com.zemoso.figma.growthcapital.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController implements UserOperations {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseHandler.generateResponse("Successfull Operation", HttpStatus.OK, userService.getAllUsers());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.getUserById(Long.valueOf(userId)), HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody UserDto userDto, @PathVariable Integer userId) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.updateUser(userDto, Long.valueOf(userId)), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Integer userId) throws ResourceNotFoundException {
        userService.deleteUserById(Long.valueOf(userId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/{userId}/contracts")
    public ResponseEntity<List<Contract>> getUserContracts(@PathVariable Integer userId) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.getUserContracts(Long.valueOf(userId)), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/cashkicks")
    public ResponseEntity<List<Cashkick>> getUserCashkicks(@PathVariable Integer userId) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.getUserCashkicks(Long.valueOf(userId)), HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/creditDetails")
    public ResponseEntity<UserCreditDetails> createUserCreditDetails(@PathVariable Integer userId, @Valid @RequestBody UserCreditDetailsDto userCreditDetailsDto) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.createUserCreditDetails(userCreditDetailsDto, Long.valueOf(userId)), HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}/creditDetails")
    public ResponseEntity<UserCreditDetails> updateUserCreditDetails(@PathVariable Integer userId, @RequestBody UserCreditDetailsDto userCreditDetailsDto) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.updateUserCreditDetails(userCreditDetailsDto, Long.valueOf(userId)), HttpStatus.OK);
    }
}

