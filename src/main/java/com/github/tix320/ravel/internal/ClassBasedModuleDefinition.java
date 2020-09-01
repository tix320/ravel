package com.github.tix320.ravel.internal;

import java.util.List;
import java.util.Map;

import com.github.tix320.ravel.api.bean.BeanDefinition;
import com.github.tix320.ravel.api.bean.BeanKey;

/**
 * @author Tigran Sargsyan on 26-Aug-20
 */
public class ClassBasedModuleDefinition extends ModuleDefinition {

	private final Object instance;

	public ClassBasedModuleDefinition(String name, List<String> dependencies,
									  Map<BeanKey, BeanDefinition> beanDefinitions, Object instance) {
		super(name, dependencies, beanDefinitions);
		this.instance = instance;
	}

	public Object getInstance() {
		return instance;
	}
}
