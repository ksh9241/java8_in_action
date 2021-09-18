package java_8_in_action.optional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

 
public class Example2 {
	public static void main(String[] args) {
		//nullCheck();
		
		Optional<Integer> number = stringToInt("asd");
		System.out.println(number.get());
	}

	/* 잠재적으로 null이 될 수 있는 대상을 Optional로 감싸기*/
	public static void nullCheck () {
		Map<String, Object> map = new HashMap<>();

		// 수정 전 : null 반환
		Object value = map.get("key");
		System.out.println(value);
		
		// 수정 후 : 예외 발생
		Optional<Object> value2 = Optional.ofNullable(map.get("key"));
		Object obj = value2.get();
		System.out.println(obj);
	}
	
	public static Optional<Integer> stringToInt(String s) {
		try {
			// 문자열을 정수로 변환할 수 있으면 정수로 변환된 값을 포함하는 Optional을 반환
			return Optional.of(Integer.parseInt(s));
			
		} catch (NumberFormatException e) {
			// 그렇지 않다면 빈 Optional을 반환.
			return Optional.empty();
			
		}
	}
}
