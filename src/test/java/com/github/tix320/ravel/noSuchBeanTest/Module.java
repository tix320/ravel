package com.github.tix320.ravel.noSuchBeanTest;

import com.github.tix320.ravel.api.Bean;
import com.github.tix320.ravel.api.BeansModule;

public class Module implements BeansModule {

	@Bean
	public A a(B b) {
		return new A(b);
	}
}
