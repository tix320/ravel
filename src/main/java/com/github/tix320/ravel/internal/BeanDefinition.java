package com.github.tix320.ravel.internal;

import java.util.List;

import com.github.tix320.ravel.api.BeansModule;

public abstract class BeanDefinition {

	private final Class<? extends BeansModule> ownModule;

	private final BeanFactory factoryMethod;

	private final List<BeanDefinition> dependencies;

	public BeanDefinition(Class<? extends BeansModule> ownModule, BeanFactory beanFactory,
						  List<BeanDefinition> dependencies) {
		this.ownModule = ownModule;
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

	public Class<? extends BeansModule> getOwnModule() {
		return ownModule;
	}

	public abstract Object getInstance();
}
