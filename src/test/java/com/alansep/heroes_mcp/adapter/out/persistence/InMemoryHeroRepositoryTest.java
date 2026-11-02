package com.alansep.heroes_mcp.adapter.out.persistence;

import com.alansep.heroes_mcp.domain.model.Hero;
import com.alansep.heroes_mcp.domain.model.Universe;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryHeroRepositoryTest {

	private final InMemoryHeroRepository repository = new InMemoryHeroRepository();

	@Test
	void savesAndFindsHeroById() {
		final var hero = Hero.create("Superman", List.of("Flight"), 35, "male", Universe.DC);

		repository.save(hero);

		assertThat(repository.findById(hero.id())).contains(hero);
	}

	@Test
	void returnsEmptyWhenHeroDoesNotExist() {
		assertThat(repository.findById(UUID.randomUUID())).isEmpty();
	}

	@Test
	void listsHeroesSortedByName() {
		repository.save(Hero.create("Superman", List.of("Flight"), 35, "male", Universe.DC));
		repository.save(Hero.create("Batman", List.of("Intelligence"), 40, "male", Universe.DC));

		assertThat(repository.findAll())
				.extracting(Hero::name)
				.containsExactly("Batman", "Superman");
	}

	@Test
	void deletesHeroById() {
		final var hero = Hero.create("Superman", List.of("Flight"), 35, "male", Universe.DC);
		repository.save(hero);

		repository.deleteById(hero.id());

		assertThat(repository.findById(hero.id())).isEmpty();
	}
}
