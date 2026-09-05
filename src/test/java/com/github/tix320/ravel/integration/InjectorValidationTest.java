package com.github.tix320.ravel.integration;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.api.scope.Singleton;
import com.github.tix320.ravel.internal.exception.CycleDependencyException;
import com.github.tix320.ravel.internal.exception.ModuleException;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class InjectorValidationTest {

	@Test
	public void nonFinalStaticModuleRejected() {
		Injector injector = new Injector();

		assertThrows(ModuleException.class, () -> injector.registerModule(NonFinalModule.class));
	}

	@Test
	public void beanCycleRejected() {
		Injector injector = new Injector();
		injector.registerModule(CycleModule.class);

		assertThrows(CycleDependencyException.class, injector::build);
	}

	@Test
	public void nullBeanFactoryResultRejected() {
		Injector injector = new Injector();
		injector.registerModule(NullBeanModule.class);
		injector.build();

		assertThrows(NullPointerException.class, () -> injector.inject(BeanA.class));
	}

	public static class NonFinalModule {

	}

	public static final class CycleModule {

		@Singleton
		public BeanA a(BeanB b) {
			return new BeanA(Objects.requireNonNull(b).text());
		}

		@Singleton
		public BeanB b(BeanA a) {
			return new BeanB(a.text());
		}
	}

	public static final class NullBeanModule {

		@Singleton
		public BeanA a() {
			return null;
		}
	}

	public record BeanA(String text) {

	}

	public record BeanB(String text) {

	}
}
