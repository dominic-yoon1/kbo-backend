package com.kbo.backend.application.response;

import com.kbo.backend.domain.model.Role;
import com.kbo.backend.domain.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupResponseDto {

	private String email;
	private String username;
	private String nickname;
	private Role role;

	public static SignupResponseDto from(User user) {
		return SignupResponseDto.builder()
			.email(user.getEmail())
			.username(user.getUsername())
			.nickname(user.getNickname())
			.role(user.getRole())
			.build();
	}
}
