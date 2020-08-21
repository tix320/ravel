package com.github.tix320.ravel.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

import com.github.tix320.ravel.internal.*;
import com.github.tix320.ravel.internal.exception.*;

public class Injector {

	private final Map<Class<?>, ModuleDefinition> modules;

	private final Map<BeanKey, BeanDefinition> beans;

	public Injector(Class<? extends BeansModule> configurationClass) {
		modules = new HashMap<>();
		beans = new HashMap<>();
		analyzeModule(configurationClass, new LinkedHashSet<>(), new HashMap<>(), null);
	}

	public <T> T inject(Class<T> clazz) {
		return inject(clazz, null);
	}

	public <T> T inject(Class<T> clazz, String qualifier) {
		BeanDefinition bean = beans.get(new BeanKey(clazz, qualifier));
		if (bean == null) {
			throw new NoSuchBeanException(String.format("No such bean with params %s", new BeanKey(clazz, qualifier)));
		}

		@SuppressWarnings("unchecked")
		T instance = (T) bean.getInstance();
		return instance;
	}

	private ModuleDefinition analyzeModule(Class<? extends BeansModule> moduleClass,
										   LinkedHashSet<Class<?>> visitedModules,
										   Map<BeanKey, BeanMethod> parentModuleBeanMethods,
										   Class<?> parentModuleClass) {
		if (visitedModules.contains(moduleClass)) {
			throw new CycleDependencyException("Cycle module dependency found: ", visitedModules);
		}

		ModuleDefinition definition = modules.get(moduleClass);
		if (definition != null) {
			return definition;
		}

		visitedModules.add(moduleClass);

		BeansModule moduleInstance;
		try {
			moduleInstance = moduleClass.getConstructor().newInstance();
		}
		catch (NoSuchMethodException e) {
			throw new ModuleException(
					String.format("Public No-arg constructor required to create instance of %s", moduleClass));
		}
		catch (IllegalAccessException | InstantiationException e) {
			throw new ModuleException("Cannot create module instance", e);

		}
		catch (InvocationTargetException e) {
			throw new ModuleException("Cannot create module instance", e.getTargetException());
		}

		Map<BeanKey, BeanMethod> beanMethods = getModuleBeanMethods(moduleInstance);
		// TODO we need change fail logic to override beans , which comes from parent module
		for (BeanKey beanKey : beanMethods.keySet()) {
			if (parentModuleBeanMethods.containsKey(beanKey)) {
				throw new DuplicateBeanException(
						String.format("Duplicated beans with same type and qualifier %s in different modules [%s, %s]",
								beanKey, moduleClass.getName(), parentModuleClass.getName()));
			}
		}
		///////////////////////////////////////////////////////////

		UseModule useModule = moduleClass.getAnnotation(UseModule.class);

		List<ModuleDefinition> dependentModules;
		if (useModule == null) {
			dependentModules = List.of();
		}
		else {
			dependentModules = new ArrayList<>();
			Class<? extends BeansModule>[] dependentConfigurationClasses = useModule.value();
			for (Class<? extends BeansModule> dependentModule : dependentConfigurationClasses) {
				ModuleDefinition conf = analyzeModule(dependentModule, visitedModules, beanMethods, moduleClass);
				dependentModules.add(conf);
			}
		}


		List<BeanDefinition> beanDefinitions = new ArrayList<>();
		for (BeanMethod beanMethod : beanMethods.values()) {
			BeanDefinition beanDefinition = analyzeBeanMethod(beanMethod, beanMethods);
			beanDefinitions.add(beanDefinition);
		}

		visitedModules.remove(moduleClass);

		ModuleDefinition moduleDefinition = new ModuleDefinition(dependentModules, beanDefinitions);

		modules.put(moduleClass, moduleDefinition);

		return moduleDefinition;
	}

	private BeanDefinition analyzeBeanMethod(BeanMethod beanMethod, Map<BeanKey, BeanMethod> beanMethods) {
		return analyzeBeanMethod(beanMethod, beanMethods, new LinkedHashSet<>());
	}

