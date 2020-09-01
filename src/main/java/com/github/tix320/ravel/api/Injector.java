package com.github.tix320.ravel.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.github.tix320.ravel.api.bean.BeanDefinition;
import com.github.tix320.ravel.api.bean.BeanKey;
import com.github.tix320.ravel.api.bean.Qualifier;
import com.github.tix320.ravel.api.module.DynamicModuleDefinition;
import com.github.tix320.ravel.api.module.UseModules;
import com.github.tix320.ravel.api.scope.Prototype;
import com.github.tix320.ravel.api.scope.Scope;
import com.github.tix320.ravel.api.scope.Singleton;
import com.github.tix320.ravel.internal.*;
import com.github.tix320.ravel.internal.exception.*;

public class Injector {

	private final String ROOT_MODULE_NAME = "ROOT_" + UUID.randomUUID().toString();

	private final Map<String, ModuleDefinition> modules;

	private final Map<BeanKey, BeanDefinition> beanDefinitions;

	private final Map<BeanKey, BaseBean> beans;

	private final AtomicBoolean built = new AtomicBoolean(false);

	public Injector() {
		modules = new HashMap<>();
		beanDefinitions = new HashMap<>();
		beans = new HashMap<>();
	}

	public void registerModule(Class<?> moduleClass) {
		if (built.get()) {
			throw new IllegalStateException("Already built");
		}

		String moduleName = moduleClass.getName();

		if (modules.containsKey(moduleName)) {
			throw new IllegalArgumentException(String.format("Module with name %s already registered", moduleName));
		}

		registerClassModule(moduleClass);
	}

	public void registerDynamicModule(DynamicModuleDefinition moduleDefinition) {
		if (built.get()) {
			throw new IllegalStateException("Already built");
		}

		String moduleName = moduleDefinition.getName();

		if (modules.containsKey(moduleName)) {
			throw new IllegalArgumentException(String.format("Module with name %s already registered", moduleName));
		}

		modules.put(moduleName, moduleDefinition);
	}

	public void build() {
		if (!built.compareAndSet(false, true)) {
			throw new IllegalStateException("Already built");
		}

		ModuleDefinition rootModule = new DynamicModuleDefinition(ROOT_MODULE_NAME, new ArrayList<>(modules.keySet()),
				Map.of());

		analyzeModule(rootModule);
	}

	public <T> T inject(Class<T> clazz) {
		return inject(clazz, null);
	}

	public <T> T inject(Class<T> clazz, String qualifier) {
		if (!built.get()) {
			throw new IllegalStateException("Not built yet");
		}
		BaseBean bean = beans.get(new BeanKey(clazz, qualifier));
		if (bean == null) {
			throw new NoSuchBeanException(String.format("No such bean with params %s", new BeanKey(clazz, qualifier)));
		}

		@SuppressWarnings("unchecked")
		T instance = (T) bean.getInstance();
		return instance;
	}

	private void registerClassModule(Class<?> moduleClass) {
		registerClassModule(moduleClass, new LinkedHashSet<>(), new HashSet<>());
	}

	private void registerClassModule(Class<?> moduleClass, LinkedHashSet<Class<?>> visitedModules,
									 Set<Class<?>> processesModules) {
		if (visitedModules.contains(moduleClass)) {
			throw new CycleDependencyException("Cycle module dependency found: ", visitedModules);
		}

		if (processesModules.contains(moduleClass)) {
			return;
		}

		visitedModules.add(moduleClass);

		String moduleName = moduleClass.getName();

		Object moduleInstance;
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

		Map<BeanKey, BeanDefinition> moduleBeanDefinitions = getModuleBeanDefinitions(moduleInstance);

		UseModules useModulesAnnotation = moduleClass.getAnnotation(UseModules.class);

		List<String> dependencies;
		if (useModulesAnnotation == null) {
			dependencies = List.of();
		}
		else {
			String[] dynamicDependencies = useModulesAnnotation.dynamic();
			Class<?>[] classDependencies = useModulesAnnotation.classes();

			dependencies = new ArrayList<>(dynamicDependencies.length + classDependencies.length);

			dependencies.addAll(Arrays.asList(dynamicDependencies));

			for (Class<?> classDependency : classDependencies) {
				registerClassModule(classDependency, visitedModules, processesModules);
				dependencies.add(classDependency.getName());
			}
		}

		ClassBasedModuleDefinition classBasedModuleDefinition = new ClassBasedModuleDefinition(moduleName, dependencies,
				moduleBeanDefinitions, moduleInstance);

		modules.put(moduleName, classBasedModuleDefinition);
		visitedModules.remove(moduleClass);
		processesModules.add(moduleClass);
	}

	private void analyzeModule(ModuleDefinition moduleDefinition) {
		analyzeModule(moduleDefinition,
				new RecursionContext(new LinkedHashSet<>(), new ArrayList<>(), new HashSet<>()));
	}

