package com.stream.streamforge.controller;
import org.springframework.web.bind.annotation.*;
import com.stream.streamforge.dto.*;
import com.stream.streamforge.service.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
	public AuthService authService;
	AuthController(AuthService authService){
		this.authService = authService;
	}
	@PostMapping("/signup")
	public SignupResponse signup(@RequestBody SignupRequest req) {
		return authService.signupService(req);
	}
	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest req) {
		return authService.loginService(req);
	}
	
}