	private BeanDefinition analyzeBeanMethod(BeanMethod beanMethod, Map<BeanKey, BeanMethod> beanMethods,
											 LinkedHashSet<Class<?>> visitedClasses) {
		Class<?> clazz = beanMethod.beanKey.type;
		String qualifier = beanMethod.beanKey.qualifier;
		BeanKey beanKey = new BeanKey(clazz, qualifier);

		if (visitedClasses.contains(clazz)) {
			throw new CycleDependencyException("Dependency cycle found: ", visitedClasses);
		}

		BeanDefinition definition = beans.get(beanKey);
		if (definition != null) {
			if (beanMethod.ownModule != definition.getOwnModule()) {
				throw new DuplicateBeanException(
						String.format("Duplicated beans with same type and qualifier %s in different modules [%s, %s]",
								beanKey, beanMethod.ownModule, definition.getOwnModule()));
			}
			return definition;
		}

		visitedClasses.add(clazz);

		List<BeanKey> dependencies = beanMethod.dependencies;
		List<BeanDefinition> dependencyBeans = new ArrayList<>(dependencies.size());
		for (BeanKey dependentBeanKey : dependencies) {
			Class<?> type = dependentBeanKey.type;

			BeanDefinition beanDefinition = beans.get(dependentBeanKey);

			if (beanDefinition == null) {
				BeanMethod dependentBeanMethod = beanMethods.get(dependentBeanKey);
				if (dependentBeanMethod == null) {
					throw new NoSuchBeanException(
							String.format("Cannot construct bean %s because of missing dependency %s", clazz, type));
				}

				beanDefinition = analyzeBeanMethod(dependentBeanMethod, beanMethods, visitedClasses);
			}

			dependencyBeans.add(beanDefinition);
		}

		visitedClasses.remove(clazz);


		Scope scope = beanMethod.scope;
		BeanDefinition bean;
		if (scope == Scope.PROTOTYPE) {
			bean = new PrototypeBean(beanMethod.ownModule, beanMethod.beanFactory, dependencyBeans);
		}
		else {
			bean = new SingletonBean(beanMethod.ownModule, beanMethod.beanFactory, dependencyBeans);
		}

		beans.put(beanMethod.beanKey, bean);

		return bean;
	}

	private static class BeanMethod {

		private final Class<? extends BeansModule> ownModule;
		private final BeanKey beanKey;
		private final Scope scope;
		private final List<BeanKey> dependencies;
		private final BeanFactory beanFactory;

		private BeanMethod(Class<? extends BeansModule> ownModule, BeanKey beanKey, Scope scope,
						   List<BeanKey> dependencies, BeanFactory beanFactory) {
			this.ownModule = ownModule;
			this.beanKey = beanKey;
			this.scope = scope;
			this.dependencies = dependencies;
			this.beanFactory = beanFactory;
		}
	}

	private Map<BeanKey, BeanMethod> getModuleBeanMethods(BeansModule module) {
		Class<? extends BeansModule> moduleClass = module.getClass();
		Method[] methods = moduleClass.getDeclaredMethods();

		Map<BeanKey, BeanMethod> beanMethods = new HashMap<>();

		for (Method method : methods) {
			Bean annotation = method.getAnnotation(Bean.class);
			if (annotation != null) {
				boolean isPublic = Modifier.isPublic(method.getModifiers());
				if (!isPublic) {
					throw new ModuleException(
							String.format("Bean factory method must be public.\nMethod %s in module %s",
									method.getName(), moduleClass.getName()));
				}

				Qualifier qualifierAnnotation = method.getAnnotation(Qualifier.class);
				String qualifier = qualifierAnnotation == null ? null : qualifierAnnotation.value();
				Class<?> beanType = method.getReturnType();
				BeanKey beanKey = new BeanKey(beanType, qualifier);

				if (beanMethods.containsKey(beanKey)) {
					throw new DuplicateBeanException(
							String.format("Duplicated beans with same type and qualifier %s in module %s", beanKey,
									moduleClass.getName()));
				}

				List<BeanKey> dependencies = new ArrayList<>();
				for (Parameter parameter : method.getParameters()) {
					Class<?> type = parameter.getType();
					Qualifier paramQualifierAnnotation = parameter.getAnnotation(Qualifier.class);
					String paramQualifier = paramQualifierAnnotation == null ? null : paramQualifierAnnotation.value();

					dependencies.add(new BeanKey(type, paramQualifier));
				}


				BeanFactory beanFactory = dependencies1 -> {
					try {
						return method.invoke(module, dependencies1);
					}
					catch (IllegalAccessException e) {
						throw new BeanCreationException(String.format("Cannot create bean of %s", beanType), e);
					}
					catch (InvocationTargetException e) {
						throw new BeanCreationException(String.format("Cannot create bean of %s", beanType),
								e.getTargetException());
					}
				};

				beanMethods.put(beanKey,
						new BeanMethod(moduleClass, beanKey, annotation.scope(), dependencies, beanFactory));
			}
		}

		return beanMethods;
	}

	private static class BeanKey {
		private final Class<?> type;
		private final String qualifier;

		private BeanKey(Class<?> type, String qualifier) {
			this.type = type;
			this.qualifier = qualifier;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			BeanKey beanKey = (BeanKey) o;
			return type.equals(beanKey.type) && Objects.equals(qualifier, beanKey.qualifier);
		}

		@Override
		public int hashCode() {
			return Objects.hash(type, qualifier);
		}

		@Override
		public String toString() {
			return "[type=" + type + ", qualifier=" + qualifier + "]";
		}
	}
}
