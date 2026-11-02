package com.alansep.heroes_mcp.adapter.in.web;

import com.alansep.heroes_mcp.adapter.in.web.dto.HeroRequest;
import com.alansep.heroes_mcp.adapter.in.web.dto.HeroResponse;
import com.alansep.heroes_mcp.application.port.in.HeroUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Heroes", description = "Superhero CRUD")
@RestController
@RequestMapping("/api/heroes")
public class HeroController {

	private final HeroUseCase heroUseCase;

	public HeroController(final HeroUseCase heroUseCase) {
		this.heroUseCase = heroUseCase;
	}

	@Operation(summary = "Create a hero")
	@PostMapping
	public ResponseEntity<HeroResponse> create(@RequestBody final HeroRequest request) {
		final var created = heroUseCase.create(HeroWebMapper.toCreateCommand(request));
		return ResponseEntity
				.created(URI.create("/api/heroes/" + created.id()))
				.body(HeroWebMapper.toResponse(created));
	}

	@Operation(summary = "List all heroes")
	@GetMapping
	public List<HeroResponse> findAll() {
		return heroUseCase.findAll().stream()
				.map(HeroWebMapper::toResponse)
				.toList();
	}

	@Operation(summary = "Find a hero by id")
	@GetMapping("/{id}")
	public HeroResponse findById(@PathVariable final UUID id) {
		return HeroWebMapper.toResponse(heroUseCase.findById(id));
	}

	@Operation(summary = "Update a hero")
	@PutMapping("/{id}")
	public HeroResponse update(@PathVariable final UUID id, @RequestBody final HeroRequest request) {
		return HeroWebMapper.toResponse(heroUseCase.update(id, HeroWebMapper.toUpdateCommand(request)));
	}

	@Operation(summary = "Delete a hero")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable final UUID id) {
		heroUseCase.delete(id);
		return ResponseEntity.noContent().build();
	}
}
