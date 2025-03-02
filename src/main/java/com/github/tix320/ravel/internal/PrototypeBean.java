package com.github.tix320.ravel.internal;

import com.github.tix320.ravel.api.bean.BeanFactory;
import java.util.List;

public final class PrototypeBean extends BaseBean {

	public PrototypeBean(BeanFactory beanFactory, List<BaseBean> dependencies) {
		super(beanFactory, dependencies);
	}

	@Override
	public Object getInstance() {
		return createInstance();
	}

}
