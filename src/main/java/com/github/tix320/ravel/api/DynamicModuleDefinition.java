package com.github.tix320.ravel.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tigran Sargsyan on 26-Aug-20
 */
public final class DynamicModuleDefinition extends ModuleDefinition {

	public DynamicModuleDefinition(String name, List<String> dynamicDependencies, List<Class<?>> classDependencies,
								   Map<BeanKey, BeanDefinition> beanDefinitions) {
		super(name, Stream.concat(dynamicDependencies.stream(), classDependencies.stream().map(Class::getName))
				.collect(Collectors.toList()), beanDefinitions);
	}

	public DynamicModuleDefinition(String name, List<String> dependencies,
								   Map<BeanKey, BeanDefinition> beanDefinitions) {
		super(name, dependencies, beanDefinitions);
	}
}
