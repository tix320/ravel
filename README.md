# Ravel

Ravel is a small Java dependency injection library built around explicit modules and factory methods. It is intended for projects that want lightweight dependency wiring without classpath scanning, reflection-heavy configuration, or a large framework runtime.

## Installation

Maven:

```xml
<dependency>
    <groupId>com.github.tix320</groupId>
    <artifactId>ravel</artifactId>
    <version>1.1.0</version>
</dependency>
```

Gradle:

```gradle
dependencies {
    implementation 'com.github.tix320:ravel:1.1.0'
}
```

## Requirements

Ravel is built with Java 26. It is published as a regular jar and as a Java module:

```java
requires com.github.tix.ravel;
```

## Core Concepts

Ravel uses modules to declare beans. A bean is identified by:

- Java type
- Optional qualifier
- Scope
- Dependency list
- Factory function or factory method

Two scopes are supported:

- `@Singleton`: one instance is created and reused by the injector.
- `@Prototype`: a new instance is created each time the bean is needed.

## Static Modules

Static modules are final classes with public factory methods annotated with `@Singleton` or `@Prototype`.

```java
import com.github.tix320.ravel.api.module.RequireModules;
import com.github.tix320.ravel.api.scope.Singleton;

@RequireModules(statics = ModuleB.class)
public final class ModuleA {

    @Singleton
    public BeanA beanA(BeanB beanB) {
        return new BeanA(beanB);
    }
}

public final class ModuleB {

    @Singleton
    public BeanB beanB() {
        return new BeanB();
    }
}
```

Module classes must be final and must have a public no-argument constructor.

## Qualifiers

Use `@Qualifier` when multiple beans share the same type.

```java
import com.github.tix320.ravel.api.bean.Qualifier;
import com.github.tix320.ravel.api.scope.Singleton;

public class TextModule {

    @Singleton
    @Qualifier("primary")
    public String primaryText() {
        return "hello";
    }

    @Singleton
    public Message message(@Qualifier("primary") String text) {
        return new Message(text);
    }
}
```

## Dynamic Modules

Dynamic modules define beans programmatically.

```java
import com.github.tix320.ravel.api.bean.BeanDefinition;
import com.github.tix320.ravel.api.bean.BeanKey;
import com.github.tix320.ravel.api.module.DynamicModuleDefinition;
import com.github.tix320.ravel.api.scope.Scope;
import java.util.List;

var beanDefinition = new BeanDefinition(
    new BeanKey(BeanB.class),
    Scope.SINGLETON,
    List.of(),
    dependencies -> new BeanB()
);

var module = new DynamicModuleDefinition(
    "dynamic-module",
    List.of(),
    List.of(beanDefinition)
);
```

## Using The Injector

```java
import com.github.tix320.ravel.api.Injector;

Injector injector = new Injector();
injector.registerModule(ModuleA.class);
injector.registerDynamicModule(module);
injector.build();

BeanA beanA = injector.inject(BeanA.class);
String primaryText = injector.inject(String.class, "primary");
```

`build()` validates module dependencies, duplicate beans, missing bean dependencies, and dependency cycles. Modules cannot be registered after the injector is built.

## Limitations

Ravel intentionally keeps resolution simple:

- Beans are resolved by exact type and optional qualifier.
- Injection by interface or superclass is not supported unless that exact type is registered as a bean.
- Static modules are not inheritance-based. Ravel requires module classes to be final and scans only factory methods declared directly on the registered module class.
- Bean factory methods must be public.
- Bean factory methods and dynamic factories must not return `null`.
- Cyclic module or bean dependencies are rejected.

## Versioning

This project follows [Semantic Versioning](https://semver.org/).

## License

Ravel is released under the [Apache License, Version 2.0](LICENSE).

## Contact

- Author: [tix320](https://github.com/tix320)
