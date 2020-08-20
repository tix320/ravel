package com.github.tix320.ravel.internal;

import java.util.List;

public final class ModuleDefinition {

	private final List<ModuleDefinition> dependencies;

	private final List<BeanDefinition> beanDefinitions;

	public ModuleDefinition(List<ModuleDefinition> dependencies, List<BeanDefinition> beanDefinitions) {
		this.dependencies = dependencies;
		this.beanDefinitions = beanDefinitions;
	}

	public List<ModuleDefinition> getDependencies() {
		return dependencies;
	}

	public List<BeanDefinition> getBeans() {
		return beanDefinitions;
	}
}
