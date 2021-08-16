package java_8_in_action.stream_collector;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Reduce {

	public static void main(String[] args) {
		List<Integer> numbers = Arrays.asList(3, 2, 5, 9, 7);
		
		// forEach로 numbers합 구하기
		int sum = 0;
		for (int i : numbers) {
			sum += i;
		}
		
		System.out.println(sum);
		
		// Reduce로 numbers합 구하기
		int sum2 = numbers.stream().reduce(0, (a, b) -> a + b);
		System.out.println(sum2);
		
		// 내장함수를 이용하여 처리하기
		int sum3 = numbers.stream().reduce(0, Integer::sum);
		System.out.println(sum3);
		
		// Reduce Excample
		int product = numbers.stream().reduce(1, (a, b) -> a * b);
		System.out.println(product);
		
		// 초깃값 없는 Reduce
		Optional<Integer> op = numbers.stream().reduce(Integer::sum);
		int sum4 = op.get();
		System.out.println(sum4);
		
		// 최대값과 최소값 찾기
		Optional<Integer> maxOp = numbers.stream().reduce(Integer::max);
		int max = maxOp.get();
		System.out.println(max);
		
		Optional<Integer> minOp = numbers.stream().reduce(Integer::min);
		int min = minOp.get();
		System.out.println(min);
		
		int max2 = numbers.stream().reduce(0, (a, b) -> {return a < b ? b:a;});
		System.out.println(max2);
		
		int min2 = numbers.stream().reduce(100, (a, b) -> {return a < b ? a:b;});
		System.out.println(min2);
		
		System.out.println();
		
		List<Dish> menu = Arrays.asList(
				new Dish("pork", false, 800, Dish.Type.MEAT),
				new Dish("beef", false, 700, Dish.Type.MEAT),
				new Dish("chicken", false, 400, Dish.Type.MEAT),
				new Dish("french", false, 530, Dish.Type.OTHER),
				new Dish("rice", true, 350, Dish.Type.OTHER),
				new Dish("season fruit", true, 120, Dish.Type.OTHER),
				new Dish("pizza", true, 550, Dish.Type.OTHER),
				new Dish("prawns", false, 300, Dish.Type.FISH),
				new Dish("salmon", false, 450, Dish.Type.FISH)
				);
		
		// 문제1. map과 reduce를 이용하여 요리 개수를 계산하시오
		int dishCount = menu.stream().map(d -> 1).reduce(0, (a, b) -> a + b);
		System.out.println(dishCount);
		
		long dishCount2 = menu.stream().count();
		System.out.println(dishCount2);
	}
}
