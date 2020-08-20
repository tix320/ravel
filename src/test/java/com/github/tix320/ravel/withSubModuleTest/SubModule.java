package com.github.tix320.ravel.withSubModuleTest;

import com.github.tix320.ravel.api.Bean;
import com.github.tix320.ravel.api.Module;

public class SubModule implements Module {

	@Bean
	public B b() {
		return new B();
	}
}
