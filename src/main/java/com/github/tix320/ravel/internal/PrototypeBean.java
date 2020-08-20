package com.github.tix320.ravel.internal;

import java.util.List;

public final class PrototypeBean extends BeanDefinition {

	public PrototypeBean(Class<?> clazz, BeanFactory beanFactory, List<BeanDefinition> dependencies) {
		super(clazz, beanFactory, dependencies);
	}

	@Override
	public Object getInstance() {
		return createInstance();
	}
}
