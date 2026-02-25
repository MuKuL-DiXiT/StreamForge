package com.stream.streamforge.service;

import org.springframework.stereotype.Service;

import com.stream.streamforge.model.UserTable;
import com.stream.streamforge.repository.UserRepository;

@Service
public class StreamKeyService {
	private final UserRepository userRepository;
	public StreamKeyService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	public String getStreamKey(String email) {
		UserTable user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));
		return user.getStreamKey();
	}
}
