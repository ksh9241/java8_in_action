package java_8_in_action.stream_collector;

import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

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
								
		
		// Stream을 이용한 value length
		List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");
		List<Integer> wordsList = words.stream().map(String::length).collect(toList());
		System.out.println(wordsList);
		
		// Stream을 이용한 메뉴명 길이 체크
		List<Integer> dishMenuLength = menu.stream().map(Dish::getName).map(String::length).collect(toList());
		System.out.println(dishMenuLength);
		
		// Stream을 이용한 split 실패 사례
		List<String> arr = Arrays.asList("Hello", "World");
		arr.stream().map(word -> word.split("")).map(Arrays::stream).distinct().collect(toList());
		System.out.println(arr);
		
		// Stream을 이용한 split 성공 사례
		List<String> result = arr.stream().map(a -> a.split("")).flatMap(Arrays::stream).distinct().collect(toList());
		System.out.println(result);
		
		// test 1
		List<Integer> number = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> numberResult = number.stream().map((d) -> {return d * d;}).collect(toList());
		//책 정답
		// List<Integer> numberResult = number.stream().map(d -> d * d).collect(toList());
		System.out.println(numberResult);
		
		// test 2
		List<Integer> number1 = Arrays.asList(1, 2, 3);
		List<Integer> number2 = Arrays.asList(3, 4);
		
		List<int[]> pairs = number1.stream().flatMap(i -> number2.stream().map(j -> new int[]{i, j})).collect(toList());
		System.out.println(pairs);
		
		List<int[]> pairs2 = number1.stream().flatMap(i -> number2.stream().filter(j -> (i + j) % 3 == 0).map(j -> new int[] {i, j})).collect(toList());
		System.out.println(pairs2);
	}
}
