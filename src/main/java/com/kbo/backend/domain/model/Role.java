package com.kbo.backend.domain.model;

import lombok.Getter;

@Getter
public enum Role {

	USER("일반 유저"),
	FAN("KBO 팬"),
	ADMIN("관리자"),
	MASTER("최고관리자");

	private final String description;

	Role(String description) {
		this.description = description;
	}
}
