package java_8_in_action.stream_collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;

import java_8_in_action.stream_collector.Dish.Type;

public class MiddleOperatorExmaple {
	
	public static void main(String [] args) {
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
		
		List<String> streamExample = menu.stream()
									.filter(d -> d.getType() == Type.MEAT)
									.filter(d -> d.getCalories() > 400)
									.map(Dish::getName)
									.skip(1)
									.collect(toList());
		
		System.out.println(streamExample);
									
		
	}
}
