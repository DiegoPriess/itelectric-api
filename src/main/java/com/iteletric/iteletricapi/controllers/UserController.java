package com.iteletric.iteletricapi.controllers;

import com.iteletric.iteletricapi.config.validation.ValidationGroups;
import com.iteletric.iteletricapi.dtos.user.LoginRequest;
import com.iteletric.iteletricapi.dtos.user.LoginResponse;
import com.iteletric.iteletricapi.dtos.user.UserRequest;
import com.iteletric.iteletricapi.dtos.user.UserResponse;
import com.iteletric.iteletricapi.enums.user.RoleName;
import com.iteletric.iteletricapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuários", description = "Operações para gerenciamento de usuários")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Autenticar usuário", description = "Realiza o login de um usuário e retorna o token JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = userService.authenticate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Criar usuário", description = "Cria um novo usuário no sistema")
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody @Validated(ValidationGroups.POST.class) UserRequest request) {
        userService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza as informações de um usuário existente")
    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(@Parameter(description = "ID do usuário a ser atualizado") @PathVariable Long userId,
                                       @RequestBody @Validated(ValidationGroups.PUT.class) UserRequest request) {
        userService.update(userId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Excluir usuário", description = "Exclui um usuário do sistema pelo ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID do usuário a ser excluído") @PathVariable Long userId) {
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Recupera um usuário específico pelo seu ID")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getById(@Parameter(description = "ID do usuário") @PathVariable Long userId) {
        UserResponse user = userService.getById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Listar usuários", description = "Lista os usuários com paginação e filtro opcional pelo cargo")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(@RequestParam(required = false) RoleName role,
                                                   Pageable pageable) {
        Page<UserResponse> userList = userService.list(role, pageable);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
}
