package com.github.tix320.ravel.duplicateBeansInTwoModulesTest;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.internal.exception.DuplicateBeanException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DuplicateBeansInTwoModulesTest {

	@Test
	public void test() {
		assertThrows(DuplicateBeanException.class, () -> {
			Injector injector = new Injector();
			injector.registerModule(BaseModule.class);
			injector.build();
		});
	}
}
