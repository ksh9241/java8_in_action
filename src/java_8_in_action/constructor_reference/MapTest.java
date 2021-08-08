package java_8_in_action.constructor_reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MapTest {

	// 인자 생성자의 매개변수 값이 하나일 때는 Function 인터페이스 사용
	List<Integer> weights = Arrays.asList(7, 3, 4, 10);
	List<Apple> apples = map(weights, Apple::new);
	
	private static List<Apple> map(List<Integer> list, Function<Integer, Apple> f) {
		List<Apple> result = new ArrayList<>();
		
		for(Integer e : list) {
			result.add(f.apply(e));
		}
		return result;
	}
	
	// 인자 생성자의 매개변수 값이 복수 일 때는 BiFunction 인터페이스 사용
	BiFunction<String, Integer, Apple> bf = Apple::new; // 생성자 레퍼런스
	//BiFunction<String, Integer, Apple> bf2 = (String, Integer) -> new Apple(String, Integer); 람다로표현해봄.
	Apple a1 = bf.apply("red", 120);
	
	
	
	public static void main(String [] args) {
		MapTest a = new MapTest();
		
		//System.out.println("apples=="+a.apples);
		
		System.out.println(a.a1);
	}
}
