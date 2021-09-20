package java_8_in_action.future;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class AsynchronizedException {
	public static void main(String[] args) {
		Shop shop = new Shop("BestShop");
		long start = System.nanoTime();
		// 상점에 제품가격 정보 요청
		Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
		long invocationTime = ((System.nanoTime() - start) / 1_000_000);
		System.out.println("Invocation returned after " + invocationTime + " msecs");
		
		doSomethingElse();
		try {
			// 가격 정보가 있으면 Futrue에서 가격정보를 읽고, 가격정보가 없으면 정보를 받을 때까지 블록한다.
			double price = futurePrice.get();
			System.out.printf("Price is %.2f%n", price);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		long retrivalTime = ((System.nanoTime() - start) / 1_000_000);
		System.out.println("Price returned after " + retrivalTime + " msecs");
	}

	private static void doSomethingElse() {
		
	}
}

class Shop {
	private String name;
	
	public Shop (String name) {
		this.name = name;
	}
	
	public double getPrice(String product) {
		return calculatePrice(product);
	}
	
	// getPrice 동기 메서드를 비동기 메서드로 변환
	public Future<Double> getPriceAsync (String product) {
		CompletableFuture<Double> futurePrice = new CompletableFuture<>();
		new Thread( () -> {
			double price = calculatePrice(product);
			futurePrice.complete(price);
		}).start();
		
		return futurePrice;
	}
	
	public double calculatePrice (String product) {
		delay();
		Random random = new Random();
		return random.nextDouble() * product.charAt(0) + product.charAt(1);
	}
	
	public void delay() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			throw new RuntimeException(ie);
		}
	}
}
