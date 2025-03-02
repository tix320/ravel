package com.github.tix320.ravel.api.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequireModules {

	Class<?>[] statics() default {};

	String[] dynamics() default {};

}
