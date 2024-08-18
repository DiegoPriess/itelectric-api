package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.dtos.user.UserRequest;
import com.iteletric.iteletricapi.dtos.user.LoginRequest;
import com.iteletric.iteletricapi.dtos.user.LoginResponse;
import com.iteletric.iteletricapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody @Valid LoginRequest request) {
        LoginResponse token = userService.authenticateUser(request);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserRequest request) {
        userService.createUser(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
