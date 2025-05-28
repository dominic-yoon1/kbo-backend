package com.kbo.backend.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbo.backend.application.response.LoginResponseDto;
import com.kbo.backend.application.response.SignupResponseDto;
import com.kbo.backend.application.service.AuthService;
import com.kbo.backend.presentation.request.LoginRequestDto;
import com.kbo.backend.presentation.request.SignupRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
		SignupResponseDto responseDto = authService.signup(requestDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto req) {
		LoginResponseDto res = authService.login(req);

		return ResponseEntity.status(HttpStatus.OK).body(res);
	}
}
