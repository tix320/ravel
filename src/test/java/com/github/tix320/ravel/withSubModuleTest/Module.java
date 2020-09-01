package com.github.tix320.ravel.withSubModuleTest;

import com.github.tix320.ravel.api.scope.Singleton;
import com.github.tix320.ravel.api.module.UseModules;

@UseModules(classes = {SubModule.class})
public class Module {

	@Singleton
	public A a(B b) {
		return new A(b);
	}
}
