package com.github.tix320.ravel.api.module;

import com.github.tix320.ravel.api.bean.BeanDefinition;
import com.github.tix320.ravel.internal.ModuleDefinition;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tigran Sargsyan on 26-Aug-20
 */
public final class DynamicModuleDefinition extends ModuleDefinition {

	public DynamicModuleDefinition(String name, List<String> dynamicDependencies, List<Class<?>> staticDependencies,
								   List<BeanDefinition> beanDefinitions) {
		super(name, Stream.concat(dynamicDependencies.stream(), staticDependencies.stream().map(Class::getName))
			.collect(Collectors.toList()), beanDefinitions);
	}

	public DynamicModuleDefinition(String name, List<String> dependencies,
								   List<BeanDefinition> beanDefinitions) {
		super(name, dependencies, beanDefinitions);
	}

}
