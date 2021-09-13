package java_8_in_action.refactoring_testing_debug;

//public class TemplateMethodPattern {
//	public static void main(String[] args) {
//		new OnlineBankingLambda().processCustomer(1337, (Customer c) -> System.out.println("Hello "+c.getName()));
//	}
//}
//
//abstract class OnlineBanking {
//	public void processCustomer ( int id) {
//		Customer c = Database.getCustomerWithId(id);
//		makeCustomerHappy(c);
//	}
//	
//	abstract void makeCustomerHappy (Customer c);
//	
//	// 람다 표현식 사용
//	public void processCustomer(int id, Consumer<Customer> makeCustomerHappy) {
//		Customer c = Database.getCustomerWithId(id);
//		makeCustomerHappy.accept(c);
//	}
//}