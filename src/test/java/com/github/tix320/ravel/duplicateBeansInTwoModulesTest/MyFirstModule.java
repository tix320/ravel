package com.github.tix320.ravel.duplicateBeansInTwoModulesTest;

import com.github.tix320.ravel.api.Bean;
import com.github.tix320.ravel.api.BeansModule;

public class MyFirstModule implements BeansModule {

	@Bean
	public B b() {
		return new B();
	}
}
