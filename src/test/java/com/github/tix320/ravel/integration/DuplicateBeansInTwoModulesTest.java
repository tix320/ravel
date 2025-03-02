package com.github.tix320.ravel.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.api.module.RequireModules;
import com.github.tix320.ravel.api.scope.Singleton;
import com.github.tix320.ravel.internal.exception.DuplicateBeanException;
import org.junit.jupiter.api.Test;

public class DuplicateBeansInTwoModulesTest {

	@Test
	public void test() {
		assertThrows(DuplicateBeanException.class, () -> {
			Injector injector = new Injector();
			injector.registerModule(BaseModule.class);
			injector.build();
		});
	}

	@RequireModules(statics = {MyFirstModule.class, MySecondModule.class})
	public static class BaseModule {}

	public static class MyFirstModule {

		@Singleton
		public Bean b() {
			return new Bean();
		}

	}

	public static class MySecondModule {

		@Singleton
		public Bean b() {
			return new Bean();
		}

	}

	public static class Bean {

		public String getText() {
			return "dummy";
		}

	}

}
