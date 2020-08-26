package com.github.tix320.ravel.api;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author Tigran Sargsyan on 25-Aug-20
 */
public abstract class ModuleDefinition {

	private final String name;

	private final List<String> dependencies;

	private final Map<BeanKey, BeanDefinition> beanDefinitions;

	public ModuleDefinition(String name, List<String> dependencies, Map<BeanKey, BeanDefinition> beanDefinitions) {
		this.name = name;
		this.dependencies = dependencies;
		this.beanDefinitions = beanDefinitions;
	}

	public String getName() {
		return name;
	}

	public List<String> getDependencies() {
		return dependencies;
	}

	public Map<BeanKey, BeanDefinition> getBeanDefinitions() {
		return beanDefinitions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ModuleDefinition that = (ModuleDefinition) o;
		return name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
