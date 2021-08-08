package java_8_in_action.constructor_reference;

import java.util.function.Function;
import java.util.function.Supplier;

public class Test {
	public static void main(String [] args) {
		// Supplier : 매개변수를 받지 않고 단순히 무엇인가를 반환하는 추상 메서드

		// 람다표현식을 통한 객체 생성
		Supplier<Apple> c1 = () -> new Apple();
		Apple a1 = c1.get();
		
		// 생성자 레퍼런스를 통한 객체 생성
		Supplier<Apple> c2 = Apple::new;
		Apple a2 = c2.get(); 
		
		Function<Integer, Apple> f1 = Apple::new;
		
		Function<String, Apple> f2 = (color) -> new Apple(color);
		
		Apple a3 = f1.apply(110);
		
		Apple a4 = f2.apply("red");
		
		
		System.out.println("a3==="+a3);
		System.out.println("a4==="+a4);
	}
}
