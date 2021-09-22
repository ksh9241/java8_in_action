package java_8_in_action.future;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class AddNonBlockSource {
	public static List<Shop> shops;
	
	public static void main(String[] args) {
		shops = Arrays.asList(new Shop("BestPrice"),
				new Shop("LetsSaveBig"),
				new Shop("MyFavoriteShop"),
				new Shop("FiveProduct"),
				new Shop("BuyItAll")
	);

	long start2 = System.nanoTime();
	System.out.println(findPrices("myPhone27S"));
	long duration = (System.nanoTime() - start2) / 1_000_000;
	System.out.println("Done in " + duration + "msecs");
	
	System.out.println("-----------------");
	
	long start = System.nanoTime();
	System.out.println(findPricesAsync("myPhone27S"));
	long duration2 = (System.nanoTime() - start) / 1_000_000;
	System.out.println("Parallel in " + duration2 + "msecs");
	
	System.out.println("-----------------");
	long start1 = System.nanoTime();
	System.out.println(findPrices_CompletableFuture("myPhone27S"));
	long duration1 = (System.nanoTime() - start1) / 1_000_000;
	System.out.println("CompletableFuture in " + duration1 + " msecs");
	}
	
	// 커스텀 Executor 만들기
	private final Executor executor = 
			Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() { // 상점 수 만큼의 스레드를 갖는 풀을 생성한다 (스레드 수의 범위는 0 과 100 사이).
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setDaemon(true); // 프로그램 종료를 방해하지 않는 데몬 스레드를 사용한다.
					return t;
				}
	});
	
	// 검색하려면 1초의 대기시간이 필요하므로 4초의 시간이 필요하다.
	private static List<String> findPrices (String product) {
		return shops.stream()
					.map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
					.collect(toList());
	}
	
	// CompletableFuture로 비동기 호출 구현
	private static List<String> findPrices_CompletableFuture (String product) {
		List<CompletableFuture<String>> priceFutures = shops.stream()
														.map(shop -> CompletableFuture.supplyAsync( // CompletableFuture로 각각의 가격을 비동기적으로 계산한다.
																() -> shop.getName() + " price is " + shop.getPrice(product)))
														.collect(toList());
		
		return priceFutures.stream().map(CompletableFuture::join) // 모든 비동기 동작이 끝나길 기다린다.
									.collect(toList());
	}
	
	
	// 병렬 스트림으로 처리하기
	private static List<String> findPricesAsync (String product) {
		return shops.parallelStream()
					.map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
					.collect(toList());
	}
}
