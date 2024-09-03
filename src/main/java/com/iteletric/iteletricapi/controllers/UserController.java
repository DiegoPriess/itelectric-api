package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.dtos.user.UserRequest;
import com.iteletric.iteletricapi.dtos.user.LoginRequest;
import com.iteletric.iteletricapi.dtos.user.LoginResponse;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.repositories.user.UserRepository;
import com.iteletric.iteletricapi.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginRequest request) {
        LoginResponse token = userService.authenticate(request);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody @Valid UserRequest request) {
        userService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        userService.update(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<User>> list(Pageable pageable) {
        Page<User> users = userService.list(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
