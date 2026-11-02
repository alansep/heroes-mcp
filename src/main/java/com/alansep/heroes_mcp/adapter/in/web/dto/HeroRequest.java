package com.alansep.heroes_mcp.adapter.in.web.dto;

import com.alansep.heroes_mcp.domain.model.Universe;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Payload to create or update a hero")
public record HeroRequest(
		@Schema(description = "Hero name", example = "Superman")
		String name,

		@Schema(description = "Hero powers", example = "[\"Flight\", \"Super strength\"]")
		List<String> powers,

		@Schema(description = "Hero age", example = "35")
		Integer age,

		@Schema(description = "Hero gender", example = "male")
		String gender,

		@Schema(description = "Hero universe", example = "DC")
		Universe universe
) {
}
