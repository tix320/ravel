package com.github.tix320.ravel.noSuchBeanTest;

import com.github.tix320.ravel.api.Singleton;

public class Module {

	@Singleton
	public A a(B b) {
		return new A(b);
	}
}
