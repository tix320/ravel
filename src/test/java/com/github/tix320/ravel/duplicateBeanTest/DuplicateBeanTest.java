package com.github.tix320.ravel.duplicateBeanTest;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.internal.exception.DuplicateBeanException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DuplicateBeanTest {

	@Test
	void test() {
		assertThrows(DuplicateBeanException.class, () -> {
			Injector injector = new Injector(Module.class);
		});
	}
}
