package com.alansep.heroes_mcp.adapter.in.web;

import com.alansep.heroes_mcp.adapter.in.web.dto.HeroRequest;
import com.alansep.heroes_mcp.adapter.in.web.dto.HeroResponse;
import com.alansep.heroes_mcp.application.port.in.CreateHeroCommand;
import com.alansep.heroes_mcp.application.port.in.UpdateHeroCommand;
import com.alansep.heroes_mcp.domain.exception.InvalidHeroException;
import com.alansep.heroes_mcp.domain.model.Hero;

public final class HeroWebMapper {

	private HeroWebMapper() {
	}

	public static CreateHeroCommand toCreateCommand(final HeroRequest request) {
		return new CreateHeroCommand(
				request.name(),
				request.powers(),
				requireAge(request.age()),
				request.gender(),
				request.universe()
		);
	}

	public static UpdateHeroCommand toUpdateCommand(final HeroRequest request) {
		return new UpdateHeroCommand(
				request.name(),
				request.powers(),
				requireAge(request.age()),
				request.gender(),
				request.universe()
		);
	}

	public static HeroResponse toResponse(final Hero hero) {
		return new HeroResponse(
				hero.id(),
				hero.name(),
				hero.powers(),
				hero.age(),
				hero.gender(),
				hero.universe()
		);
	}

	private static int requireAge(final Integer age) {
		if (age == null) {
			throw new InvalidHeroException("Hero age is required");
		}
		return age;
	}
}
