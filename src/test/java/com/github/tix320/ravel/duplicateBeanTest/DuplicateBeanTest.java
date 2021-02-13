package com.github.tix320.ravel.duplicateBeanTest;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.internal.exception.DuplicateBeanException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DuplicateBeanTest {

	@Test
	public void test() {
		assertThrows(DuplicateBeanException.class, () -> {
			Injector injector = new Injector();
			injector.registerModule(Module.class);
			injector.build();
		});
	}
}
