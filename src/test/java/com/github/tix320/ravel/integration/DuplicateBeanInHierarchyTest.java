package com.github.tix320.ravel.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.api.module.RequireModules;
import com.github.tix320.ravel.api.scope.Singleton;
import com.github.tix320.ravel.internal.exception.DuplicateBeanException;
import org.junit.jupiter.api.Test;

public class DuplicateBeanInHierarchyTest {

	@Test
	public void test() {
		assertThrows(DuplicateBeanException.class, () -> {
			Injector injector = new Injector();
			injector.registerModule(ModuleA.class);
			injector.build();
		});
	}

	@RequireModules(statics = {ModuleB.class})
	public static class ModuleA {

		@Singleton
		public BeanA a(BeanB b) {
			return new BeanA(b);
		}

		@Singleton
		public BeanB b() {
			return new BeanB("dependent module");
		}

	}

	public static class ModuleB {

		@Singleton
		public BeanB b() {
			return new BeanB("core module");
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

		private final String s;

		public BeanB(String s) {
			this.s = s;
		}

		public String getText() {
			return s;
		}

	}

}
