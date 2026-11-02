package com.alansep.heroes_mcp.adapter.in.web.dto;

import com.alansep.heroes_mcp.domain.model.Universe;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Hero returned by the API")
public record HeroResponse(
		@Schema(description = "Hero identifier", example = "6f1d4a9a-3c4b-4b3e-9b0d-0f0a2b5c8d1e")
		UUID id,

		@Schema(description = "Hero name", example = "Superman")
		String name,

		@Schema(description = "Hero powers", example = "[\"Flight\", \"Super strength\"]")
		List<String> powers,

		@Schema(description = "Hero age", example = "35")
		int age,

		@Schema(description = "Hero gender", example = "male")
		String gender,

		@Schema(description = "Hero universe", example = "DC")
		Universe universe
) {
}
