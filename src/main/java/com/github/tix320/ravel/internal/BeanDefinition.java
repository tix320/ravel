package com.github.tix320.ravel.internal;

import java.util.List;

public abstract class BeanDefinition {

	private final Class<?> clazz;

	private final BeanFactory factoryMethod;

	private final List<BeanDefinition> dependencies;

	public BeanDefinition(Class<?> clazz, BeanFactory beanFactory, List<BeanDefinition> dependencies) {
		this.clazz = clazz;
		this.factoryMethod = beanFactory;
		this.dependencies = dependencies;
	}

	protected final Object createInstance() {
		Object[] args = new Object[dependencies.size()];

		for (int i = 0; i < dependencies.size(); i++) {
			BeanDefinition beanDefinition = dependencies.get(i);
			args[i] = beanDefinition.getInstance();
		}

		return factoryMethod.create(args);
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public abstract Object getInstance();
}
