package com.alansep.heroes_mcp.application.port.out;

import com.alansep.heroes_mcp.domain.model.Hero;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port: what the application needs from a persistence mechanism.
 */
public interface HeroRepositoryPort {

	Hero save(final Hero hero);

	Optional<Hero> findById(final UUID id);

	List<Hero> findAll();

	void deleteById(final UUID id);
}
