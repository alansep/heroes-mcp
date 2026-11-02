package com.alansep.heroes_mcp.domain.model;

import com.alansep.heroes_mcp.domain.exception.InvalidHeroException;

import java.util.List;
import java.util.UUID;

/**
 * Core domain entity. It knows nothing about HTTP, frameworks or persistence.
 */
public record Hero(
		UUID id,
		String name,
		List<String> powers,
		int age,
		String gender,
		Universe universe
) {

	public Hero {
		if (id == null) {
			throw new InvalidHeroException("Hero id is required");
		}
		name = requireText(name, "Hero name is required");
		powers = normalizePowers(powers);
		if (age < 0) {
			throw new InvalidHeroException("Hero age must not be negative");
		}
		gender = requireText(gender, "Hero gender is required");
		if (universe == null) {
			throw new InvalidHeroException("Hero universe is required");
		}
	}

	public static Hero create(final String name, final List<String> powers, final int age, final String gender,
			final Universe universe) {
		return new Hero(UUID.randomUUID(), name, powers, age, gender, universe);
	}

	private static String requireText(final String value, final String message) {
		if (value == null || value.isBlank()) {
			throw new InvalidHeroException(message);
		}
		return value.trim();
	}

	private static List<String> normalizePowers(final List<String> powers) {
		if (powers == null) {
			return List.of();
		}
		return powers.stream()
				.map(power -> requireText(power, "Power must not be blank"))
				.toList();
	}
}
