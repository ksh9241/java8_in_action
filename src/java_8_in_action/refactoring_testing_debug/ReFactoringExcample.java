package java_8_in_action.refactoring_testing_debug;

public class ReFactoringExcample {
	public static void main(String[] args) {
		
		int a = 10;
		// 람다표현식으로는 변수를 가릴 수 없다. 때문에 컴파일 에러가 발생한다.
//		Runnable r1 = () -> {
//			int a = 2;
//			System.out.println(a);
//		};
		
		Runnable r2 = new Runnable() {
			public void run() {
				int a = 2;
				System.out.println(a);
			}
		};
	}
	public static void doSomething(Runnable r) { r.run(); }
	public static void doSomething(Task a) { a.execute(); }
}

interface Task {
	public void execute();
}


