package java_8_in_action.refactoring_testing_debug;

public class StrategyPattern{
	public static void main(String[] args) {
//		Validator numericValidator = new Validator(new IsNumeric());
//		boolean b1 = numericValidator.validate("aaaa"); // false 반환
//		Validator lowerCaseValidator = new Validator(new IsAllLowerCase());
//		boolean b2 = lowerCaseValidator.validate("bbbb"); // true 반환
//		
//		System.out.println(b1);
//		System.out.println(b2);
		
		
		Validator numericValidator2 = new Validator((String s) -> s.matches("[a-z]+"));
		boolean bb1 = numericValidator2.validate("aaaa");
		
		Validator lowerCaseValidator2 = new Validator((String s) -> s.matches("\\d+"));
		boolean bb2 = lowerCaseValidator2.validate("bbbb");
		
		System.out.println(bb1);
		System.out.println(bb2);
	}

}

interface ValidationStrategy {
	boolean execute (String s);
}

class IsAllLowerCase implements ValidationStrategy {
	@Override
	public boolean execute(String s) {
		return s.matches("[a-z]+");
	}
	
}

class IsNumeric implements ValidationStrategy {
	@Override
	public boolean execute(String s) {
		return s.matches("\\d+");
	}
}

class Validator {
	private final ValidationStrategy strategy;
	
	public Validator (ValidationStrategy v) {
		this.strategy = v;
	}
	
	public boolean validate (String s) {
		return strategy.execute(s);
	}
}


