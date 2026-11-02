package com.alansep.heroes_mcp.application.port.in;

import com.alansep.heroes_mcp.domain.model.Hero;

import java.util.List;
import java.util.UUID;

/**
 * Inbound port: the operations the application offers to the outside world.
 */
public interface HeroUseCase {

	Hero create(final CreateHeroCommand command);

	Hero findById(final UUID id);

	List<Hero> findAll();

	Hero update(final UUID id, final UpdateHeroCommand command);

	void delete(final UUID id);
}
