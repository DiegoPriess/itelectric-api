package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.config.exception.BusinessException;
import com.iteletric.iteletricapi.config.security.JwtTokenService;
import com.iteletric.iteletricapi.config.security.SecurityConfiguration;
import com.iteletric.iteletricapi.config.security.userauthentication.UserDetailsImpl;
import com.iteletric.iteletricapi.dtos.user.LoginRequest;
import com.iteletric.iteletricapi.dtos.user.LoginResponse;
import com.iteletric.iteletricapi.dtos.user.UserRequest;
import com.iteletric.iteletricapi.dtos.user.UserResponse;
import com.iteletric.iteletricapi.mappers.UserMapper;
import com.iteletric.iteletricapi.models.Role;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenService jwtTokenService;
	private final UserRepository repository;
	private final SecurityConfiguration securityConfiguration;
	private final UserMapper userMapper;

	@Autowired
	UserService(AuthenticationManager authenticationManager,
				JwtTokenService jwtTokenService,
				UserRepository repository,
				SecurityConfiguration securityConfiguration,
				UserMapper userMapper) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenService = jwtTokenService;
		this.repository = repository;
		this.securityConfiguration = securityConfiguration;
		this.userMapper = userMapper;
	}

	public LoginResponse authenticate(LoginRequest request) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(request.email(), request.password());

		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

		return new LoginResponse(jwtTokenService.generateToken(userDetailsImpl));
	}

	public void create(UserRequest request) {
		if (repository.existsByEmail(request.email())) throw new BusinessException("O e-mail informado já está em uso");

		User newUser = User.builder()
						   .name(request.name())
						   .email(request.email())
						   .password(securityConfiguration.passwordEncoder().encode(request.password()))
						   .roles(List.of(Role.builder().name(request.role()).build()))
						   .deleted(0)
						   .build();

		repository.save(newUser);
	}

	public void update(Long userId, UserRequest request) {
		User user = repository.findById(userId)
						      .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

		user.setName(request.name());
		user.setEmail(request.email());
		user.setPassword(securityConfiguration.passwordEncoder().encode(request.password()));

		repository.save(user);
	}

	public void delete(Long userId) {
		User user = repository.findById(userId).orElseThrow(() -> new BusinessException("Usuário não encontrado"));
		if (user.getDeleted() == 1) throw new BusinessException("Usuário já está desativado");

		user.setDeleted(1);
		repository.save(user);
	}

	public UserResponse getById(Long userId) {
		User user = repository.findById(userId)
							  .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
		return userMapper.toUserResponseDTO(user);
	}

	public User getUserById(Long userId) {
		return repository.findById(userId).orElseThrow(() -> new BusinessException("Usuário não encontrado"));
	}

	public Page<UserResponse> list(Pageable pageable) {
		Page<User> users = repository.findAll(pageable);
		return users.map(userMapper::toUserResponseDTO);
	}
}
