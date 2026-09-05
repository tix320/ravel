package com.github.tix320.ravel.api.bean;

import com.github.tix320.ravel.api.scope.Scope;
import java.util.List;
import java.util.Objects;

/**
 * @author Tigran Sargsyan on 25-Aug-20
 */
public record BeanDefinition(BeanKey beanKey, Scope scope, List<BeanKey> dependencies, BeanFactory factory) {

	public BeanDefinition {
		Objects.requireNonNull(beanKey);
		Objects.requireNonNull(scope);
		dependencies = List.copyOf(dependencies);
		Objects.requireNonNull(factory);
	}
}
