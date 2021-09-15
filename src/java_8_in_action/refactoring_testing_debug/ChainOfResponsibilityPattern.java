package java_8_in_action.refactoring_testing_debug;

import java.util.function.Function;
import java.util.function.UnaryOperator;

// 의무 체인 패턴
public class ChainOfResponsibilityPattern {
	public static void main(String[] args) {
		ProcessingObject<String> p1 = new HeaderTextProcessing();
		ProcessingObject<String> p2 = new SpellChenkerProcessing();
		
		p1.setSuccessor(p2);
		
		String result = p1.handle("Aren't labdas really sexy?!!");
		System.out.println(result);
		
		// 람다 표현식 사용
		UnaryOperator<String> headerProcessing =
				(String text) -> "From Raoul, Mario and Alan : " + text;
				
		UnaryOperator<String> spellChenkerProcessing =
				(String text) -> text.replaceAll("labda", "lambda");
				
		Function<String, String> pipeline = headerProcessing.andThen(spellChenkerProcessing);
		// andThen() -> headerProcessing의 값을 먼저 입력한 다음 spellCheckerProcessing을 실행하여 labda를 lambda로 replaceAll한다.
		
		String result2 = pipeline.apply("Aren't labdas really sexy?!!");
		System.out.println(result2);
	}
}

abstract class ProcessingObject<T> {
	protected ProcessingObject<T> successor;
	
	public void setSuccessor(ProcessingObject<T> successor) {
		this.successor = successor;
	}
	
	public T handle (T input) {
		T r = handleWork (input);
		if (successor != null) {
			return successor.handle(r);
		}
		return r;
	}
	
	abstract protected T handleWork (T input);
}

class HeaderTextProcessing extends ProcessingObject<String> {

	@Override
	protected String handleWork(String text) {
		return "From Raoul, Mario and Alan : " + text;
	}
}

class SpellChenkerProcessing extends ProcessingObject<String> {

	@Override
	protected String handleWork(String text) {
		return text.replaceAll("labda", "lambda");
	}
}



