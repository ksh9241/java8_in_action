package java_8_in_action.functionalprograming;

// 재귀를 사용하면 스택 프레임이 계속해서 생성되어 스택오버플로우가 발생한다. 그 문제를 해결하기 위해 꼬리 호출 최적화라는 해결책이 등장한다.
public class TailCallOptimization {
	public static void main(String[] args) {
		// 책과는 다르게 똑같이 스택오버플로우가 발생한다??
		long n = factorialTailRecursive(20);
		System.out.println(n);
	}
	
	static long factorialTailRecursive (long n) {
		return factorialHelper(1, n);
	}
	
	static long factorialHelper (long acc, long n) {
		return n == 1 ? acc : factorialHelper(acc * n, n - 1);
	}
	
//	static long factorial (long n) {
//		return n == 1 ? 1 : n * factorial(n - 1); 
//	}
	
}
