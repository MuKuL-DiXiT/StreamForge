package com.stream.streamforge.repository;
import com.stream.streamforge.model.UserTable;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserTable, Long> {
	Optional<UserTable> findByEmail(String email);
}
