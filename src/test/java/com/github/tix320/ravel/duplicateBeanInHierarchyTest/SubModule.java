package com.github.tix320.ravel.duplicateBeanInHierarchyTest;

import com.github.tix320.ravel.api.Bean;

public class SubModule {

	@Bean
	public B b() {
		return new B("Sub module");
	}
}
