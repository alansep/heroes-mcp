package com.alansep.heroes_mcp.adapter.in.web;

import com.alansep.heroes_mcp.adapter.out.persistence.InMemoryHeroRepository;
import com.alansep.heroes_mcp.application.port.in.CreateHeroCommand;
import com.alansep.heroes_mcp.application.port.in.HeroUseCase;
import com.alansep.heroes_mcp.application.service.HeroService;
import com.alansep.heroes_mcp.domain.model.Hero;
import com.alansep.heroes_mcp.domain.model.Universe;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HeroControllerTest {

	private final HeroUseCase useCase = new HeroService(new InMemoryHeroRepository());
	private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new HeroController(useCase))
			.setControllerAdvice(new ApiExceptionHandler())
			.build();

	@Test
	void createsHero() throws Exception {
		mockMvc.perform(post("/api/heroes")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Superman",
								  "powers": ["Flight", "Super strength"],
								  "age": 35,
								  "gender": "male",
								  "universe": "DC"
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", matchesPattern("/api/heroes/[0-9a-f-]+")))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Superman"))
				.andExpect(jsonPath("$.powers[0]").value("Flight"))
				.andExpect(jsonPath("$.age").value(35))
				.andExpect(jsonPath("$.gender").value("male"))
				.andExpect(jsonPath("$.universe").value("DC"));
	}

	@Test
	void listsHeroes() throws Exception {
		createHero("Superman", Universe.DC);
		createHero("Batman", Universe.DC);

		mockMvc.perform(get("/api/heroes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].name").value("Batman"))
				.andExpect(jsonPath("$[1].name").value("Superman"));
	}

	@Test
	void findsHeroById() throws Exception {
		final var hero = createHero("Homelander", Universe.THE_BOYS);

		mockMvc.perform(get("/api/heroes/{id}", hero.id()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(hero.id().toString()))
				.andExpect(jsonPath("$.universe").value("THE_BOYS"));
	}

	@Test
	void returnsNotFoundWhenHeroDoesNotExist() throws Exception {
		mockMvc.perform(get("/api/heroes/{id}", UUID.randomUUID()))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.title").value("Hero not found"));
	}

	@Test
	void updatesHero() throws Exception {
		final var hero = createHero("Superman", Universe.DC);

		mockMvc.perform(put("/api/heroes/{id}", hero.id())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Homelander",
								  "powers": ["Flight", "Heat vision"],
								  "age": 40,
								  "gender": "male",
								  "universe": "THE_BOYS"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(hero.id().toString()))
				.andExpect(jsonPath("$.name").value("Homelander"))
				.andExpect(jsonPath("$.powers.length()").value(2))
				.andExpect(jsonPath("$.universe").value("THE_BOYS"));
	}

	@Test
	void returnsNotFoundWhenUpdatingUnknownHero() throws Exception {
		mockMvc.perform(put("/api/heroes/{id}", UUID.randomUUID())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Nobody",
								  "powers": [],
								  "age": 20,
								  "gender": "other",
								  "universe": "MARVEL"
								}
								"""))
				.andExpect(status().isNotFound());
	}

	@Test
	void deletesHero() throws Exception {
		final var hero = createHero("Superman", Universe.DC);

		mockMvc.perform(delete("/api/heroes/{id}", hero.id()))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/heroes/{id}", hero.id()))
				.andExpect(status().isNotFound());
	}

	@Test
	void rejectsInvalidHeroPayload() throws Exception {
		mockMvc.perform(post("/api/heroes")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": " ",
								  "powers": ["Flight"],
								  "age": 35,
								  "gender": "male",
								  "universe": "DC"
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.title").value("Invalid hero"))
				.andExpect(jsonPath("$.detail").value("Hero name is required"));
	}

	@Test
	void rejectsInvalidUniverse() throws Exception {
		mockMvc.perform(post("/api/heroes")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Superman",
								  "powers": ["Flight"],
								  "age": 35,
								  "gender": "male",
								  "universe": "IMAGE"
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.title").value("Invalid request body"));
	}

	@Test
	void rejectsMalformedId() throws Exception {
		mockMvc.perform(get("/api/heroes/not-a-uuid"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.title").value("Invalid parameter"));
	}

	private Hero createHero(final String name, final Universe universe) {
		return useCase.create(new CreateHeroCommand(name, List.of("Flight"), 35, "male", universe));
	}
}
