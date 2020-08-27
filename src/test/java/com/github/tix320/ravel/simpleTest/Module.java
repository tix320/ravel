package com.github.tix320.ravel.simpleTest;

import com.github.tix320.ravel.api.Singleton;

public class Module {

	@Singleton
	public A a(B b) {
		return new A(b);
	}

	@Singleton
	public B b() {
		return new B();
	}
}
