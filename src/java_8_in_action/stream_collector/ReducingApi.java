package java_8_in_action.stream_collector;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.maxBy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReducingApi {
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
		
		// 1. counting()
		long howManyDishes = menu.stream().collect(counting());
		System.out.println(howManyDishes);
		
		// 2. Stream find maxVal
		Comparator<Dish> dishCaloriesComparator = Comparator.comparing(Dish::getCalories);
		
		Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator));
		Dish maxDish = mostCalorieDish.get();
		
		System.out.println(maxDish);
		
		// 3. Stream find minVal
		Comparator<Dish> dishCaloriesComparator2 = Comparator.comparing(Dish::getCalories).reversed();
		
		Optional<Dish> minCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator2));
		
		Dish minDish = minCalorieDish.get();
		System.out.println(minDish);
		
		// 요약연산
		int totalCalories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
		System.out.println(totalCalories);
		
		double averageCalories = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
		System.out.println((int) averageCalories);
		
		// 합계, 평균, 최대값, 카운트를 다양하게 사용해야 할 때
		IntSummaryStatistics menuStatistics = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
		System.out.println(menuStatistics);
		
		// 문자열 연결
		String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining());
		System.out.println(shortMenu);
		
		String shortMenu2 = menu.stream().map(Dish::getName).collect(Collectors.joining(", "));
		System.out.println(shortMenu2);
		
		// 범용 리듀싱 요약 연산
		int totalCalories2 = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (i, j) -> i + j));
		System.out.println(totalCalories2);
		
		// 리듀싱을 이용한 가장 높은 칼로리 찾기.
		Optional<Dish> bestCalorieDish = menu.stream().collect(Collectors.reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
		// reducing은 한개의 인수를 받을 경우 첫 번째 매개변수인 반환값이 없으니 Optional로 반환하여 null값을 체크한다.
		
		Integer bestCalorieDish2 = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (d1, d2) -> d1 > d2 ? d1 : d2));
		
		Dish bestResult = bestCalorieDish.get();
		
		System.out.println(bestResult);
		System.out.println(bestCalorieDish2);
		
		
	}
}
