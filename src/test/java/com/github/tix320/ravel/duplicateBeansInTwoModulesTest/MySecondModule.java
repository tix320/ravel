package com.github.tix320.ravel.duplicateBeansInTwoModulesTest;

import com.github.tix320.ravel.api.Singleton;

public class MySecondModule {

	@Singleton
	public B b() {
		return new B();
	}
}
