package java_8_in_action.lambda;

import java.util.function.Function;
import java.util.stream.Stream;

public class CloserExample {
	public static void main(String[] args) {
		
//		int count = 0;
//		Runnable incRunnable = () -> count += 1; // 암시적으로 final이 되므로 컴파일 에러가 발생함.
//		incRunnable.run();
//		System.out.println(count);
//		incRunnable.run();
		
		// 커링 예제
		Stream.of(1, 3, 5, 7)
				.map(multiplyCurry(2))
				.forEach(System.out::println);
	}

	private static Function<Integer, Integer> multiplyCurry(int x) {
		return (Integer y) -> x * y;
	}
}
