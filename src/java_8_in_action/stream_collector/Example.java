package java_8_in_action.stream_collector;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Example {
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
		
		List<String> lowCaloricDishsName = menu.stream()
												.filter(d -> d.getCalories() < 400)
												.sorted(comparing(Dish::getCalories))
												.map(Dish::getName)
												.limit(3)
												.collect(Collectors.toList());
		// Collectors.toList()로 반환하였기 때문에 예외가 발생하지 않는다.
		lowCaloricDishsName.forEach(System.out::println);
		lowCaloricDishsName.forEach(System.out::println);
		
		
		// Stream을 두번 호출하게 될 경우 IllegalStateException이 발생한다.
		List<String> title = Arrays.asList("JAVA8", "IN", "ACTION");
		Stream<String> s = title.stream();
		
		s.forEach(System.out::println);
		s.forEach(System.out::println);
		
		System.out.println();
		
		// 외부반복과 내부반복
		
		// 외부 반복
		List<String> names = new ArrayList<>();
		for(Dish d : menu) {
			names.add(d.getName());
		}
		
		// 내부적으로 숨겨졌던 반복자 (iterator)를 통한 외부반복
		List<String> names2 = new ArrayList<>();
		Iterator<Dish> iterator = menu.iterator();
		while (iterator.hasNext()) {
			Dish d = iterator.next();
			names2.add(d.getName());
		}
		
		// 내부반복
		List<String> names3 = menu.stream().map(Dish::getName).collect(Collectors.toList());
	}
}
