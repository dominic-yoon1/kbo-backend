package com.kbo.backend.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtFilter jwtFilter;

	// Security 핵심 설정
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(csrf -> csrf.disable()) // CSRF 비활성화
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Session 사용 안 함
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/api/v1/**",
					"/h2-console/**")
				.permitAll() // 인증 없이 허용
				.anyRequest().authenticated()) // 나머지는 인증 필요
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JWT Filter 등록
			.headers(headers -> headers.frameOptions().disable()) // H2 콘솔 사용 위해 FrameOptions 비활성화
			.build();
	}

	// 비밀번호 인코더 등록
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
