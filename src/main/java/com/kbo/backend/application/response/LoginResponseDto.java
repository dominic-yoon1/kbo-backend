package com.kbo.backend.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

	private String token;

	public static LoginResponseDto from(String token) {
		return LoginResponseDto.builder()
			.token(token)
			.build();
	}
}
