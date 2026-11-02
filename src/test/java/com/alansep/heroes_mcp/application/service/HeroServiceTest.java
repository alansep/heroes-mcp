package com.alansep.heroes_mcp.application.service;

import com.alansep.heroes_mcp.adapter.out.persistence.InMemoryHeroRepository;
import com.alansep.heroes_mcp.application.port.in.CreateHeroCommand;
import com.alansep.heroes_mcp.application.port.in.HeroUseCase;
import com.alansep.heroes_mcp.application.port.in.UpdateHeroCommand;
import com.alansep.heroes_mcp.domain.exception.HeroNotFoundException;
import com.alansep.heroes_mcp.domain.model.Hero;
import com.alansep.heroes_mcp.domain.model.Universe;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HeroServiceTest {

	private final HeroUseCase service = new HeroService(new InMemoryHeroRepository());

	@Test
	void createsHero() {
		final var hero = service.create(new CreateHeroCommand(
				"Superman", List.of("Flight"), 35, "male", Universe.DC));

		assertThat(hero.id()).isNotNull();
		assertThat(hero.name()).isEqualTo("Superman");
		assertThat(service.findById(hero.id())).isEqualTo(hero);
	}

	@Test
	void findsAllHeroesSortedByName() {
		service.create(new CreateHeroCommand("Superman", List.of("Flight"), 35, "male", Universe.DC));
		service.create(new CreateHeroCommand("Batman", List.of("Intelligence"), 40, "male", Universe.DC));

		assertThat(service.findAll())
				.extracting(Hero::name)
				.containsExactly("Batman", "Superman");
	}

	@Test
	void updatesHeroKeepingTheSameId() {
		final var created = service.create(new CreateHeroCommand(
				"Superman", List.of("Flight"), 35, "male", Universe.DC));

		final var updated = service.update(created.id(), new UpdateHeroCommand(
				"Homelander", List.of("Flight", "Heat vision"), 40, "male", Universe.THE_BOYS));

		assertThat(updated.id()).isEqualTo(created.id());
		assertThat(updated.name()).isEqualTo("Homelander");
		assertThat(updated.universe()).isEqualTo(Universe.THE_BOYS);
		assertThat(service.findById(created.id())).isEqualTo(updated);
	}

	@Test
	void failsToUpdateUnknownHero() {
		final var id = UUID.randomUUID();
		final var command = new UpdateHeroCommand("Nobody", List.of(), 20, "other", Universe.MARVEL);

		assertThatThrownBy(() -> service.update(id, command))
				.isInstanceOf(HeroNotFoundException.class)
				.hasMessageContaining(id.toString());
	}

	@Test
	void deletesHero() {
		final var created = service.create(new CreateHeroCommand(
				"Superman", List.of("Flight"), 35, "male", Universe.DC));

		service.delete(created.id());

		assertThatThrownBy(() -> service.findById(created.id()))
				.isInstanceOf(HeroNotFoundException.class);
	}

	@Test
	void failsToDeleteUnknownHero() {
		final var id = UUID.randomUUID();

		assertThatThrownBy(() -> service.delete(id))
				.isInstanceOf(HeroNotFoundException.class)
				.hasMessageContaining(id.toString());
	}
}
