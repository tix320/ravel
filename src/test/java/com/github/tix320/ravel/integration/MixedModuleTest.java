package com.github.tix320.ravel.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.tix320.ravel.api.Injector;
import com.github.tix320.ravel.api.bean.BeanDefinition;
import com.github.tix320.ravel.api.bean.BeanKey;
import com.github.tix320.ravel.api.module.DynamicModuleDefinition;
import com.github.tix320.ravel.api.module.RequireModules;
import com.github.tix320.ravel.api.scope.Scope;
import com.github.tix320.ravel.api.scope.Singleton;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MixedModuleTest {

	@Test
	void test() {

		var beanDefinition = new BeanDefinition(new BeanKey(BeanB.class), Scope.SINGLETON,
												List.of(), dependencies -> new BeanB());

		Injector injector = new Injector();
		injector.registerModule(Module.class);
		injector.registerDynamicModule(new DynamicModuleDefinition("dynamic", List.of(),
																   List.of(beanDefinition)));
		injector.build();
		BeanA beanA = injector.inject(BeanA.class);

		assertNotNull(beanA.getB());
	}

	@RequireModules(dynamics = "dynamic")
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

		private BeanB getB() {
			return b;
		}

	}

	public static class BeanB {
	}

}
