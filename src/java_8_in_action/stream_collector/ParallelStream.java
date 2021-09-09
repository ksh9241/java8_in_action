package java_8_in_action.stream_collector;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParallelStream {
	public static void main(String[] args) {
//		System.out.println("순차 스트림 : " + measureSumPerf(ParallelStream::sequentialSum, 10_000_000) + " msecs");
//		
//		System.out.println("Upgrade Closed 사용 : " + measureSumPerf(ParallelStream::parallelRangedSum, 10_000_000) + " msecs");
//		
//		System.out.println("자바 기본 : " + measureSumPerf(ParallelStream::iterativeSum, 10_000_000) + " msecs");
//		
//		System.out.println("병렬 스트림 : " + measureSumPerf(ParallelStream::parallelSum, 10_000_000) + " msecs");
//		
//		System.out.println("Closed 사용 : " + measureSumPerf(ParallelStream::rangedSum, 10_000_000) + " msecs");
		
		System.out.println("Closed 사용 : " + measureSumPerf(ParallelStream::parallelSideEffectSum, 10_000_000) + " msecs");
		
		
	}
	
	// 스트림 성능 측정
	public static long measureSumPerf (Function<Long, Long> adder, long n) {
		long fastest = Long.MAX_VALUE;
		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			long sum = adder.apply(n);
			long duration = (System.nanoTime() - start) / 1_000_000;
			System.out.println("Result : " + sum);
			if (duration < fastest) fastest = duration;
		}
		return fastest;
	}
	
	// 병렬스트림 주의사항
	public static long sideEffectSum (long n) {
		Accumulator accumulator = new Accumulator();
		LongStream.rangeClosed (1, n).forEach (accumulator::add);
		return accumulator.total;
	}
	
	public static long parallelSideEffectSum (long n) { 
		// Accumulator 클래스에서 add 메서드가 total객체를 자꾸 변경하므로 병렬처리 시 값이 정확하지 않게 출력된다. 
		// 병렬처리 데이터를 정확하게 출력하기 위해선 데이터를 가변상태로 사용하지 않아야 한다. 
		Accumulator acc = new Accumulator();
		LongStream.rangeClosed(1, n).parallel().forEach(acc::add);
		return acc.total;
	}
	
	// 새로운 버전의 병렬 스트림
	public static long parallelRangedSum (long n) {
		return LongStream.rangeClosed(1, n)
							.parallel()
							.reduce(0, Long::sum);
	}
	
	// LongStream.rangeClosed 사용하여 병렬 처리하기
	public static long rangedSum (long n) {
		return LongStream.rangeClosed(1, n)
							.reduce(0L, Long::sum);
	}
	
	
	// 병렬 스트림
	public static long parallelSum (long n) {
		return Stream.iterate(1L, i -> i + 1)
						.limit(n)
						.parallel()
						.reduce(0L, Long::sum);
	}
	
	// 순차 스트림
	public static long sequentialSum (long n) {
		return Stream.iterate(1L, i -> i + 1)
						.limit(n)
						.reduce(0L, Long::sum);
	}
	
	// 자바 기본코드
	public static long iterativeSum(long n) {
		long result = 0;
		for (long i = 1L; i <= n; i++) {
			result += i;
		}
		
		return result;
	}
}

class Accumulator {
	public Accumulator() {}
	
	public long total = 0;
	public void add (long value) { total += value; }
}
