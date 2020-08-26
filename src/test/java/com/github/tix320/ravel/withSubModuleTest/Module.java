package com.github.tix320.ravel.withSubModuleTest;

import com.github.tix320.ravel.api.Bean;
import com.github.tix320.ravel.api.UseModules;

@UseModules(classes = {SubModule.class})
public class Module {

	@Bean
	public A a(B b) {
		return new A(b);
	}
}
