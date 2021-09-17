package java_8_in_action.default_method;

public class Test2 extends D implements A, B{
	public static void main(String[] args) {
		new Test2().hello();
	}
}

interface A {
	default void hello () {
		System.out.println("Hello from A");
	}
}

interface B extends A {
	default void hello () {
		System.out.println("Hello from B");
	}
}

class D implements A {
	public void hello () {
		System.out.println("Hello from D");
	}
}