package com.stream.streamforge.dto;

import jakarta.validation.constraints.*;

public class SignupRequest {
	   private String name;
	   @Email
	   private String email;
	   @Size(min = 8, message = "Min 8 chars")
	   @Pattern(regexp = "^[^\\s]+$", message = "No spaces allowed")
	   private String password;
	   private String role;

	   public String getEmail() { return email; }
	   public void setEmail(String email) { this.email = email; }

	   public String getPassword() { return password; }
	   public void setPassword(String password) { this.password = password; }

	   public String getRole() { return role; }
	   public void setRole(String role) { this.role = role; }
	   public String getName() {
		return name;
	   }
	   public void setName(String name) {
		this.name = name;
	   }
}

