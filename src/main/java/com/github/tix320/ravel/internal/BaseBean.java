package com.github.tix320.ravel.internal;

import com.github.tix320.ravel.api.bean.BeanFactory;
import java.util.List;
import java.util.Objects;

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

		return Objects.requireNonNull(factoryMethod.create(args), "Bean factory must not return null");
	}

	public abstract Object getInstance();

}
