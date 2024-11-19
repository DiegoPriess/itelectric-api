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
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityConfiguration securityConfiguration;

    @Mock
    private MailService mailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("diegopriess.dev@gmail.com")
                .password("password")
                .role(RoleName.ROLE_CUSTOMER)
                .deleted(0)
                .build();
        user.setId(1L);
    }

    @Test
    void authenticate_ShouldReturnLoginResponse() {
        LoginRequest loginRequest = new LoginRequest("diegopriess.dev@gmail.com", "password");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenService.generateToken(userDetails)).thenReturn("token");

        LoginResponse response = userService.authenticate(loginRequest);

        assertEquals(1L, response.getUserId());
        assertEquals("token", response.getToken());
    }

    @Test
    void getCurrentUser_ShouldReturnCurrentUser() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        User result = userService.getCurrentUser();

        assertNotNull(result);
        assertEquals("diegopriess.dev@gmail.com", result.getEmail());
    }

    @Test
    void createCustomerIfNecessary_ShouldCreateNewCustomer() {
        User savedUser = User.builder()
                .email("diegopriess.dev@gmail.com")
                .password("encodedPassword")
                .role(RoleName.ROLE_CUSTOMER)
                .deleted(0)
                .build();
        savedUser.setId(1L);

        when(userRepository.findByEmail("diegopriess.dev@gmail.com")).thenReturn(Optional.empty());
        when(securityConfiguration.passwordEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(mailService.createWelcomeEmail(anyString(), anyString())).thenReturn("Welcome email content");

        User result = userService.createCustomerIfNecessary("diegopriess.dev@gmail.com");

        assertNotNull(result);
        assertEquals("diegopriess.dev@gmail.com", result.getEmail());
        verify(userRepository).save(any(User.class));
        verify(mailService).sendHtml(eq("diegopriess.dev@gmail.com"), eq("Bem-vindo ao itelectric!!!"), eq("Welcome email content"));
    }


    @Test
    void create_ShouldThrowExceptionWhenEmailExists() {
        UserRequest request = new UserRequest();
        request.setEmail("diegopriess.dev@gmail.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        Exception exception = assertThrows(BusinessException.class, () -> userService.create(request));
        assertEquals("O e-mail informado já está em uso", exception.getMessage());
    }

    @Test
    void update_ShouldUpdateUserDetails() {
        UserRequest request = new UserRequest();
        request.setEmail("newemail@gmail.com");
        request.setName("New Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.update(1L, request);

        assertEquals("newemail@gmail.com", user.getEmail());
        assertEquals("New Name", user.getName());
        verify(userRepository).save(user);
    }

    @Test
    void delete_ShouldMarkUserAsDeleted() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        assertEquals(1, user.getDeleted());
        verify(userRepository).save(user);
    }

    @Test
    void delete_ShouldThrowExceptionIfUserAlreadyDeleted() {
        user.setDeleted(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(BusinessException.class, () -> userService.delete(1L));
        assertEquals("Usuário já está desativado", exception.getMessage());
    }

    @Test
    void list_ShouldReturnFilteredUsersByRole() {
        Page<User> usersPage = new PageImpl<>(Collections.singletonList(user));
        Pageable pageable = PageRequest.of(0, 5);

        when(userRepository.findByRole(RoleName.ROLE_CUSTOMER, pageable)).thenReturn(usersPage);

        Page<UserResponse> result = userService.list(RoleName.ROLE_CUSTOMER, pageable);

        assertEquals(1, result.getTotalElements());
        verify(userRepository).findByRole(RoleName.ROLE_CUSTOMER, pageable);
    }
}
