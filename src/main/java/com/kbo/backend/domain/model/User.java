package com.kbo.backend.domain.model;

import com.kbo.backend.presentation.request.SignupRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_users")
public class User {

	@Builder
	public User(String email, String password, String username, String nickname, Role role) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.nickname = nickname;
		this.role = role;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(length = 512)
	private String refreshToken;

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public static User signup(SignupRequestDto req, String encodedPassword) {
		return User.builder()
			.email(req.getEmail())
			.password(encodedPassword)
			.username(req.getUsername())
			.nickname(req.getNickname())
			.role(Role.USER)
			.build();
	}
}
