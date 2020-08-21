package com.github.tix320.ravel.simpleTest;

import com.github.tix320.ravel.api.Bean;
import com.github.tix320.ravel.api.BeansModule;

public class Module implements BeansModule {

	@Bean
	public A a(B b) {
		return new A(b);
	}

	@Bean
	public B b() {
		return new B();
	}
}
