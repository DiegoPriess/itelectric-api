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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UserService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenService jwtTokenService;
	private final UserRepository repository;
	private final SecurityConfiguration securityConfiguration;
	private final MailService mailService;

	private static final String USER_NOT_FOUND_MESSAGE = "Usuário não encontrado";

	@Autowired
	UserService(AuthenticationManager authenticationManager,
				JwtTokenService jwtTokenService,
				UserRepository repository,
				SecurityConfiguration securityConfiguration,
				MailService mailService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenService = jwtTokenService;
		this.repository = repository;
		this.securityConfiguration = securityConfiguration;
		this.mailService = mailService;
	}

	public LoginResponse authenticate(LoginRequest request) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

		return new LoginResponse(
				userDetailsImpl.getId(),
				jwtTokenService.generateToken(userDetailsImpl),
				userDetailsImpl.getAuthorities().iterator().next().getAuthority()
		);
	}

	public void create(UserRequest request) {
		if (repository.existsByEmail(request.getEmail())) throw new BusinessException("O e-mail informado já está em uso");

		User newUser = User.builder()
						   .name(request.getName())
						   .email(request.getEmail())
						   .password(securityConfiguration.passwordEncoder().encode(request.getPassword()))
						   .role(request.getRole())
						   .deleted(0)
						   .build();

		repository.save(newUser);
	}

	public void update(Long userId, UserRequest request) {
		User user = repository.findById(userId)
						      .orElseThrow(() -> new BusinessException(USER_NOT_FOUND_MESSAGE));

		if (request.getName() != null) user.setName(request.getName());
		if (request.getEmail() != null) user.setEmail(request.getEmail());
		if (request.getPassword() != null) user.setPassword(securityConfiguration.passwordEncoder().encode(request.getPassword()));

		repository.save(user);
	}

	public void delete(Long userId) {
		User user = repository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND_MESSAGE));
		if (user.getDeleted() == 1) throw new BusinessException("Usuário já está desativado");

		user.setDeleted(1);
		repository.save(user);
	}

	public UserResponse getById(Long userId) {
		User user = repository.findById(userId)
							  .orElseThrow(() -> new BusinessException(USER_NOT_FOUND_MESSAGE));
		return UserResponse.convert(user);
	}

	public User getUserById(Long userId) {
		return repository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND_MESSAGE));
	}

	public Page<UserResponse> list(RoleName role, Pageable pageable) {
		Page<User> users;

		if (role != null) {
			users = repository.findByRole(role, pageable);
		} else {
			users = repository.findAll(pageable);
		}

		return UserResponse.convert(users);
	}

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == "anonymousUser") return null;

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		return userDetails.getUser();
	}

	public User createCustomerIfNecessary(String email) {
		Optional<User> user = repository.findByEmail(email);
		if (user.isPresent()) return user.get();

		String pass = generatePassword();
		User newUser = User.builder()
				.name(email)
				.email(email)
				.password(securityConfiguration.passwordEncoder().encode(pass))
				.role(RoleName.ROLE_CUSTOMER)
				.deleted(0)
				.build();

		this.mailService.sendHtml(email, "Bem-vindo ao itelectric!!!", this.mailService.createWelcomeEmail(email, pass));

		return repository.save(newUser);
	}

	public static String generatePassword() {
		final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder(6);

		for (int i = 0; i < 6; i++) {
			int pas = random.nextInt(characters.length());
			password.append(characters.charAt(pas));
		}

		return password.toString();
	}
}
