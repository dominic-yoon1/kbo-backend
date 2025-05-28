package com.kbo.backend.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequestDto {

	private String refreshToken;

	public static RefreshTokenRequestDto from(String refreshToken) {
		return RefreshTokenRequestDto.builder()
			.refreshToken(refreshToken)
			.build();
	}
}
