package com.github.tix320.ravel.api.bean;

import java.util.Objects;

public class BeanKey {
	private final Class<?> type;
	private final String qualifier;

	public BeanKey(Class<?> type) {
		this(type, null);
	}

	public BeanKey(Class<?> type, String qualifier) {
		this.type = type;
		this.qualifier = qualifier;
	}

	public Class<?> getType() {
		return type;
	}

	public String getQualifier() {
		return qualifier;
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
