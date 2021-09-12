package java_8_in_action.stream_collector;

import java.util.Spliterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
	
	// 반복형 단어 개수 메서드
	public static int countWordsIteratively (String s) {
		int counter = 0;
		boolean lastSpace = true;
		for (char c : s.toCharArray()) {
			if (Character.isWhitespace(c)) {
				lastSpace = true;
			} else {
				if (lastSpace) {
					counter++;
				}
				lastSpace = false;
			}
		}
		return counter;
	}
	
	public static void main (String [] args) {
		String sentence = " Nel    mezzo del   cammin   di nostra   vita " +
						"mi   ritrovai    in  una   selva  oscuse" +
						" ch    la    dritta  via era   samiahjkaf";
		
		System.out.println(sentence.length());
		
		System.out.println("Found == " + countWordsIteratively(sentence));
		
		Stream<Character> stream = IntStream.range(0, sentence.length())
											.mapToObj(sentence::charAt);
		
		
		WordCounter w = new WordCounter();
		System.out.println("Found == " + w.counterWords(stream) + " words");
		
		// 병렬 처리 시 데이터 정확성이 떨어짐.
		//System.out.println("Found == " + w.counterWords(stream.parallel()) + " words");
		
		// 병렬 처리 클래스 재정의
		Spliterator<Character> spliterator = new WordCounterSpliterator(sentence);
		Stream<Character> parallelStream = StreamSupport.stream(spliterator, true);
		WordCounter wordCounter = parallelStream.reduce(new WordCounter(0, true), WordCounter::accumulate, WordCounter::combine);
		System.out.println(wordCounter.getCounter());
		
		
		
	}
}

// 반복형 단어 개수 메서드 스트림으로 재구현을 위한 클래스
class WordCounter {
	private int counter;
	private boolean lastSpace;
	
	public WordCounter() {}
	
	public WordCounter (int counter, boolean lastSpace) {
		this.counter = counter;
		this.lastSpace = lastSpace;
	}
	
	// 반복 알고리즘처럼 accumulate 메서드는 문자열의 문자를 하나씩 탐색한다.
	public WordCounter accumulate (Character c) { 
		if (Character.isWhitespace(c)) {
			return lastSpace ? this : new WordCounter(counter, true);
		} else {
			// 문자를 하나씩 탐색하다 공백 문자를 만나면 지금까지 탐색한 문자를 단어로 간주하여 (공백 문자는 제외) 단어 개수를 증가시킨다.
			return lastSpace ? new WordCounter (counter + 1, false) : this; 
		}
	}
	
	public WordCounter combine (WordCounter wordCounter) {
		return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
	}
	
	public int counterWords (Stream<Character> stream) {
		WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
												WordCounter::accumulate,
												WordCounter::combine);
		return wordCounter.getCounter();
	}
	
	public int getCounter () {
		return this.counter;
	}
}


// 병렬 스트림을 하기 위한 클래스 재정의
class WordCounterSpliterator implements Spliterator<Character> {
	private String string;
	private int currentChar = 0;
	
	public WordCounterSpliterator (String string) {
		this.string = string;
	}

	@Override
	public boolean tryAdvance(Consumer<? super Character> action) {
		action.accept(string.charAt(currentChar++)); 	// 현재 문자를 소비한다.
		return currentChar < string.length();				// 소비할 문자가 남아있으면 true를 반환한다.
	}

	@Override
	public Spliterator<Character> trySplit() {
		int currentSize = string.length() - currentChar;
		if (currentChar < 10) {
			// 파싱할 문자열을 순차 처리할 수 있을 만큼 충분히 작아졌음을 알리는 null을 반환.
			return null;
		}
		for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
			if (Character.isWhitespace(string.charAt(splitPos))) {
				
				// 처음부터 분할 위치까지 문자열을 파싱할 새로운 wordCounterSpliterator를 생성
				Spliterator<Character> spliterator = new WordCounterSpliterator(string.substring(currentChar, splitPos));
				
				// 이 WordCounterSpliterator 의 시작 위치를 분할 위치로 설정한다.
				currentChar = splitPos;
				return spliterator;
			}
		}
		return null;
	}

	@Override
	public long estimateSize() {
		return string.length() - currentChar;
	}

	@Override
	public int characteristics() {
		return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
	}
	
}