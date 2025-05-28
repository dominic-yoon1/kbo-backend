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
public class MyInfoResponseDto {

	private String email;
	private String username;
	private String nickname;
	private Role role;

	public static MyInfoResponseDto from(User user) {
		return MyInfoResponseDto.builder()
			.email(user.getEmail())
			.username(user.getUsername())
			.nickname(user.getNickname())
			.role(user.getRole())
			.build();
	}
}
