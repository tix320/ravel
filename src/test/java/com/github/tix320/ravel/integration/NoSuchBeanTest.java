package com.github.tix320.ravel.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.api.scope.Singleton;
import com.github.tix320.ravel.internal.exception.NoSuchBeanException;
import org.junit.jupiter.api.Test;

public class NoSuchBeanTest {

	@Test
	public void test() {
		assertThrows(NoSuchBeanException.class, () -> {
			Injector injector = new Injector();
			injector.registerModule(Module.class);
			injector.build();
		});
	}

	public static class Module {

		@Singleton
		public BeanA a(BeanB b) {
			return new BeanA(b);
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
