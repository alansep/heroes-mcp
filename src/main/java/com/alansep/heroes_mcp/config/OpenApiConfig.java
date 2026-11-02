package com.alansep.heroes_mcp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI heroesOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("Heroes API")
						.description("Superhero CRUD (DC, Marvel, and The Boys)")
						.version("v1"));
	}
}
