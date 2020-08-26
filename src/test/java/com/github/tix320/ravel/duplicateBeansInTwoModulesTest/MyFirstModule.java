package com.github.tix320.ravel.duplicateBeansInTwoModulesTest;

import com.github.tix320.ravel.api.Bean;

public class MyFirstModule {

	@Bean
	public B b() {
		return new B();
	}
}
