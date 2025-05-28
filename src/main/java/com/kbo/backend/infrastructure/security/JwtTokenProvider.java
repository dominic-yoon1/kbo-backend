package com.kbo.backend.infrastructure.security;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kbo.backend.domain.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secretKeyString;
	private Key key;

	@Value("${jwt.access-expiration}")
	private Long accessExpiration;

	@Value("${jwt.refresh-expiration}")
	private Long refreshExpiration;

	/**
	 * 1. SecretKey 초기화
	 * secretKey를 기반으로 HMAC-SHA 암호화 키를 생성
	 * 애플리케이션 실행 시 한 번만 초기화
	 *  Key Vs. SecretKey
	 *  Key - 대칭, 비대칭키 모두를 포함
	 *  SecretKey - 더 엄격한 타입으로 대칭키만을 포현 (Key의 하위 인터페이스)
	 */
	@PostConstruct
	public void init() {
		byte[] keyBytes = Base64.getDecoder().decode(secretKeyString);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * 2. JWT 토큰 생성
	 * user 정보로 JWT 토큰 생성
	 * @param user
	 * @return 문자열 JWT 토큰
	 */
	public String createToken(User user) {
		return Jwts.builder()
			.setSubject(user.getEmail())
			.claim("role", user.getRole().name())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();

	}

	public String createRefreshToken(User user) {
		return Jwts.builder()
			.setSubject(user.getEmail())
			.claim("role", user.getRole().name())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	/**
	 * 3. Token 검증
	 * 클라이언트로부터 전달받은 JWT가 유효한 토큰인지 검증
	 * @param token
	 * @return true / false
	 */
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

			return !claims.getBody().getExpiration().before(new Date());
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	// Token에서 사용자 Email 추출 메서드
	public String getUserEmailFromToken(String token) {
		return	Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	// Token에서 사용자 Role 추출 메서드
	public String getUserRoleFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("role", String.class);
	}

	// 헤더에서 Token 추출하는 메서드
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");

		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}

}
