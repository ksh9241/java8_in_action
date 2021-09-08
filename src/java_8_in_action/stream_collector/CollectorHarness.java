package java_8_in_action.stream_collector;

import static java.util.stream.Collectors.partitioningBy;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

// 기본 컬렉터 vs 커스텀 컬렉터 성능 비교
public class CollectorHarness {
	public static void main(String[] args) {
		long fastest = Long.MAX_VALUE;
		for (int i = 0; i < 10; i++) {									// 테스트를 10번 반복
			long start = System.nanoTime();
			//partitionPrimes(1_000_000);								// 백만 개의 숫자를 소수와 비소수로 분할한다.		(604ms)
			partitionPrimesWithCustomCollector(1_000_000);				//									(302ms)									
			long duration = (System.nanoTime() - start) / 1_000_000;	// duration을 밀리초 단위로 측정
			if (duration < fastest) {									// 가장 빨리 실행되었는지 확인.
				fastest = duration;
			}
		}
		System.out.println("Fastest execution done in " + fastest + "msecs");
	}
	
	public static boolean isPrime(int candidate) {
		
		// 소수의 대상을 주어진 수의 제곱근 이하의 수로 제한함.
		int candidateRoot = (int) Math.sqrt((double) candidate);
		return IntStream.rangeClosed(2, candidateRoot)
							.noneMatch(i -> candidate % i == 0);
		
//		 2 부터 candidate 미만의 자연수를 생성함.
//		return IntStream.range(2, candidate)
//							.noneMatch(i -> candidate % i == 0);
	}
	
	// 기존 컬렉터로 사용한 메서드
	public static Map<Boolean, List<Integer>> partitionPrimes (int n) {
		return IntStream.rangeClosed(2, n).boxed()
							.collect(partitioningBy(candidate -> isPrime(candidate)));
	}
	
	// 커스텀 컬렉터 (PrimeNumbersCollector)를 사용하는 메서드
	public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n) {
		return IntStream.rangeClosed(2, n).boxed()
						.collect(new PrimeNumbersCollector());
	}
}
