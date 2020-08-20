package com.github.tix320.ravel.simpleTest;

import com.github.tix320.ravel.api.Injector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {

	@Test
	void test() {
		Injector injector = new Injector(Module.class);
		A inject = injector.inject(A.class);

		assertEquals("dummy", inject.getText());
	}
}
