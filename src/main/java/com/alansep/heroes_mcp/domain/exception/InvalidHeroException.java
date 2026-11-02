package com.alansep.heroes_mcp.domain.exception;

public class InvalidHeroException extends RuntimeException {

	public InvalidHeroException(final String message) {
		super(message);
	}
}
