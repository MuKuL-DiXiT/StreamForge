package com.stream.streamforge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class UserTable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank
  private String name;
  
  @NotBlank
  @Email
  @Column(unique = true)
  private String email;
  
  @NotBlank
  private String password;

  
  @NotBlank
  private String role;
  
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
	return role;
  }

  public void setRole(String role) {
	this.role = role;
  }
}
