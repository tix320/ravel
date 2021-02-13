package com.github.tix320.ravel.withSubModuleTest;

import com.github.tix320.ravel.api.Injector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WithSubModuleTest {

	@Test
	public void test() {
		Injector injector = new Injector();
		injector.registerModule(Module.class);
		injector.build();
		A inject = injector.inject(A.class);

		assertEquals("dummy", inject.getText());
	}
}
