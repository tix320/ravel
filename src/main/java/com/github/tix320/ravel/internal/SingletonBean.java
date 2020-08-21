package com.github.tix320.ravel.internal;

import java.util.List;

import com.github.tix320.ravel.api.BeansModule;

public final class SingletonBean extends BeanDefinition {

	private volatile Object instance;

	public SingletonBean(Class<? extends BeansModule> ownModule, BeanFactory beanFactory,
						 List<BeanDefinition> dependencies) {
		super(ownModule, beanFactory, dependencies);
	}

	@Override
	public Object getInstance() {
		Object result = instance;
		if (result == null) {
			synchronized (this) {
				result = instance;
				if (result == null)
					instance = result = createInstance();
			}
		}
		return result;
	}
}
