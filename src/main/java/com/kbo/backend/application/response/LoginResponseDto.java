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

	private String accessToken;

	public static LoginResponseDto from(String accessToken) {
		return LoginResponseDto.builder()
			.accessToken(accessToken)
			.build();
	}
}
