package java_8_in_action.stream_collector;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NumberStream {
	public static void main(String[] args) {
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
		
		// 숫자 스트림으로 매핑
		int calories = menu.stream().mapToInt(Dish::getCalories).sum();
		System.out.println(calories);
		
		// 객체 스트림으로 복원하기
		IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
		Stream<Integer> stream = intStream.boxed();
		
		OptionalInt maxCalories = menu.stream().mapToInt(Dish::getCalories).max();
		int maxCalori = maxCalories.orElse(1); // 값이 없으면 1로 대체
		System.out.println(maxCalori);
	}
}
