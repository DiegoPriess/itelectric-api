package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exeption.BusinessException;
import com.iteletric.iteletricapi.config.security.JwtTokenService;
import com.iteletric.iteletricapi.config.security.SecurityConfiguration;
import com.iteletric.iteletricapi.dtos.user.UserRequest;
import com.iteletric.iteletricapi.dtos.user.LoginRequest;
import com.iteletric.iteletricapi.dtos.user.LoginResponse;
import com.iteletric.iteletricapi.models.Role;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.models.UserDetailsImpl;
import com.iteletric.iteletricapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private UserRepository repository;

	@Autowired
	private SecurityConfiguration securityConfiguration;

	public LoginResponse authenticateUser(LoginRequest request) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(request.email(), request.password());

		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return new LoginResponse(jwtTokenService.generateToken(userDetails));
	}

	public void createUser(UserRequest request) {

		if (repository.existsByEmail(request.email())) throw new BusinessException("O e-mail informado já está em uso");

		User newUser = User.builder()
						   .name(request.name())
						   .email(request.email())
						   .password(securityConfiguration.passwordEncoder().encode(request.password()))
						   .roles(List.of(Role.builder().name(request.role()).build()))
						   .build();

		repository.save(newUser);
	}
}