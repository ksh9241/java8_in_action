package java_8_in_action.stream_collector;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FindAndMatch {
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
		
		// anyMatch : 하나 이상의 요소가 주어진 조건에 만족하는지 조사
		if (menu.stream().anyMatch(Dish::isVegetarian)) {
			System.out.println("The menu is (somewhat) vegetarian friendly!!");
		}
		
		// allMatch : 모든 요소들이 매개값으로 주어진 조건을 만족하는지 조사
		boolean isHealthy = menu.stream().allMatch(d -> d.getCalories() < 1000); // menu의 모든 리스트의 칼로리가 1000이 넘지 않기 때문에 true를 반환
		boolean checkPoint = menu.stream().allMatch(d -> d.getCalories() < 800); // pork의 칼로리가 800이기 조건에 맞지 않아 false를 반환
		System.out.println(isHealthy);
		System.out.println(checkPoint);
		
		System.out.println();
		// noneMatch : 모든 요소들이 주어진 조건을 만족하지 않는지 검사 (질문이 부정문임)
		boolean isHealthy2 = menu.stream().noneMatch(d -> d.getCalories() > 1000); // list(i)값이 1000 작은게 없나?
		boolean checkPoint2 = menu.stream().noneMatch(d -> d.getCalories() < 800); // 모든조건 중 pork가 800보다 작지 않기 때문에 false를 반환함.
		System.out.println(isHealthy2);
		System.out.println(checkPoint2);
		
		
		// Optional 사용하기
		Optional<Dish> dish = menu.stream().filter(Dish::isVegetarian).findAny();
		Dish d = dish.get();
		
		System.out.println(d);
		
		menu.stream().filter(Dish::isVegetarian).findAny().ifPresent(dd -> System.out.println(dd.getName()));
		
		// 첫번째 요소 찾기
		List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
		// 리스트 값을 제곱근으로 처리한 뒤 (1, 4, 9, 16, 25) 3 나머지가 0인 조건의 첫번째 값을 반환한다.
		Optional<Integer> firstSquareDivisibleByThree = someNumbers.stream().map(x -> x * x).filter(x -> x % 4 == 0).findFirst();
		int result = firstSquareDivisibleByThree.get();
		System.out.println(result);
		
		
	}
}
