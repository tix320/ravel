package com.github.tix320.ravel.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.api.bean.Qualifier;
import com.github.tix320.ravel.api.scope.Singleton;
import com.github.tix320.ravel.internal.exception.DuplicateBeanException;
import org.junit.jupiter.api.Test;

public class DuplicateBeanTest {

	@Test
	public void test() {
		assertThrows(DuplicateBeanException.class, () -> {
			Injector injector = new Injector();
			injector.registerModule(Module.class);
			injector.build();
		});
	}

	public static class Module {

		@Singleton
		@Qualifier("auu")
		public Bean b1() {
			return new Bean();
		}

		@Singleton
		@Qualifier("auu")
		public Bean b2() {
			return new Bean();
		}

	}

	public static class Bean {

		public String getText() {
			return "dummy";
		}

	}

}