	private void analyzeModule(ModuleDefinition moduleDefinition, RecursionContext recursionContext) {
		LinkedHashSet<ModuleDefinition> visitedModules = recursionContext.visitedModules;
		List<ModuleDefinition> parentModules = recursionContext.parentModules;
		Set<ModuleDefinition> processedModules = recursionContext.processedModules;

		if (visitedModules.contains(moduleDefinition)) {
			List<String> parts = visitedModules.stream()
					.map(moduleDefinition1 -> moduleDefinition.getName())
					.collect(Collectors.toList());
			throw new CycleDependencyException("Cycle module dependency found: ", parts);
		}

		if (processedModules.contains(moduleDefinition)) {
			return;
		}

		visitedModules.add(moduleDefinition);

		for (ModuleDefinition parentModule : parentModules) {
			Set<BeanKey> parentBeanKeys = parentModule.getBeanDefinitions().keySet();
			Set<BeanKey> currentModuleBeanKeys = moduleDefinition.getBeanDefinitions().keySet();
			Set<BeanKey> beanInteractions = getSetsInteraction(parentBeanKeys, currentModuleBeanKeys);
			if (!beanInteractions.isEmpty()) { // TODO error with all interactions
				BeanKey anyKey = beanInteractions.iterator().next();

				throw new DuplicateBeanException(
						String.format("Duplicated beans with same type and qualifier %s in different modules [%s, %s]",
								anyKey, moduleDefinition.getName(), parentModule.getName()));
			}
		}

		List<String> dependencies = moduleDefinition.getDependencies();

		for (String moduleName : dependencies) {
			ModuleDefinition dependentModule = modules.get(moduleName);
			if (dependentModule == null) {
				throw new ModuleException(
						String.format("Module with name %s not registered, which need to module %s", moduleName,
								moduleDefinition.getName()));
			}
			else {
				parentModules.add(moduleDefinition);
				analyzeModule(dependentModule, recursionContext);
				parentModules.remove(moduleDefinition);
			}
		}

		Set<BeanKey> alreadyExistingBeanDefinitions = this.beanDefinitions.keySet();
		Set<BeanKey> definitionsToAdd = moduleDefinition.getBeanDefinitions().keySet();
		Set<BeanKey> interactions = getSetsInteraction(alreadyExistingBeanDefinitions, definitionsToAdd);
		if (!interactions.isEmpty()) { // TODO error with all interactions
			BeanKey anyKey = interactions.iterator().next();
			throw new DuplicateBeanException(
					String.format("Duplicated beans with same type and qualifier %s in different modules [%s, %s]",
							anyKey, moduleDefinition.getName(), "Unknown"));
		}

		this.beanDefinitions.putAll(moduleDefinition.getBeanDefinitions());
		for (BeanDefinition beanDefinition : moduleDefinition.getBeanDefinitions().values()) {
			analyzeBean(beanDefinition);
		}

		visitedModules.remove(moduleDefinition);

		processedModules.add(moduleDefinition);
	}

	private BaseBean analyzeBean(BeanDefinition beanDefinition) {
		BeanKey beanKey = beanDefinition.getBeanKey();

		BaseBean baseBean = beans.get(beanKey);
		if (baseBean != null) {
			return baseBean;
		}

		List<BeanKey> dependencies = beanDefinition.getDependencies();
		List<BaseBean> dependencyBeans = new ArrayList<>(dependencies.size());
		for (BeanKey dependentBeanKey : dependencies) {
			Class<?> type = dependentBeanKey.getType();

			BeanDefinition dependentBeanDefinition = beanDefinitions.get(dependentBeanKey);
			if (dependentBeanDefinition == null) {
				throw new NoSuchBeanException(
						String.format("Cannot construct bean %s because of missing dependency %s", beanKey.getType(),
								type));

			}

			BaseBean dependentBean = beans.get(dependentBeanKey);

			if (dependentBean == null) {
				dependentBean = analyzeBean(dependentBeanDefinition);
			}

			dependencyBeans.add(dependentBean);
		}


		Scope scope = beanDefinition.getScope();
		BaseBean bean;
		if (scope == Scope.PROTOTYPE) {
			bean = new PrototypeBean(beanDefinition.getFactory(), dependencyBeans);
		}
		else {
			bean = new SingletonBean(beanDefinition.getFactory(), dependencyBeans);
		}

		beans.put(beanKey, bean);

		return bean;
	}

	private Map<BeanKey, BeanDefinition> getModuleBeanDefinitions(Object moduleInstance) {
		Class<?> moduleClass = moduleInstance.getClass();
		Method[] methods = moduleClass.getDeclaredMethods();

		Map<BeanKey, BeanDefinition> beanDefinitions = new HashMap<>();

		for (Method method : methods) {
			Singleton singletonAnnotation = method.getAnnotation(Singleton.class);
			Prototype prototypeAnnotation = method.getAnnotation(Prototype.class);
			if (singletonAnnotation != null && prototypeAnnotation != null) {
				throw new ModuleException(
						String.format("Both @%s and @%s annotations declared in method %s (%s)", Singleton.class,
								Prototype.class, method.getName(), moduleClass));
			}

			if (singletonAnnotation != null || prototypeAnnotation != null) {
				Scope scope;
				if (singletonAnnotation != null) {
					scope = Scope.SINGLETON;
				}
				else {
					scope = Scope.PROTOTYPE;
				}

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

				if (beanDefinitions.containsKey(beanKey)) {
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

				beanDefinitions.put(beanKey, new BeanDefinition(beanKey, scope, dependencies, deps -> {
					try {
						return method.invoke(moduleInstance, deps);
					}
					catch (IllegalAccessException e) {
						throw new BeanCreationException("Cannot construct bean", e);
					}
					catch (InvocationTargetException e) {
						throw new BeanCreationException("Cannot construct bean", e.getTargetException());
					}
				}));
			}
		}

		return beanDefinitions;
	}

	private static <T> Set<T> getSetsInteraction(Set<T> set1, Set<T> set2) {
		Set<T> intersectSet = new HashSet<>(set1);
		intersectSet.retainAll(set2);
		return intersectSet;
	}


	private static final class RecursionContext {
		private final LinkedHashSet<ModuleDefinition> visitedModules;
		private final List<ModuleDefinition> parentModules;
		private final Set<ModuleDefinition> processedModules;

		private RecursionContext(LinkedHashSet<ModuleDefinition> visitedModules, List<ModuleDefinition> parentModules,
								 Set<ModuleDefinition> processedModules) {
			this.visitedModules = visitedModules;
			this.parentModules = parentModules;
			this.processedModules = processedModules;
		}
	}
}
