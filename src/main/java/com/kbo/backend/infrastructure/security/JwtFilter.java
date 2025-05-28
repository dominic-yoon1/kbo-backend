package com.kbo.backend.infrastructure.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
		FilterChain filterChain) throws ServletException, IOException {
		// 1. 헤더 추출 및 검증
		String token = jwtTokenProvider.resolveToken(req);

		// 2. 토큰 유효성 및 사용자 인증 설정
		if (token != null && jwtTokenProvider.validateToken(token)) {
			String email = jwtTokenProvider.getUserEmailFromToken(token);
			String role = jwtTokenProvider.getUserRoleFromToken(token);

			// 3. 인증 객체 생성 및 Security Context 설정
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				email, "", Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role))
			);

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		// 요청 필터 페인 실행
		try {
			filterChain.doFilter(req, res);
		} finally {
			SecurityContextHolder.clearContext();
		}
	}
}
