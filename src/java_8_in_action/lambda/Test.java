package java_8_in_action.lambda;

import java.util.ArrayList;
import java.util.List;

import java_8_in_action.movement_parameter.Apple;
import java_8_in_action.movement_parameter.Predicate;

public class Test implements Predicate, AppleFormatter{
	@Override
	public boolean check(Apple apple) {
		return ("green".equals(apple.getColor()) && apple.getWeight() > 150);
	} 
	
	public static List<Apple> filterApples (List<Apple> inventory, Predicate p) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if (p.check(apple)) {
				result.add(apple);
			}
		}
		return result;
	}
	
	// Lambda
	List<Apple> inventory = new ArrayList<>();
	List<Apple> result = filterApples(inventory, (Apple apple) -> "red".equals(apple.getColor()));

	@Override
	public String accept(java_8_in_action.lambda.Apple apple) {
		String characteristic = apple.getWeight() > 150 ? "heavy" : "light";
		return "A " + characteristic + " " + apple.getColor() + " apple";
	}
	
	public static void main(String [] args) {
		Runnable r1 = () -> System.out.println("Hello Wolrd");
		
		process(r1);
		
		int portNumber = 3306;
		Runnable r = () -> {
			System.out.println(portNumber);
		};
		//portNumber = 13306; // 재정의 시 에러발생
		process(r);
	}
	
	public static void process (Runnable r) {
		r.run();
	}
	
}
