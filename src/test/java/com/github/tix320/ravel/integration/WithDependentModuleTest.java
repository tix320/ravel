package com.github.tix320.ravel.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.api.module.RequireModules;
import com.github.tix320.ravel.api.scope.Singleton;
import org.junit.jupiter.api.Test;

public class WithDependentModuleTest {

	@Test
	public void test() {
		Injector injector = new Injector();
		injector.registerModule(ModuleA.class);
		injector.build();
		BeanA inject = injector.inject(BeanA.class);

		assertEquals("dummy", inject.getText());
	}

	@RequireModules(statics = {ModuleB.class})
	public static class ModuleA {

		@Singleton
		public BeanA a(BeanB b) {
			return new BeanA(b);
		}

	}

	public static class ModuleB {

		@Singleton
		public BeanB b() {
			return new BeanB();
		}

	}

	public static class BeanA {

		private final BeanB b;

		public BeanA(BeanB b) {
			this.b = b;
		}

		public String getText() {
			return b.getText();
		}

	}

	public static class BeanB {

		public String getText() {
			return "dummy";
		}

	}

}
