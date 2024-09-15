package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.config.security.JwtTokenService;
import com.iteletric.iteletricapi.config.security.SecurityConfiguration;
import com.iteletric.iteletricapi.dtos.user.UserRequest;
import com.iteletric.iteletricapi.enums.user.RoleName;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private UserRepository repository;

    @Mock
    private SecurityConfiguration securityConfiguration;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserEmailAlreadyExists() {
        UserRequest request = new UserRequest("John Doe", "john.doe@example.com", "password", RoleName.ROLE_CUSTOMER);
        when(repository.existsByEmail(anyString())).thenReturn(true);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userService.create(request);
        });

        assertEquals("O e-mail informado já está em uso", thrown.getMessage());
    }

    @Test
    void testUpdateUserNotFound() {
        UserRequest request = new UserRequest("John Doe", "john.doe@example.com", "password", RoleName.ROLE_CUSTOMER);
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userService.update(1L, request);
        });

        assertEquals("Usuário não encontrado", thrown.getMessage());
    }

    @Test
    void testDeleteUserSuccess() {
        User user = new User();
        user.setDeleted(0);
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(repository).save(user);
        assertEquals(1, user.getDeleted());
    }

    @Test
    void testDeleteUserNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userService.delete(1L);
        });

        assertEquals("Usuário não encontrado", thrown.getMessage());
    }

    @Test
    void testDeleteUserAlreadyDeleted() {
        User user = new User();
        user.setDeleted(1);
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userService.delete(1L);
        });

        assertEquals("Usuário já está desativado", thrown.getMessage());
    }

    @Test
    void testGetByIdSuccess() {
        User user = new User();
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        User result = userService.getById(1L);

        assertEquals(user, result);
    }

    @Test
    void testGetByIdNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userService.getById(1L);
        });

        assertEquals("Usuário não encontrado", thrown.getMessage());
    }

    @Test
    void testListUsers() {
        Pageable pageable = mock(Pageable.class);
        List<User> userList = List.of(new User(), new User());
        Page<User> userPage = new PageImpl<>(userList);
        when(repository.findAll(pageable)).thenReturn(userPage);

        Page<User> result = userService.list(pageable);

        assertEquals(userPage, result);
    }
}
