package com.github.tix320.ravel.internal.exception;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class CycleDependencyException extends RavelException {

	public CycleDependencyException(String message, LinkedHashSet<Class<?>> classes) {
		super(message + constructErrorMessage(classes.stream().map(Class::getName).collect(Collectors.toList())));
	}

	public CycleDependencyException(String message, List<String> parts) {
		super(message + constructErrorMessage(parts));
	}

	private static String constructErrorMessage(Iterable<String> values) {
		StringJoiner stringJoiner = new StringJoiner(" -> ", "[", "]");

		String first = values.iterator().next();
		values.forEach(stringJoiner::add);
		stringJoiner.add(first);

		return stringJoiner.toString();
	}
}
