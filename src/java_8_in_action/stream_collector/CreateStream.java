package java_8_in_action.stream_collector;

import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CreateStream {
	public static void main(String[] args) {
		
		// 값으로 스트림 만들기
		Stream<String> stream = Stream.of("Java8", "Lambdas", "In", "Action");
		stream.map(String::toUpperCase).forEach(d -> System.out.println(d));
		
		// 배열로 스트림 만들기
		int[] numbers = {2, 3, 5, 7, 11, 13};
		int sum = Arrays.stream(numbers).sum();
		System.out.println(sum);
		
		// 함수로 무한 스트림 만들기 (iterate)
		Stream.iterate(0, n -> n + 2).limit(10).forEach(System.out::println);
		
		// 피보나치 수 만들기
		Stream.iterate(new int[] {0, 1}, t -> new int[]{t[1], t[0]+ t[1]})
				.limit(20)
				.forEach(t -> System.out.println("(" + t[0] + ", " + t[1] + ")"));
		
		System.out.println();
		// 함수로 무한 스트림 만들기 (generate)
		Stream.generate(Math::random)
				.limit(5)
				.forEach(System.out::println);
		
		// 피보나치 수 만들기
		IntSupplier fib = new IntSupplier() {
			private int previous = 0;
			private int current = 1;
			@Override
			public int getAsInt() {
				int oldPrevious = this.previous;
				int nextValue = this.previous + this.current;
				this.previous = this.current;
				this.current = nextValue;
				return oldPrevious;
			}
		};
		
		IntStream.generate(fib).limit(10).forEach(System.out::println);
	}
}
