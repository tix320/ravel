package com.github.tix320.ravel.noSuchBeanTest;

import com.github.tix320.ravel.api.Bean;

public class Module {

	@Bean
	public A a(B b) {
		return new A(b);
	}
}
