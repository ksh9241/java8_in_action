package java_8_in_action.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Example {
	public static void main(String[] args) {
		// 스레드 풀에 태스크를 제출하려면 ExecutorService를 만들어야 한다.
		ExecutorService executor = Executors.newCachedThreadPool();
		
		// Callable을 ExecutorService로 제출한다.
		Future<Double> future = executor.submit(new Callable<Double>() {
			@Override
			public Double call() throws Exception {
				// 시간이 오래 걸리는 작업은 다른 스레드에서 비동기적으로 실행한다.
				return doSomeLongComputation();
			}

			
		});
		
		//비동기 작업을 수행하는 동안 다른 작업을 한다.
		doSumethingElse();
		
		try {
			// 비동기 작업의 결과를 가져온다. 결과가 준비되어 있지 않으면 호출 스레드가 블록된다. 하지만 최대 1초까지만 기다린다.
			// 이 부분을 추가한 이유는 오래걸리는 작업이 끝나지 않는 경우를 위한 예외 처리이다.
			Double result = future.get(1, TimeUnit.SECONDS);
		} catch (ExecutionException ee) {
			// 계산 중 예외 발생
		} catch (InterruptedException ie) {
			// 현재 스레드에서 대기 중 인터럽트 발생
		} catch (TimeoutException te) {
			// Future가 완료되기 전에 타임아웃 발생
		}
	}

	static void doSumethingElse() {
		// TODO Auto-generated method stub
		
	}
	
	static Double doSomeLongComputation() {
		// TODO Auto-generated method stub
		return null;
	}
}
