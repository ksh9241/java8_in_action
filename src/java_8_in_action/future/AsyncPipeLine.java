package java_8_in_action.future;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AsyncPipeLine {
	private static Random r = new Random();
	private static List<Shop> shops;
	
	public static void main(String[] args) {
		shops = Arrays.asList(new Shop("BestPrice"),
				new Shop("LetsSaveBig"),
				new Shop("MyFavoriteShop"),
				new Shop("FiveProduct"),
				new Shop("BuyItAll")
		);
		
		for(Shop s : shops) {
			System.out.println(getPrice(s.getName()));
		}
	}
	
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
}
