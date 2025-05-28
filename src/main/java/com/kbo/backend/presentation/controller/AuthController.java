package com.kbo.backend.presentation.controller;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kbo.backend.application.response.LoginResponseDto;
import com.kbo.backend.application.response.SignupResponseDto;
import com.kbo.backend.application.response.TokenDto;
import com.kbo.backend.application.service.AuthService;
import com.kbo.backend.presentation.request.LoginRequestDto;
import com.kbo.backend.presentation.request.SignupRequestDto;

import jakarta.servlet.http.HttpServletResponse;
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
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
		// 1. 로그인 처리 및 토큰 생성
		TokenDto tokenDto = authService.login(requestDto);

		// 2. refreshToken을 HttpOnly 쿠키로 클라이언트에게 전달
		ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
			.httpOnly(true) // JS에서 접근 불가능
			.secure(false) // HTTPS 환경에서만 전송 (개발 중엔 false)
			.path("/") // 모든 경로에서 쿠키 전송
			.maxAge(Duration.ofDays(1)) // 유효기간 1일
			.sameSite("Strict") // CSRF 방지
			.build();

		// 3. 응답 헤더에 쿠키 설정
		res.setHeader("Set-Cookie", refreshCookie.toString());

		// 4. accessToken만 JSON 응답으로 전달
		return ResponseEntity.status(HttpStatus.OK).body(LoginResponseDto.from(tokenDto.getAccessToken()));
	}

		return ResponseEntity.status(HttpStatus.OK).body(res);
	}
}
