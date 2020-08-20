package com.github.tix320.ravel.internal.exception;

import java.util.LinkedHashSet;
import java.util.StringJoiner;

public class CycleDependencyException extends RavelException {

	public CycleDependencyException(String message, LinkedHashSet<Class<?>> classes) {
		super(constructErrorMessage(classes));
	}

	private static String constructErrorMessage(LinkedHashSet<Class<?>> classes) {
		StringJoiner stringJoiner = new StringJoiner(" -> ", "[", "]");

		Class<?> first = classes.iterator().next();
		classes.forEach(aClass -> stringJoiner.add(aClass.getName()));
		stringJoiner.add(first.getName());

		return stringJoiner.toString();
	}
}
