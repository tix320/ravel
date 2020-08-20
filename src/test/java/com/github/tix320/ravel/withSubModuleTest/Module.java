package com.github.tix320.ravel.withSubModuleTest;

import com.github.tix320.ravel.api.Bean;
import com.github.tix320.ravel.api.UseModule;

@UseModule({SubModule.class})
public class Module implements com.github.tix320.ravel.api.Module {

	@Bean
	public A a(B b) {
		return new A(b);
	}
}
