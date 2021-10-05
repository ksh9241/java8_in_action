package java_8_in_action.functionalprograming;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class CustomLazyList {
	public static void main(String[] args) {
		/** 기본적인 연결 리스트 */
		MyList<Integer> l = new MyLinkedList<>(5, new MyLinkedList<>(10, new Empty<>()));
		System.out.println(l.head());
		System.out.println(l.tail().head());
		
		/** 게으른 리스트 */
		LazyList<Integer> numbers = from(2);
		int two = numbers.head();
		int three = numbers.tail().head();
		int four = numbers.tail().tail().head();
		
		System.out.println(two + " " + three + " " + four);
		
		LazyList<Integer> prime = from(2);
		int t = primes(prime).head();
		int th = primes(prime).tail().head();
		int f = primes(prime).tail().tail().head();
		
		System.out.println(t + " " + th + " " + f);
		
		//printAll(primes(from(2)));
		printAllRecursion(primes(from(2)));
	}
	static LazyList<Integer> from(int n) {
		return new LazyList<Integer>(n, () -> from(n + 1));
	}
	
	/** 게으른 리스트로 PrimeNumber 알고리즘 만들기 */
	static MyList<Integer> primes (MyList<Integer> numbers) {
		return new LazyList (numbers.head(),
				() -> primes( numbers.tail().filter (n -> n % numbers.head() != 0 )));
	}
	
	// 메서드를 무한으로 실행한다.
	static <T> void printAll(MyList<T> list) {
		while (!list.isEmpty()) {
			System.out.println(list.head());
			list = list.tail();
		}
	}
	
	// 재귀로 문제 해결하기
	// 아래의 코드를 사용하면 무한실행은 되지않지만 꼬리 호출 제거를 지원하지 않기 때문에 스택 오버플로우가 발생한다.
	static <T> void printAllRecursion (MyList<T> list) {
		if (list.isEmpty()) return;
		System.out.println(list.head());
		printAllRecursion(list.tail());
	}
}





interface MyList<T> {
	T head();
	
	MyList<T> tail();
	
	default boolean isEmpty() {
		return true;
	}
	
	default MyList<T> filter (Predicate<T> p) {
		return isEmpty () ? this : p.test(head()) ? new LazyList<T>(head(), () -> tail().filter(p)) : tail().filter(p);
	}
}

/** 게으른 리스트 
 *  Supplier 의 get 메서드를 호출하면 (마치 팩토리로 새로운 객체를 생성하듯이 ) LazyList의 노드가 만들어진다.
 * */
class LazyList<T> implements MyList<T> {
	T head;
	Supplier<MyList<T>> tail;
	public LazyList (T head, Supplier<MyList<T>> tail) {
		this.head = head;
		this.tail = tail;
	}
	
	@Override
	public T head() {
		return head;
	}

	@Override
	public MyList<T> tail() {
		return tail.get();
	}
	
	public boolean isEmpty () {
		return false;
	}
}


/** 기본적인 연결 리스트 */
class MyLinkedList<T> implements MyList<T> {
	private T head;
	private MyList<T> tail;
	public MyLinkedList (T head, MyList<T> tail) {
		this.head = head;
		this.tail = tail;
	}
	
	@Override
	public T head() {
		return head;
	}

	@Override
	public MyList<T> tail() {
		return tail;
	}
	
	public boolean isEmpty() {
		return false;
	}
}

class Empty<T> implements MyList {

	@Override
	public Object head() {
		throw new UnsupportedOperationException();
	}

	@Override
	public MyList tail() {
		throw new UnsupportedOperationException();
	}
	
}