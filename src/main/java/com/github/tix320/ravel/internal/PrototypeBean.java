package com.github.tix320.ravel.internal;

import java.util.List;

import com.github.tix320.ravel.api.BeansModule;

public final class PrototypeBean extends BeanDefinition {

	public PrototypeBean(Class<? extends BeansModule> ownModule, BeanFactory beanFactory,
						 List<BeanDefinition> dependencies) {
		super(ownModule, beanFactory, dependencies);
	}

	@Override
	public Object getInstance() {
		return createInstance();
	}
}
