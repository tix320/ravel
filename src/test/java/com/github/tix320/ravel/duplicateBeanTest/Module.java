package com.github.tix320.ravel.duplicateBeanTest;

import com.github.tix320.ravel.api.Bean;
import com.github.tix320.ravel.api.Qualifier;

public class Module implements com.github.tix320.ravel.api.Module {

	@Bean
	@Qualifier("auu")
	public B b1() {
		return new B();
	}

	@Bean
	@Qualifier("auu")
	public B b2() {
		return new B();
	}
}
