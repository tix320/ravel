package com.github.tix320.ravel.simpleTest;

import com.github.tix320.ravel.api.Bean;

public class Module {

	@Bean
	public A a(B b) {
		return new A(b);
	}

	@Bean
	public B b() {
		return new B();
	}
}
