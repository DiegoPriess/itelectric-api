package com.itelectric.itelectricapi.controllers;

import com.itelectric.itelectricapi.config.validation.ValidationGroups;
import com.itelectric.itelectricapi.dtos.user.LoginRequest;
import com.itelectric.itelectricapi.dtos.user.LoginResponse;
import com.itelectric.itelectricapi.dtos.user.UserRequest;
import com.itelectric.itelectricapi.dtos.user.UserResponse;
import com.itelectric.itelectricapi.enums.user.RoleName;
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
    public ResponseEntity<Page<UserResponse>> list(@RequestParam(required = false) RoleName role, Pageable pageable) {
        Page<UserResponse> userList = userService.list(role, pageable);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
}
