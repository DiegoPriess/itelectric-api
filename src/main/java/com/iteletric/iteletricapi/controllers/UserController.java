package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.config.validation.ValidationGroups;
import com.iteletric.iteletricapi.dtos.user.LoginRequest;
import com.iteletric.iteletricapi.dtos.user.LoginResponse;
import com.iteletric.iteletricapi.dtos.user.UserRequest;
import com.iteletric.iteletricapi.dtos.user.UserResponse;
import com.iteletric.iteletricapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = userService.authenticate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody @Validated(ValidationGroups.POST.class) UserRequest request) {
        userService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(@PathVariable Long userId, @RequestBody @Validated(ValidationGroups.PUT.class) UserRequest request) {
        userService.update(userId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long userId) {
        UserResponse user = userService.getById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(@RequestParam(required = false) String role, Pageable pageable) {
        Page<UserResponse> userList = userService.list(role, pageable);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
}
