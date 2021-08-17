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
		
		// 피타고라스 수 (a 제곱 + b 제곱 = c 제곱)
		Stream<int[]> pythagoreanTriples =
				IntStream.rangeClosed(1, 100).boxed() 								// rangeClosed가 반환한 IntStream을 boxed를 이용해서 Stream<Integer>로 복원했다. 
				.flatMap(a -> 														// flatMap 메서드는 생성된 각각의 스트림을 하나의 평준화된 스트림으로 만들어준다.
					IntStream.range(a, 100)
						.filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)				// Math.sqrt : 입력값에 루트를 씌운다. ex) 9 = 3.0, 4 = 2.0
						.mapToObj(b ->												// 개체값 스트림을 반환하는 IntStream의 mapToObj 메서드를 이용해서 이 코드를 재구현할 수 있다.
							new int[] {a, b, (int) Math.sqrt(a * a + b * b)})
				);
		
		pythagoreanTriples.limit(5)
							.forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
		
		System.out.println();
		// 개선된 코드
		Stream<double[]> pythagoreanTriples2 = 
				IntStream.range(1, 100).boxed()
				.flatMap(a ->
					IntStream.range(a, 100)
						.mapToObj(
							b -> new double[] {a, b, Math.sqrt(a * a + b * b)})
						.filter(t -> t[2] % 1 == 0));
		
		pythagoreanTriples2.limit(5)
							.forEach(t -> System.out.println((int)t[0] + ", " + (int)t[1] + ", " + (int)t[2]));
	}
	
	
}
