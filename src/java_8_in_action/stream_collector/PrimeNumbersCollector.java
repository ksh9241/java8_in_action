package java_8_in_action.stream_collector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;


public class PrimeNumbersCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>>{
	
	public static <A> List<A> takeWhile (List<A> list, Predicate<A> p) {
		int i = 0;
		for (A item : list) {
			if (!p.test(item)) {				// 리스트의 현재 요소가 프레디케이트를 만족하는지 검사한다.
				return list.subList(0, i);		// 프레디케이트를 만족하지 않으면 검사한 항목을 앞쪽에 위치한 서브리스트 반환한다.
			}
			i++;
		}
		return list;							// 리스트의 모든 항목이 프레디케이트를 만족하므로 리스트 자체를 반환한다.
	}
	
	public static boolean isPrime(List<Integer> primes, int candidate) {
		int candidateRoot = (int) Math.sqrt((double) candidate);
		return takeWhile(primes, i -> i <= candidateRoot)
							.stream()
							.noneMatch(p -> candidate % p == 0);
	}

	@Override /** 누적자를 만드는 함수를 반환해야 한다. true, false로 list 초기화. */
	public Supplier<Map<Boolean, List<Integer>>> supplier() {
		return () -> new HashMap<Boolean, List<Integer>> () {{
			put(true, new ArrayList<Integer>());
			put(false, new ArrayList<Integer>());
		}};
	}

	@Override /** 리스트를 조건에 따라 소수와 비소수로 나누어 supplier로 만든 리스트에 추가한다. */
	public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
		return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
			acc.get( isPrime(acc.get(true), candidate))	// isPrime의 결과에 따라 리스트와 비소수 리스트를 만든다.
				.add(candidate);						// candidate를 알맞은 리스트에 추가한다.
		};
	}

	/** 병렬 실행할 수 있는 컬렉터 만들기
	(참고로 알고리즘 자체가 순차적이어서 컬렉터를 실제 병렬로 처리할 순 없다. 따라서 combiner 메서드는 호출될 일이 없으므로 빈 구현으로 남겨두거나 UnsupportedOperationException을 던지도록 구현하는 방법도 좋다. 실제 이 메서드는 사용할 일이없지만 학습 목적으로 구현한다.) */
	@Override 
	public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
		return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
			map1.get(true).addAll(map2.get(true));
			map1.get(false).addAll(map2.get(false));
			return map1;
		};
	}

	@Override /** finisher 메서드와 컬렉터의 characteristics 메서드 */
	public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
		// accumulator의 형식은 컬렉터 결과 형식과 같으므로 변환 과정이 필요 없다. 따라서 항등함수 identity를 반환하도록 finisher를 구현하였다.
		return Function.identity();
	}

	@Override
	public Set<Characteristics> characteristics() {
		// 커스텀 컬렉터는 CONCURRENT도 아니고 UNORDERED 도 아니지만 IDENTITY_FINISH이므로 characteristics를 구현하였다.
		return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
	}
}
