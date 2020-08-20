package com.github.tix320.ravel.withSubModuleTest;

class A {

	private final B b;

	public A(B b) {
		this.b = b;
	}

	public String getText() {
		return b.getText();
	}
}
