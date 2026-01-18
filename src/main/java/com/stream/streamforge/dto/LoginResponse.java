package com.stream.streamforge.dto;

public class LoginResponse {
	private Long id;
	private String email;
	private String token;
	public LoginResponse(Long id2, String email2, String token) {
		this.id = id2;
		this.email = email2;
		this.token = token;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
