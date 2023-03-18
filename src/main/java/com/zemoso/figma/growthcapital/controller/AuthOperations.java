package com.zemoso.figma.growthcapital.controller;

import com.zemoso.figma.growthcapital.dto.AuthDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.User;
import com.zemoso.figma.growthcapital.exceptions.ItemExistsException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthOperations {
    @PostMapping("/login")
    public ResponseEntity<HttpStatus> loginUser(@Valid @RequestBody AuthDto authModel);

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserDto user) throws ItemExistsException;
}

