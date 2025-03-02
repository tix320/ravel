package com.github.tix320.ravel.internal;

import com.github.tix320.ravel.api.bean.BeanFactory;
import java.util.List;

public final class SingletonBean extends BaseBean {

	private volatile Object instance;

	public SingletonBean(BeanFactory beanFactory, List<BaseBean> dependencies) {
		super(beanFactory, dependencies);
	}

	@Override
	public Object getInstance() {
		Object result = instance;
		if (result == null) {
			synchronized (this) {
				result = instance;
				if (result == null) {
					instance = result = createInstance();
				}
			}
		}
		return result;
	}

}
