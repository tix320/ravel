package com.github.tix320.ravel.api.bean;

import com.github.tix320.ravel.api.scope.Scope;
import java.util.List;

/**
 * @author Tigran Sargsyan on 25-Aug-20
 */
public record BeanDefinition(BeanKey beanKey, Scope scope, List<BeanKey> dependencies, BeanFactory factory) {
}
