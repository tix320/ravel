package com.github.tix320.ravel.withSubModuleTest;

import com.github.tix320.ravel.api.scope.Singleton;

public class SubModule {

	@Singleton
	public B b() {
		return new B();
	}
}
