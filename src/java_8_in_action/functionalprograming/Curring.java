package java_8_in_action.functionalprograming;

import java.util.function.DoubleUnaryOperator;

public class Curring {
	public static void main(String[] args) {
		
		double xx = converter(5 , 9.0/5, 32);
		System.out.println(xx);
		
		DoubleUnaryOperator xxx = curriedConerter(9.0/5, 32);
		System.out.println(xxx.applyAsDouble(5));
	}
	
	// 코드의 문제점 이 코드는 섭씨를 화씨로 변환하는 기능만 사용하는 메서드이다.
	// x = 변환값, f = 변환 요소, b = 기준치 조정 요소 
	static double converter (double x, double f, double b) {
		return x * f + b;
	}
	
	// f = 변환 요소, b = 기준치 만 넘겨주어 변환하는 공통함수로 사용할 수 있다.
	static DoubleUnaryOperator curriedConerter (double f, double b) {
		return (double x) -> x * f + b;
	}
}
