package com.kbo.backend.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kbo.backend.application.response.SignupResponseDto;
import com.kbo.backend.application.response.TokenDto;
import com.kbo.backend.domain.model.User;
import com.kbo.backend.domain.repository.UserRepository;
import com.kbo.backend.infrastructure.security.JwtTokenProvider;
import com.kbo.backend.presentation.request.LoginRequestDto;
import com.kbo.backend.presentation.request.SignupRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	@Transactional
	public SignupResponseDto signup(SignupRequestDto req) {
		// 1. email 중복 확인
		if (userRepository.findByEmail(req.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Email already in use");
		}

		String encodedPassword = passwordEncoder.encode(req.getPassword());

		// 2. 새로운 user 생성
		User newUser = User.signup(req, encodedPassword);

		// 3. 새로운 user DB 저장
		userRepository.save(newUser);

		return SignupResponseDto.from(newUser);
	}

	@Transactional
	public TokenDto login(LoginRequestDto req) {
		// 1. email 존재 여부 확인
		User user = userRepository.findByEmail(req.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("ID 혹은 PW가 잘 못 입력되었습니다."));

		// 2. 비밀번호 확인
		if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("ID 혹은 PW가 잘 못 입력되었습니다.");
		}

		// 3. JWT 생성
		String accessToken = jwtTokenProvider.createAccessToken(user);
		String refreshToken = jwtTokenProvider.createRefreshToken(user);

		// 4. user에 refresh token 추가
		user.updateRefreshToken(refreshToken);

		// 5. update 된 user DB 저장
		userRepository.save(user);

		return TokenDto.from(accessToken, refreshToken);
	}

		return LoginResponseDto.from(token);
	}
}
