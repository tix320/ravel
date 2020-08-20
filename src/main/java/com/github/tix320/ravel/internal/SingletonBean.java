package com.github.tix320.ravel.internal;

import java.util.List;

public final class SingletonBean extends BeanDefinition {

	private volatile Object instance;

	public SingletonBean(Class<?> clazz, BeanFactory beanFactory, List<BeanDefinition> dependencies) {
		super(clazz, beanFactory, dependencies);
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
