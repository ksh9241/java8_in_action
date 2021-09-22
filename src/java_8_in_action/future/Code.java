package java_8_in_action.future;

public enum Code {
	NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);
	
	private final int percentage;
	
	Code (int percentage) {
		this.percentage = percentage;
	}
	
	public static String applyDiscount (Quote quote) {
		return quote.getShopName() + " price is " +
				apply(quote.getPrice(), quote.getDiscountCode());
				// apply : 기존 가격에 할인 코드를 적용한다.
	}
	
	public static double apply (double price, Code code) {
		AsyncPipeLine.delay();
		return (price * (100 - code.percentage) / 100);
	}
}