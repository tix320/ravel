package com.github.tix320.ravel.internal;

import java.util.List;

import com.github.tix320.ravel.api.BeanFactory;

public final class PrototypeBean extends BaseBean {

	public PrototypeBean(BeanFactory beanFactory, List<BaseBean> dependencies) {
		super(beanFactory, dependencies);
	}

	@Override
	public Object getInstance() {
		return createInstance();
	}
}
