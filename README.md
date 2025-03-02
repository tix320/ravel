# Ravel - Java Dependency Injection Library

A lightweight and efficient Dependency Injection (DI) library for Java applications, designed for simplicity and flexibility.

## Features

- **Module-Based Dependency Management (Static and Dynamic Modules)**

## Limitations

This library is designed to be minimalistic and does not support the following features:

- **Supertype Injections**: Beans must be explicitly defined with their concrete types; injection by supertype or interface is not supported.
- **Cyclic Dependencies**: The library does not support cyclic dependencies. If a cycle is detected, an exception is thrown with no way to resolve it.

## Installation

Add the following dependency to your `pom.xml` if using Maven:

```xml
<dependency>
    <groupId>com.github.tix320</groupId>
    <artifactId>ravel</artifactId>
    <version>0.4.0</version>
</dependency>
```

For Gradle:

```gradle
dependencies {
    implementation 'com.github.tix320:ravel:0.4.0'
}
```

## Architecture Overview

The library is structured around **modules**, which can be either **dynamic** or **static**. Each module contains a list of **bean definitions**.

### **Bean Definition Components**

- **Bean Key**: A combination of the bean type and its qualifier.
- **Scope**: Either `SINGLETON` (one instance per injector) or `PROTOTYPE` (a new instance per injection point).
- **Dependencies**: Other beans, represented by their **Bean Keys**.
- **Factory Method**: A function that accepts dependencies and creates an instance of the bean.

### **Types of Modules**

1. **Static Modules**: Defined declaratively by a class with annotated factory methods.
2. **Dynamic Modules**: Built programmatically, allowing logic-driven bean definitions rather than declarative configurations.

Static and dynamic modules can be used together within the same injector.

## Quick Start

### **Defining a Static Module with Annotations**

```java
@RequireModules(statics = {ModuleB.class})
public class ModuleA {
    
    @Singleton
    public BeanA a(BeanB b) {
        return new BeanA(b);
    }
}

public class ModuleB {
    
    @Singleton
    public BeanB b() {
        return new BeanB();
    }
}
```

### **Defining a Dynamic Module**

```java
var beanDefinition = new BeanDefinition(
    new BeanKey(BeanB.class), Scope.SINGLETON,
    List.of(), dependencies -> new BeanB()
);

DynamicModuleDefinition dynamicModule = new DynamicModuleDefinition(
    "dynamic", List.of(), List.of(beanDefinition)
);
```

### **Using Static and Dynamic Modules Together**

```java
Injector injector = new Injector();
injector.registerModule(ModuleA.class);
injector.registerDynamicModule(dynamicModule);
injector.build();

BeanA beanA = injector.inject(BeanA.class);
```

## Versioning

This project follows [Semantic Versioning](https://semver.org/).

## License

**Ravel** is released under the [Apache License, Version 2.0](LICENSE).

## Contact

- **Author**: [tix320](https://github.com/tix320)