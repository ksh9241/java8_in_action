package java_8_in_action.future;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

public class DiscountService {
	private static List<Shop> shops;
	static Random r = new Random();
	public static void main(String[] args) {
		shops = Arrays.asList(new Shop("BestPrice"),
				new Shop("LetsSaveBig"),
				new Shop("MyFavoriteShop"),
				new Shop("FiveProduct"),
				new Shop("BuyItAll")
		);
		
		
		for (Shop s : shops) {
			System.out.println(findPrices(s.getName()));
		}		
	}
	
	public static List<String> findPrices (String product) {
		return shops.stream().map(shop -> getPrice(shop.getName()))
							 .map(Quote::parse)
							 .map(Code::applyDiscount)
							 .collect(Collectors.toList());
	}
	
//	public static List<String> findPrices_CompletableFuture (String product) {
//		List<CompletableFuture<String>> priceFutures = shops.stream().map(shop -> CompletableFuture.supplyAsync( // 각 상점에서 할인전 가격을 비동기적으로 얻는다.
//																		() -> shop.getPrice(product), executor))
//				
//																	 // 상점에서 반환한 문자열을 Quote객체로 반환한다.
//																	 .map(future -> future.thenApply(Quote::parse))
//																	 
//																	 // 결과 Future를 다른 비동기 작업과 조합해서 할인코드를 적용한다.
//																	 .map(future -> future.thenCompose(
//																			 quote -> CompletableFuture.supplyAsync(
//																					 () -> Code.applyDiscount(quote), executor)))
//																	 .collect(Collectors.toList());
//		
//		// 스트림의 모든 Future가 종료되길 기다렸다가 각각의 결과를 추출한다.
//		return priceFutures.stream().map(CompletableFuture::join)
//									.collect(Collectors.toList());
//	}
	
	private static final Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100),
														new ThreadFactory() {
					public Thread newThread(Runnable r) {
						Thread t = new Thread(r);
						t.setDaemon(true);
						return t;
					}
	});
	
	public static String getPrice(String product) {
		
		double price = calculatePrice(product);
		Code code = Code.values() [r.nextInt(Code.values().length)];
		return String.format("%s:%.2f:%s", product, price, code);
	}
	
	private static double calculatePrice(String product) {
		delay();
		return r.nextDouble() * product.charAt(0) + product.charAt(1); 
	}
	
	protected static void delay() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			throw new RuntimeException(ie);
		}
	}
	
	// 0.5 ~ 2.5초 사이의 임의의 지연을 흉내내는 메서드
	protected static void randomDelay () {
		int delay = 500 + r.nextInt(2000);
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}

class Quote {
	private String shopName;
	private double price;
	private Code discountCode;
	
	public Quote (String shopName, double price, Code discountCode) {
		this.shopName = shopName;
		this.price = price;
		this.discountCode = discountCode;
	}
	
	public static Quote parse (String s) {
		String split[] = s.split(":");
		String shopName = split[0];
		double price = Double.parseDouble(split[1]);
		Code discountCode = Code.valueOf(split[2]);
		return new Quote (shopName, price, discountCode);
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Code getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(Code discountCode) {
		this.discountCode = discountCode;
	}
	
	
}