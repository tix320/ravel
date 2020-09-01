package com.github.tix320.ravel.duplicateBeanTest;

import com.github.tix320.ravel.api.scope.Singleton;
import com.github.tix320.ravel.api.bean.Qualifier;

public class Module {

	@Singleton
	@Qualifier("auu")
	public B b1() {
		return new B();
	}

	@Singleton
	@Qualifier("auu")
	public B b2() {
		return new B();
	}
}
