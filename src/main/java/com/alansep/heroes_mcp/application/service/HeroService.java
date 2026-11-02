package com.alansep.heroes_mcp.application.service;

import com.alansep.heroes_mcp.application.port.in.CreateHeroCommand;
import com.alansep.heroes_mcp.application.port.in.HeroUseCase;
import com.alansep.heroes_mcp.application.port.in.UpdateHeroCommand;
import com.alansep.heroes_mcp.application.port.out.HeroRepositoryPort;
import com.alansep.heroes_mcp.domain.exception.HeroNotFoundException;
import com.alansep.heroes_mcp.domain.model.Hero;

import java.util.List;
import java.util.UUID;

public class HeroService implements HeroUseCase {

	private final HeroRepositoryPort repository;

	public HeroService(final HeroRepositoryPort repository) {
		this.repository = repository;
	}

	@Override
	public Hero create(final CreateHeroCommand command) {
		final var hero = Hero.create(
				command.name(),
				command.powers(),
				command.age(),
				command.gender(),
				command.universe()
		);
		return repository.save(hero);
	}

	@Override
	public Hero findById(final UUID id) {
		return repository.findById(id)
				.orElseThrow(() -> new HeroNotFoundException(id));
	}

	@Override
	public List<Hero> findAll() {
		return repository.findAll();
	}

	@Override
	public Hero update(final UUID id, final UpdateHeroCommand command) {
		final var current = findById(id);
		final var updated = new Hero(
				current.id(),
				command.name(),
				command.powers(),
				command.age(),
				command.gender(),
				command.universe()
		);
		return repository.save(updated);
	}

	@Override
	public void delete(final UUID id) {
		findById(id);
		repository.deleteById(id);
	}
}
