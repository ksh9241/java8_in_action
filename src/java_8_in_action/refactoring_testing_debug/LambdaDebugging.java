package java_8_in_action.refactoring_testing_debug;

import java.util.Arrays;
import java.util.List;

public class LambdaDebugging {
	public static void main(String[] args) {
		List<Point> points = Arrays.asList(new Point(12, 2), null);
		//points.stream().map(p -> p.getX()).forEach(System.out::println);
		
		// 메서드 레퍼런스를 사용하였지만 스택트레이스의 이름이 표시되지 않음.
		//points.stream().map(Point::getX).forEach(System.out::println);
		
		// 메서드 레퍼런스를 사용하여 스택트레이스의 이름이 표기됨.
		List<Integer> numbers = Arrays.asList(1, 2, 3);
		numbers.stream().map(LambdaDebugging::divideByZero).forEach(System.out::println);
	}
	
	public static int divideByZero(int n) {
		return n / 0;
	}
}

class Point {
	int x;
	int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}