package java_8_in_action.refactoring_testing_debug;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java_8_in_action.lambda.BufferedReaderProcesser;

public class ReFactoringExcample {
	public static void main(String[] args) throws IOException {
		
		int a = 10;
		// 람다표현식으로는 변수를 가릴 수 없다. 때문에 컴파일 에러가 발생한다.
//		Runnable r1 = () -> {
//			int a = 2;
//			System.out.println(a);
//		};
		
		Runnable r2 = new Runnable() {
			public void run() {
				int a = 2;
				System.out.println(a);
			}
		};
		
		String oneLine = processFile((BufferedReader b) -> b.readLine()); // 람다 전달
		String twoLine = processFile((BufferedReader b) -> b.readLine() + b.readLine()); // 다른 람다 전달
		
	}
	public static void doSomething(Runnable r) { r.run(); }
	public static void doSomething(Task a) { a.execute(); }
	
	public static String processFile(BufferedReaderProcesser p) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader("java8inaction/chap8/data.txt"))) {
			return p.process(br); // 인수로 전달된 BufferedReaderProcessor 를 실행
		}
	}
	
	public interface bufferedReaderProcessor { // IOException을 던질 수 있는 람다의 함수형 인터페이스
		String process(BufferedReader b) throws IOException;
	}
	
//	public static void log(Level level, Supplier<String> msgSupplier) {
//		if (logger.isLoggable(level)) {
//			log(level, msgSupplier.get()); //람다 실행
//		}
//	}
}

interface Task {
	public void execute();
}


