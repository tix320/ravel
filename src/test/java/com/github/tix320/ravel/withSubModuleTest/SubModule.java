package com.github.tix320.ravel.withSubModuleTest;

import com.github.tix320.ravel.api.Bean;

public class SubModule {

	@Bean
	public B b() {
		return new B();
	}
}
