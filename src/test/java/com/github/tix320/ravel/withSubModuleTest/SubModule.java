package com.github.tix320.ravel.withSubModuleTest;

import com.github.tix320.ravel.api.Bean;
import com.github.tix320.ravel.api.BeansModule;

public class SubModule implements BeansModule {

	@Bean
	public B b() {
		return new B();
	}
}
