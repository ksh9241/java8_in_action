package java_8_in_action.refactoring_testing_debug;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FactoryPattern {
	public static void main(String[] args) {
		Product p = ProductFactory.createProduct("loan");
		
		// 람다 표현식 사용
		Supplier<Product> loanSupplier = Loan::new;
		Loan loan = (Loan) loanSupplier.get();
		System.out.println(loan.myName());
		
		System.out.println();

		Product stokcSupplier = createProduct2("stock");
		System.out.println(stokcSupplier.myName());
		
	}
	
	// 상품명을 생성자로 연결하는 Map을 만들어서 코드를 재구현할 수 있다.
	static final Map<String, Supplier<Product>> map = new HashMap<>();
	static {
		map.put("loan", Loan::new);
		map.put("stock", Stock::new);
		map.put("bond", Bond::new);
	}
	
	public static Product createProduct2 (String name) {
		Supplier<Product> p = map.get(name);
		if (p != null) return p.get();
		
		throw new IllegalArgumentException("No such product " + name);
	}
}

class ProductFactory {
	public static Product createProduct(String name) {
		switch (name) {
		case "loan" :
			return new Loan();
		case "stock" :
			return new Stock();
		case "bond" :
			return new Bond();
		default :
			throw new RuntimeException("No such product " + name);
		}
	}
}

interface Product {
	public String myName();
}

class Loan implements Product {
	public Loan () {
		//System.out.println("Loan!!!");
	}

	@Override
	public String myName() {
		return "Loan";
	}
}

class Stock implements Product {
	public Stock() {
		//System.out.println("Stock!!!");
	}

	@Override
	public String myName() {
		return "Stock";
	}
}

class Bond implements Product {
	public Bond () {
		//System.out.println("Bond!!!");
	}

	@Override
	public String myName() {
		return "Bond";
	}
}
