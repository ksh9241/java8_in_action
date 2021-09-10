package java_8_in_action.stream_collector;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class ForkJoinSumCalculator extends java.util.concurrent.RecursiveTask<Long> {

	private final long[] numbers;
	private final int start;
	private final int end;
	public static final long THRESHOLD = 10_000; // 분할할 최소 값
	
	public ForkJoinSumCalculator (long[] numbers) {
		this(numbers, 0, numbers.length);
	}
	
	
	// 메인 태스크의 서브태스크를 재귀적으로 만들 때 사용할 비공개 생성자
	private ForkJoinSumCalculator(long[] numbers, int start, int end) { 
		this.numbers = numbers;
		this.start = start;
		this.end = end;
	}


	@Override
	protected Long compute() {
		int length = end - start;
		if (length <= THRESHOLD) {
			// 기준값과 같거나 작으면 순차적으로 결과를 계산한다.
			return computeSequentially(); 
		}
		
		// 배열의 절반을 더하도록 서브태스크 생성
		ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2);
		
		// ForkJoinPool의 다른 스레드로 새로 생성한 태스크를 비동기로 실행
		leftTask.fork();
		
		// 배열의 나머지 절반을 더하도록 서브태스크 생성
		ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);
		
		// 두 번째 서브태스크를 동기 실행한다.
		Long rightResult = rightTask.compute();
		
		// 첫 번째 서브태스크의 결과를 읽거나 아직 결과가 없으면 기다린다.
		Long leftResult = leftTask.join();
		
		// 두 서브태스크의 결과를 조합한 값을 반환한다.
		return rightResult + leftResult;
	}


	// 더 분할할 수 없을 때 서브태스크의 결과를 계산하는 단순 알고리즘
	private Long computeSequentially() {
		long sum = 0;
		for (int i = 0; i < end; i++) {
			sum += numbers[i];
		}
		return sum;
	}
	
	//
	public static long forkJoinSum (long n) {
		long[] numbers = LongStream.rangeClosed(1, n).toArray();
		ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
		return new ForkJoinPool().invoke(task);
	}

}
