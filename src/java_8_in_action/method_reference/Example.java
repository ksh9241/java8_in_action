package java_8_in_action.method_reference;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class Example {
	public static void main(String [] args) {
		// Function<String, Integer> stringToInteger = (String s) -> Integer.parseInt(s);
		Function<String, Integer> stringToInteger = Integer::parseInt;
		
		// BiPredicate<List<String> , String> contains = (list, element) -> list.contains(element)
		BiPredicate<List<String> , String> contains = List::contains;
		
	}
}
