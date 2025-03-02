package com.github.tix320.ravel.internal;

import com.github.tix320.ravel.api.bean.BeanDefinition;
import java.util.Collection;
import java.util.List;

/**
 * @author Tigran Sargsyan on 26-Aug-20
 */
public class StaticModuleDefinition extends ModuleDefinition {

	private final Object instance;

	public StaticModuleDefinition(String name, List<String> dependencies,
								  Collection<BeanDefinition> beanDefinitions, Object instance) {
		super(name, dependencies, beanDefinitions);
		this.instance = instance;
	}

	public Object getInstance() {
		return instance;
	}

}
