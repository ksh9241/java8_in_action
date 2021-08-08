package java_8_in_action.method_reference;

import java.util.Arrays;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		List<String> arr = Arrays.asList("a", "b", "A", "B");
		
		System.out.println("before::"+arr);
		
		// 람다 표현식
		//arr.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
		
		// 메서드 레퍼런스
		arr.sort(String::compareToIgnoreCase);
		
		
		System.out.println("after::"+arr);
		
	}
}
