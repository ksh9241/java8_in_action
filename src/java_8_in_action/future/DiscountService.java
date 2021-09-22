package java_8_in_action.future;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
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