package com.alansep.heroes_mcp.application.port.in;

import com.alansep.heroes_mcp.domain.model.Universe;

import java.util.List;

public record UpdateHeroCommand(
		String name,
		List<String> powers,
		int age,
		String gender,
		Universe universe
) {
}
