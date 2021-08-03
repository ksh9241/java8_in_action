package java_8_in_action.stream_collector;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		List<Apple> inventory = new ArrayList<>();
		
		// 순차 처리 방식
		//List<Apple> heavyApples = inventory.stream().filter((Apple a) -> a.getWeight() > 150).collect(toList());
		
		// 병렬 처리 방식
		List<Apple> heavyApples =inventory.parallelStream().filter((Apple a) -> a.getWeight() > 150).collect(toList());
		
		System.out.println("heavyApples=="+heavyApples);
	}
}

class Apple {
	int weight;
	
	public int getWeight() {
		return this.weight;
	}
}
