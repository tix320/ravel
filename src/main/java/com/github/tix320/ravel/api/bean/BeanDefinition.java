package com.github.tix320.ravel.api.bean;

import java.util.List;

import com.github.tix320.ravel.api.scope.Scope;

/**
 * @author Tigran Sargsyan on 25-Aug-20
 */
public final class BeanDefinition {

	private final BeanKey beanKey;

	private final Scope scope;

	private final List<BeanKey> dependencies;

	private final BeanFactory factory;

	public BeanDefinition(BeanKey beanKey, Scope scope, List<BeanKey> dependencies, BeanFactory factory) {
		this.beanKey = beanKey;
		this.scope = scope;
		this.dependencies = dependencies;
		this.factory = factory;
	}

	public BeanKey getBeanKey() {
		return beanKey;
	}

	public Scope getScope() {
		return scope;
	}

	public List<BeanKey> getDependencies() {
		return dependencies;
	}

	public BeanFactory getFactory() {
		return factory;
	}
}
