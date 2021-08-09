package java_8_in_action.stream_collector;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Example2 {
	public static void main (String [] args) {
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
		
		List<String> names = menu.stream()
								.filter(d -> {
									System.out.println("filtering : "+d.getName());
									return d.getCalories() > 300;
								})
								.map(d -> {
									System.out.println("mapping : "+d.getName());
									return d.getName();
								})
								.limit(3)
								.collect(Collectors.toList());
		
		System.out.println(names);
								
		
	}
}
