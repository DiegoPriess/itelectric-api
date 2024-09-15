package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.config.security.JwtTokenService;
import com.iteletric.iteletricapi.config.security.SecurityConfiguration;
import com.iteletric.iteletricapi.config.security.userauthentication.UserDetailsImpl;
import com.iteletric.iteletricapi.dtos.user.LoginRequest;
import com.iteletric.iteletricapi.dtos.user.LoginResponse;
import com.iteletric.iteletricapi.dtos.user.UserRequest;
import com.iteletric.iteletricapi.dtos.user.UserResponse;
import com.iteletric.iteletricapi.enums.user.RoleName;
import com.iteletric.iteletricapi.mappers.UserMapper;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateUserSuccess() {
        LoginRequest loginRequest = new LoginRequest("email@test.com", "password");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenService.generateToken(any(UserDetailsImpl.class))).thenReturn("mockToken");

        LoginResponse loginResponse = userService.authenticate(loginRequest);

        assertNotNull(loginResponse);
        assertEquals("mockToken", loginResponse.getToken());
    }

    @Test
    void createUserSuccess() {
        UserRequest userRequest = new UserRequest("John Doe", "email@test.com", "password", RoleName.ROLE_CUSTOMER);
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(securityConfiguration.passwordEncoder().encode(anyString())).thenReturn("encodedPassword");

        userService.create(userRequest);

        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void createUserEmailAlreadyExists() {
        UserRequest userRequest = new UserRequest("John Doe", "email@test.com", "password", RoleName.ROLE_CUSTOMER);
        when(repository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.create(userRequest));
    }

    @Test
    void updateUserSuccess() {
        Long userId = 1L;
        UserRequest userRequest = new UserRequest("John Doe", "newemail@test.com", "newpassword", RoleName.ROLE_CUSTOMER);
        User existingUser = new User();

        when(repository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(securityConfiguration.passwordEncoder().encode(anyString())).thenReturn("encodedPassword");

        userService.update(userId, userRequest);

        verify(repository, times(1)).save(existingUser);
    }

    @Test
    void updateUserNotFound() {
        Long userId = 1L;
        UserRequest userRequest = new UserRequest("John Doe", "email@test.com", "password", RoleName.ROLE_CUSTOMER);
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> userService.update(userId, userRequest));
    }

    @Test
    void deleteUserSuccess() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setDeleted(0);

        when(repository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.delete(userId);

        verify(repository, times(1)).save(existingUser);
        assertEquals(1, existingUser.getDeleted());
    }

    @Test
    void deleteUserAlreadyDeleted() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setDeleted(1);

        when(repository.findById(userId)).thenReturn(Optional.of(existingUser));

        assertThrows(BusinessException.class, () -> userService.delete(userId));
    }

    @Test
    void getByIdUserSuccess() {
        Long userId = 1L;
        User user = new User();
        UserResponse userResponse = new UserResponse(1L, "John Doe", "email@test.com", null, null);

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDTO(user)).thenReturn(userResponse);

        UserResponse result = userService.getById(userId);

        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
    }

    @Test
    void getByIdUserNotFound() {
        Long userId = 1L;
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> userService.getById(userId));
    }

    @Test
    void listUsersSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User();
        UserResponse userResponse = new UserResponse(1L, "John Doe", "email@test.com", null, null);
        Page<User> usersPage = new PageImpl<>(Collections.singletonList(user));

        when(repository.findAll(pageable)).thenReturn(usersPage);
        when(userMapper.toUserResponseDTO(user)).thenReturn(userResponse);

        Page<UserResponse> result = userService.list(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
