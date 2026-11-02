package com.alansep.heroes_mcp.config;

import com.alansep.heroes_mcp.application.port.in.HeroUseCase;
import com.alansep.heroes_mcp.application.port.out.HeroRepositoryPort;
import com.alansep.heroes_mcp.application.service.HeroService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires the application service (framework-free) into the Spring context.
 */
@Configuration
public class BeanConfiguration {

	@Bean
	public HeroUseCase heroUseCase(final HeroRepositoryPort repository) {
		return new HeroService(repository);
	}
}
