package com.github.tix320.ravel.noSuchBeanTest;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.internal.exception.NoSuchBeanException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NoSuchBeanTest {

	@Test
	void test() {
		assertThrows(NoSuchBeanException.class, () -> {
			Injector injector = new Injector(Module.class);
		});
	}
}
