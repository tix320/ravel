package com.github.tix320.ravel.duplicateBeanInHierarchyTest;

import com.github.tix320.ravel.api.Bean;
import com.github.tix320.ravel.api.UseModules;

@UseModules(classes = {SubModule.class})
public class Module {

	@Bean
	public A a(B b) {
		return new A(b);
	}

	@Bean
	public B b() {
		return new B("Parent module");
	}
}
