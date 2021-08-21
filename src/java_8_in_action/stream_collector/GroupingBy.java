package java_8_in_action.stream_collector;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toCollection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import java_8_in_action.stream_collector.GroupingBy.CaloricLevel;

public class GroupingBy {
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
		
		// Collectors.groupingBy 사용하기
		Map<Dish.Type, List<Dish>> dishesByType = 
										menu.stream().collect(groupingBy(Dish::getType));
		
		System.out.println(dishesByType+"\n");
		
		
		// 다수준 그룹화
		Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = 
							menu.stream().collect(groupingBy(Dish::getType, 
								groupingBy(dish -> {
			if (dish.getCalories() <= 400) {
				return CaloricLevel.DIET;
			} else if (dish.getCalories() <= 700) {
				return CaloricLevel.NORMAL;
			} else {
				return CaloricLevel.FAT;
			}
		})));
		
		System.out.println(dishesByTypeCaloricLevel+"\n");
		
		// 서브그룹으로 데이터 수집
		Map<Dish.Type, Long> typesCount = menu.stream().collect(groupingBy(Dish::getType, counting()));
		System.out.println(typesCount+"\n");
		
		// 종류로 분류한 컬렉션 중 가장 높은 칼로리 찾기
		Map<Dish.Type, Optional<Dish>> mostCaloricByType = 
					menu.stream()
						.collect(groupingBy(Dish::getType, maxBy(comparingInt(Dish::getCalories))));
		
		System.out.println(mostCaloricByType+"\n");
		
		Map<Dish.Type, Dish> mostCaloricByType2 = 
				menu.stream().collect(groupingBy(Dish::getType, collectingAndThen(maxBy(comparing(Dish::getCalories)), Optional::get)));
		
		System.out.println(mostCaloricByType2+"\n");
		
		
		// 메뉴에 있는 모든 칼로리 합계 구하기
		Map<Dish.Type, Integer> totalCalories = 
				menu.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));
		
		System.out.println(totalCalories);
		
		// HashSet을 이용하여 각 Type별 존재하는 칼로리 레벨 그룹처리하기
		Map<Dish.Type, Set<CaloricLevel>> caloricLevelByType =
				menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
					if (dish.getCalories() <= 400) {
						return CaloricLevel.DIET;
					} else if (dish.getCalories() <= 700) {
						return CaloricLevel.NORMAL;
					} else {
						return CaloricLevel.FAT;
					}
				}, toSet())));
		
		System.out.println(caloricLevelByType);
		
		// toCollection를 사용하면 원하는 방식으로 결과를 반환할 수 있다.
		Map<Dish.Type, Set<CaloricLevel>> caloricLevelByType2 =
				menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
					if (dish.getCalories() <= 400) {
						return CaloricLevel.DIET;
					} else if (dish.getCalories() <= 700) {
						return CaloricLevel.NORMAL;
					} else {
						return CaloricLevel.FAT;
					}
				}, toCollection(HashSet::new))));
		
		System.out.println(caloricLevelByType2);
	}
	public enum CaloricLevel {DIET, NORMAL, FAT}
}
