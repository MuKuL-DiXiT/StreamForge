package com.stream.streamforge.dto;

public class SignupResponse {
	private Long id;
    private String email;
    private String role;
    public SignupResponse() {};
    public SignupResponse(Long id2, String email2, String role2) {
    	this.id = id2;
    	this.email = email2;
    	this.role = role2;
	}
	public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}

