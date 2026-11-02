package com.alansep.heroes_mcp.domain.model;

import com.alansep.heroes_mcp.domain.exception.InvalidHeroException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HeroTest {

	@Test
	void createsHeroWithGeneratedIdAndNormalizedText() {
		final var hero = Hero.create("  Superman  ", List.of(" Flight ", " Super strength "), 35, " male ", Universe.DC);

		assertThat(hero.id()).isNotNull();
		assertThat(hero.name()).isEqualTo("Superman");
		assertThat(hero.powers()).containsExactly("Flight", "Super strength");
		assertThat(hero.age()).isEqualTo(35);
		assertThat(hero.gender()).isEqualTo("male");
		assertThat(hero.universe()).isEqualTo(Universe.DC);
	}

	@Test
	void acceptsHeroWithoutPowers() {
		final var hero = Hero.create("Batman", null, 40, "male", Universe.DC);

		assertThat(hero.powers()).isEmpty();
	}

	@Test
	void rejectsBlankName() {
		assertThatThrownBy(() -> Hero.create("  ", List.of("Flight"), 30, "male", Universe.MARVEL))
				.isInstanceOf(InvalidHeroException.class)
				.hasMessage("Hero name is required");
	}

	@Test
	void rejectsNegativeAge() {
		assertThatThrownBy(() -> Hero.create("Superman", List.of("Flight"), -1, "male", Universe.DC))
				.isInstanceOf(InvalidHeroException.class)
				.hasMessage("Hero age must not be negative");
	}

	@Test
	void rejectsNullUniverse() {
		assertThatThrownBy(() -> Hero.create("Superman", List.of("Flight"), 30, "male", null))
				.isInstanceOf(InvalidHeroException.class)
				.hasMessage("Hero universe is required");
	}

	@Test
	void rejectsBlankPower() {
		assertThatThrownBy(() -> Hero.create("Superman", List.of("Flight", " "), 30, "male", Universe.DC))
				.isInstanceOf(InvalidHeroException.class)
				.hasMessage("Power must not be blank");
	}

	@Test
	void keepsPowersImmutableAfterCreation() {
		final var powers = new ArrayList<>(List.of("Flight"));
		final var hero = Hero.create("Superman", powers, 30, "male", Universe.DC);

		powers.add("Heat vision");

		assertThat(hero.powers()).containsExactly("Flight");
		assertThatThrownBy(() -> hero.powers().add("Heat vision"))
				.isInstanceOf(UnsupportedOperationException.class);
	}
}
