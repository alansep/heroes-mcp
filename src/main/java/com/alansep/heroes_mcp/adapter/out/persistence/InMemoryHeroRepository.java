package com.alansep.heroes_mcp.adapter.out.persistence;

import com.alansep.heroes_mcp.application.port.out.HeroRepositoryPort;
import com.alansep.heroes_mcp.domain.model.Hero;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryHeroRepository implements HeroRepositoryPort {

	private final Map<UUID, Hero> store = new ConcurrentHashMap<>();

	@Override
	public Hero save(final Hero hero) {
		store.put(hero.id(), hero);
		return hero;
	}

	@Override
	public Optional<Hero> findById(final UUID id) {
		return Optional.ofNullable(store.get(id));
	}

	@Override
	public List<Hero> findAll() {
		return store.values().stream()
				.sorted(Comparator.comparing(Hero::name))
				.toList();
	}

	@Override
	public void deleteById(final UUID id) {
		store.remove(id);
	}
}
