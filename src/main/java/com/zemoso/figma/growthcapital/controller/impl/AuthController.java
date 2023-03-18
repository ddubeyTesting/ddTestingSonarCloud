package com.zemoso.figma.growthcapital.controller.impl;

import com.zemoso.figma.growthcapital.controller.AuthOperations;
import com.zemoso.figma.growthcapital.dto.AuthDto;
import com.zemoso.figma.growthcapital.dto.UserDto;
import com.zemoso.figma.growthcapital.entity.User;
import com.zemoso.figma.growthcapital.exceptions.ItemExistsException;
import com.zemoso.figma.growthcapital.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthOperations {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> loginUser(@Valid @RequestBody AuthDto authModel) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authModel.getEmail(), authModel.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserDto user) throws ItemExistsException {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }
}