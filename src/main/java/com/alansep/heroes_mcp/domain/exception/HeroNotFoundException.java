package com.alansep.heroes_mcp.domain.exception;

import java.util.UUID;

public class HeroNotFoundException extends RuntimeException {

	public HeroNotFoundException(final UUID id) {
		super("Hero not found: " + id);
	}
}
