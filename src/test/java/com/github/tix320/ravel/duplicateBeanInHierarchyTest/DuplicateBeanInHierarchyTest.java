package com.github.tix320.ravel.duplicateBeanInHierarchyTest;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.internal.exception.DuplicateBeanException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DuplicateBeanInHierarchyTest {

	@Test
	void test() {
		assertThrows(DuplicateBeanException.class, () -> {
			Injector injector = new Injector();
			injector.registerModule(Module.class);
			injector.build();
		});
	}
}
