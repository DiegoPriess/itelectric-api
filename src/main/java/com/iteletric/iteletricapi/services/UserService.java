package com.iteletric.iteletricapi.services;

import com.iteletric.iteletricapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	public UserService(final UserRepository repository) {
		this.repository = repository;
	}

	private final UserRepository repository;
}
