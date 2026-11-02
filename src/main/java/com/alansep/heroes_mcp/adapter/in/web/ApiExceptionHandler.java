package com.alansep.heroes_mcp.adapter.in.web;

import com.alansep.heroes_mcp.domain.exception.HeroNotFoundException;
import com.alansep.heroes_mcp.domain.exception.InvalidHeroException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(HeroNotFoundException.class)
	public ProblemDetail handleNotFound(final HeroNotFoundException exception) {
		return problem(HttpStatus.NOT_FOUND, "Hero not found", exception.getMessage());
	}

	@ExceptionHandler(InvalidHeroException.class)
	public ProblemDetail handleInvalidHero(final InvalidHeroException exception) {
		return problem(HttpStatus.BAD_REQUEST, "Invalid hero", exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ProblemDetail handleTypeMismatch(final MethodArgumentTypeMismatchException exception) {
		return problem(HttpStatus.BAD_REQUEST, "Invalid parameter", "Invalid value for '" + exception.getName() + "'");
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ProblemDetail handleUnreadableBody(final HttpMessageNotReadableException exception) {
		return problem(HttpStatus.BAD_REQUEST, "Invalid request body", "Request body is malformed or contains invalid values");
	}

	private static ProblemDetail problem(final HttpStatus status, final String title, final String detail) {
		final var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setTitle(title);
		return problemDetail;
	}
}
