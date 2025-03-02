package com.github.tix320.ravel.internal;

import com.github.tix320.ravel.api.bean.BeanFactory;
import java.util.List;

public abstract class BaseBean {

	private final BeanFactory factoryMethod;

	private final List<BaseBean> dependencies;

	public BaseBean(BeanFactory beanFactory, List<BaseBean> dependencies) {
		this.factoryMethod = beanFactory;
		this.dependencies = dependencies;
	}

	protected final Object createInstance() {
		Object[] args = new Object[dependencies.size()];

		for (int i = 0; i < dependencies.size(); i++) {
			BaseBean baseBean = dependencies.get(i);
			args[i] = baseBean.getInstance();
		}

		return factoryMethod.create(args);
	}

	public abstract Object getInstance();

}
