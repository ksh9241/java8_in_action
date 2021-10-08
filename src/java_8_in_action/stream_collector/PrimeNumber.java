package java_8_in_action.stream_collector;

import java.util.stream.IntStream;

public class PrimeNumber {
	public static void main(String[] args) {
		IntStream i = primes (numbers());
		System.out.println(i);
	}
	
	
	
	// 스트림 숫자 얻기
	static IntStream numbers() {
		return IntStream.iterate(2,  n -> n + 1);
	}
	
	// 머리 획득
	static int head (IntStream numbers) {
		return numbers.findFirst().getAsInt();
	}
	
	// 꼬리 필터링
	static IntStream tail (IntStream numbers) {
		return numbers.skip(1);
	}
	
	static IntStream primes (IntStream numbers) {
		// 정적 메서드 intStream.concat은 두 개의 스트림 인스턴스를 인수로 받는다. 두 번째 인수가 primes를 직접 재귀적으로 호출하면서 무한 재귀에 빠진다.
		// 그래서 자바 8 설계자는 이 같은 제한을 두기로 결정했다. 두 번째 인수에서 primes를 게으르게 평가하는 방식으로 문제를 해결할 수 있다.
		int head = head (numbers);
		return IntStream.concat(IntStream.of(head), primes(tail(numbers).filter(n -> n % head != 0)));
	}
}
