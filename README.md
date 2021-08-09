# Java 8 in Action 책 정리

##### 프레디케이트란?
```JAVA
public interface Predicate<T> {
    boolean test(T t);
}
/** 설명
Apple::isGreenApple 메서드를 (Predicate<Apple> 파라미터를 인수로 받는) filterApples로 넘겨주었다. 수학에서는 인수로 값을 받아 true 나 false 를 반환하는 함수를 프레디케이트(Predicate) 라고 한다. 자바 8에서도 Function<Apple, Boolean> 같이 코드를 구현할 수 있지만 Predicate<Apple>을 사용하는 것이 더 표준적인 방식이다. (또한 boolean을 Boolean으로 변환하는 과정이 없으므로 더 효율적이기도 하다.)
*/
```

##### 스트림API와 컬렉션API의 차이점
- 컬렉션 API : 
	- 반복 과정을 직접 처리한다. 즉 for-each 루프를 이용해서 각 요소를 반복하면서 작업을 수행한다. 이런 방식의 반복을 외부반복(external iteration)이라고 한다.
	- 컬렉션은 어떻게 데이터를 저장하고 접근할지에 중점을 둔다.
	- 컬렉션의 경우 병렬처리하기 위해서는 컬렉션을 스트림으로 바꾸고, 병렬처리 후, 리스트로 다시 복원한다.

```JAVA
// 순차처리 방식
import static java.util.strem.Collectors.toList;
List<Apple> heavyApples = inventory.stream().filter((Apple a) -> a.getWeight() > 150).collect(toList());
```

- 스트림 API : 
	- 루프를 신경쓸 필요가 없다. 라이브러리 내부에서 모든 데이터가 처리된다. 이와 같은 반복을 내부반복(internal iteration)이라고 한다.
	- 데이터에 어떤 계산을 할 것인지 묘사하는 것에 중점을 둔다.
	- 스트림 내의 요소를 쉽게 병렬로 처리할 수 있는 환경을 제공한다는 것이 핵심이다.

```JAVA
// 병렬처리 방식
import static java.util.stream.Collectors.toList;
List<Apple> heavyApples = inventory.parallelStream().filter((Apple a) -> a.getWeight() > 150).collect(toList());
```

##### 디폴트 메서드
더 쉽게 변화할 수 있는 인터페이스를 만들수 있도록 디폴트 메서드가 추가되었다.
기존의 구현을 고치지 않고도 이미 공개된 인터페이스를 변경할 수 있을까??

```JAVA
default void sort (Comparator<? super E> c) {
	Collections.sort(this, c);
}
```

##### 동적파라미터화
동적 파라미터화란 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블록을 의미한다. 이 코드 블록은 나중에 프로그램에서 호출한다.

동적 파라미터에서 유연한 코드를 작성하는 방법은 조건에 값을 직접 넣지 말고 변수로 받아서 변수를 넣는 게 조금 더 유연한 코드를 작성할 수 있다.

```JAVA
// 예제
// 이 경우 색상을 다르게 변경 해야 할 상황이 발생하게 되면 재사용성이 어렵다.
public List<Apple> filterApples (List<Apple> inventory) {
	List<Apple> result = new ArrayList<>();
	for (Apple apple : inventory) {
		if ( "green".equals(apple.getColor() ) {
			result.add(apple);
		}
	}
	return result;
}

// 이런식으로 컬러를 매개변수로 받아서 조건으로 처리하게 되면 재사용성의 범위가 조금 더 커진다.
public List<Apple> filterApples (List<Apple> inventory, String color) {
	List<Apple> result = new ArrayList<>();
	for (Apple apple : inventory) {
		if ( color.equals(apple.getColor() ) {
			result.add(apple);
		}
	}
	return result;
}
```

하지만 이러한 방식으로 설계를 하는 것 조차 잘못된 이유가 소프트웨어 공학의 DRY ( don't repeat yourself [같은 것을 반복하지 마라] ) 원칙을 어기게 된다. 예시로 지금은 사과의 색상만 조건처리하게 되지만 사과의 모양, 무게 등의 다양한 조건이 들어왔을 경우 매개변수를 추가적으로 늘려준다던가 혹은 매개변수만 변경하여 if 조건을 수정하는 등의 코드 형식으로 작성되기 때문에 추상메서드를 사용하여 처리하는 방식이 가장 좋다.

```JAVA
public interface Predicate {
	boolean check (Apple apple);
}

public interface AppleFormatter {
	String accept (Apple a);
}


public class Test implements Predicate, AppleFormatter{ 
	// Predicate 인터페이스를 정의하면서 사과를 선택하는 조건을 캡슐화함 
	//이러한 패턴을 전략 디자인 패턴이라고 한다.

	@Override
	public boolean check(Apple apple) {
		//return apple.getWeight() > 150;
		return ("green".equals(apple.getColor()) && apple.getWeight() > 150);
	}
	
	public static List<Apple> filterApples (List<Apple> inventory, Predicate p) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if (p.check(apple)) {
				result.add(apple);
			}
		}
		return result;
	}
	
	public static void prettyPrintApple (List<Apple> inventory, AppleFormatter format) {
		for(Apple apple : inventory) {
			String output = format.accept(apple);
			System.out.println(output);
		}
	}

	// AppleFormatter 
	@Override
	public String accept(Apple apple) {
		String characteristic = apple.getWeight() > 150 ? "heavy" : "light";
		return "A " + characteristic + " " + apple.getColor() + " apple";
	}
}
```

##### 익명 클래스
익명 클래스는 자바의 지역클래스 (local class) 와 비슷한 개념이다.

##### 람다 문법
1. () -> {} : 파라미터가 없으며 void를 반환한다. 이는 public void run() {} 처럼 바디가 없는 메서드와 같다.
2. () -> "문자열" : 파라미터가 없으며 문자열을 반환한다.
3. () -> { return "문자열"; } : 파라미터가 없으며 명시적으로 return을 이용해서 문자열을 반환한다.
4. (int i) -> { return "i == " + i; } : 흐름제어문

##### 람다 사용처
함수형 인터페이스에서 람다 표현식을 사용할 수 있다. 함수형 인터페이스란 오직 하나의 추상 메서드만 가지고 있는 인터페이스를 말한다. 인터페이스 내에 추상메서드가 하나밖에 없기 때문에 인터페이스를 호출하면서 다이렉트로 추상메서드를 재정의해준다. 함수형 인터페이스가 아닌 경우 추상메서드를 지정해줘야 하기 때문에 람다를 사용 못하는 게 아닐까 생각된다.

##### 함수 디스크립터
아직 모르겠다. 뒤에 나오면 다시 수정해야겠다.

##### @FunctionalInterface 란??
@FunctionalInterface 어노테이션은 함수형 인터페이스임을 가리키는 어노테이션이다. 어노테이션을 선언했지만 실제 함수형 인터페이스가 아니면 컴파일러가 에러를 발생시킨다.

##### 함수형 인터페이스 사용
- Predicate : test(T t) 의 추상메서드를 재정의하며 boolean타입으로 반환한다. Predicate에는 negate, and, or 세 가지 메서드를 제공한다. 
	- neaget : 특정조건이 아닌 것을 반전시킬 때 사용한다.
	- and : 두 람다를 조합할 수 있다. (조건문 ex : a -> a.getWeight() > 150)
	- or : 추가적인 다양한 조건을 만들 수 있다. ( ex : a -> a.getWeight() > 150).or( a -> a.getColor().equals("green"))
- Consumer : 파라미터를 받아서 어떤 동작을 수행하고 싶을 때 사용한다. 반환타입은 void로 반환하지않으며, 예를들어 int값을 받아서 반복문을 통한 동작수행이라던가 그런 동작을 할 떄 사용한다.
- Function : 제네릭 형식의 T를 인수로 받아서 제네릭 형식의 R을 반환하는 apply라는 추상 메서드를 정의한다. 입력을 출력으로 매핑하는 람다를 정의할 때 Function 인터페이스를 활용한다.
	- andThen과 compose의 차이 : 인터페이스A.andThen(인터페이스B); 가 존재할 때 andThen은 대상 값(A)부터 처리 후 B를 처리하여 반환하지만 compose의 경우 매개변수 값(B)를 먼저 처리하고 A를 통한 뒤 반환하게 된다.
	- andThen : 주어진 함수를 먼저 적용한 결과를 다른 함수의 입력으로 전달하는 함수를 반환한다. (ex : f = x -> x +1, g = x -> x *2 가 있을 때 f와 g를 조립해서 숫자를 증가시킨 뒤 결과에 2를 곱하는 h라는 함수를 만들 수 있다.)
	- compose : 인수로 주어진 함수를 먼저 실행한 다음에 그 결과를 외부 함수의 인수로 제공한다. 즉, f.andThen (g) 에서 andThen 대신에 compose를 사용하면 g(f(x))가 아니라 f(g(x)) 라는 수식이 된다.
- Supplier : 매개변수를 받지 않고 단순히 무엇인가를 반환하는 추상메서드
##### 기본형 특화
참조형 : Byte, Integer, Object, List
기본형 : int, char, byte, double
제네릭 파라미터는 참조형만 사용할 수 있다. 그렇기 때문에 자바에서는 기본형을 참조형으로 변환시켜주는 기능을 제공한다 이 기능을 박싱(boxing) 이라고 한다. 또한 참조형을 기본형으로 변환하는 기능인 언박싱(unboxing)도 존재하며 박싱과 언박싱을 자동으로 이루어주는 오토박싱(autoboxing)이라는 기능도 존재한다. 하지만 이런 변화 과정은 비용이 발생한다. 박싱한 값을 기본형에 감싸는 래퍼며 힙에 저장된다. 따라서 박싱한 값은 메모리를 더 소비하여 기본형을 가져올 때도 메모리를 탐색하는 과정이 필요하다. 자바 8에서는 기본형을 입출력으로 사용하는 상황에서 오토박싱 동작을 피할 수 있도록 특별한 버전의 함수형 인터페이스를 제공한다.

##### 람다 형식 검사
람다가 사용되는 context를 이용해서 람다의 type을 추론할 수 있다. 어떤 컨텍스트 (예를 들면 람다가 전달 될 메서드 파라미터나 람다가 할당되는 변수 등) 에서 기대되는 람다 표현식의 형식을 대상형식(target type)이라고 부른다.

```JAVA
List<Apple> heavierThan150g = filter( inventory, (Apple a) -> a.getWeight() > 150 );

1. filter 메서드의 선언을 확인한다.
2. filter 메서드는 두 번째 파라미터로 Predicate<Apple> 형식을 기대한다.
3. Predicate<Apple>은 test라는 한 개의 추상 메서드를 정의하는 함수형 인터페이스이다.
4. test 메서드는 Apple을 받아 boolean을 반환하는 함수 디스크립터를 묘사한다.
5. filter 메서드로 전달된 인수는 이와 같은 요구사항을 만족해야 한다.
```

##### 형식 추론
다이아몬드 연산자를 통한 타입을 지정했을 경우 파라미터에 타입을 지정하지 않는다.

```JAVA
// 형식을 추론하지 않음.
Comparrator<Apple> c = (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()); 

// 형식을 추론함.
Comparrator<Apple> c = (a1, a2) -> a1.getWeight().compareTo(a2.getWeight()); 
```

##### 지역변수 사용
지금까지 람다는 파라미터로 넘겨온 변수만 사용했지만 람다 밖에서 정의된 변수를 사용할 수 있다. 하지만 약간의 제약이 있다. 람다 외부 변수를 사용하기 위해선 사용할 변수가 final로 선언 되거나 final같은 기능으로 사용해야한다. 람다에서 사용 후 사용된 변수를 다른 곳에서 재정의 하게 되면 에러가 발생한다.

```JAVA

// 정상 출력
int portNumber = 3306;
		Runnable r = () -> {
			System.out.println(portNumber);
		};

// 에러
int portNumber = 3306;
		Runnable r = () -> {
			System.out.println(portNumber);
		};
		portNumber = 13306;
```

##### 지역변수 제약
인스턴스 변수는 힙에 저장되는 반면 지역변수는 스택에 위치한다. 람다에서 지역변수에 바로 접근할 수 있다는 가정하에 람다가 스레드에서 실행된다면 변수를 할당한 스레드가 사라져서 변수 할당이 해제되었는데도 람다를 실행하는 스레드에서는 해당 변수에 접근하려 할 수 있다. 따라서 자바 구현에서는 원래 변수에 접근을 허용하는 것이 아니라 자유 지역변수의 복사본을 제공한다. 따라서 복사본의 값이 바뀌지 않아야 하므로 제약이 생긴 것이다.

##### 메서드 레퍼런스
메서드 레퍼런스란 기존의 메서드를 재정의하여 람다처럼 사용할 수 있다. 메서드 레퍼런스는 메서드명 앞에 구분자 (::)를 붙이는 방식으로 메서드 레퍼런스를 활용할 수 있다. 예를 들어 Apple::getWeight는 람다표현식 (Apple a) -> a.getWeight()를 축약한 것이다. 메서드 레퍼런스는 메서드를 직접 호출하는 것이 아니므로 괄호가 필요없다.

```JAVA
// 람다						메서드 레퍼런스
(Apple a) -> a.getWeight();			Apple::getWeight
() -> Thread.currentThread().dumpStack()	Thread.currentThread()::dumpStack
(str, i) -> str.substring(i)			String::substring
(String s) -> System.out.println(s)		System.out::println
```

##### 메서드 레퍼런스 만드는 방법
메서드 레퍼런스는 세 가지 유형으로 구분할 수 있다.
1. 정적 메서드 레퍼런스
- 예를 들어 Integer 의 parseInt 메서드는 Integer::parseInte로 표현할 수 있다.

2. 다양한 형식의 인스턴스 메서드 레퍼런스
- 예를 들어 String의 length 메서드는 String::length로 표현할 수 있다.
ex ) (String s) -> s.toUpperCase()	==	String::toUpperCase

3. 기존 객체의 인스턴스 메서드 레퍼런스
- 예를 들어 Transaction 객체를 할당받은 expensiveTransaction 지역 변수가 있고, Transaction 객체에는 getValue 메서드가 있다면, 이를 expensiveTransaction::getValue 라고 표현할 수 있다.
ex ) Transaction expensiveTransaction = new Transaction();
expensiveTransaction.getValue();		==	expensiveTransaction::getValue

##### 생성자 레퍼런스
ClassName::new 를 이용하여 클래스의 기본생성자의 레퍼런스를 만들 수 있다.
ex) Supplier<Apple> c1 = new Apple<>();	==	Supplier<Apple> c1 = Apple::new;

문제 : Color(int, int, int) 처럼 인수가 세 개인 생성자의 생성자 레퍼런스를 사용하려면 어떻게 해야 할까??
답 : 생성자 레퍼런스 문법은 ClassName::new이므로 Color 생성자의 레퍼런스는 Color::new가 된다. 하지만 이를 사용하려면 생성자 레퍼런스와 일치하는 시그니처를 갖는 함수형 인터페이스가 필요하다. 현재 이런 시그니처를 갖는 함수형 인터페이스는 제공되지 않으므로 우리가 직접 만들어야 한다.
public interface TriFunction<T, U, V, R> {
	R apple(T t, U u, V v);
}
TriFunction<Integer, Integer, Integer, Color> colorFactory = Color::new;

##### Comparator
- comparing : Function함수를 재정의하여 메서드 레퍼런스를 사용할 수 있다.
- reversed() : 정렬 값을 내림차순으로 할 때 사용한다.
- thenComparing : 첫번째 정렬 값이 동일 할 때 두번 째 정렬 값을 지정한다.

##### 스트림이란?
스트림이란 자바 API에 새로 추가된 기능으로, 스트림을 이용하면 선언형(즉, 데이터를 처리하는 임시 구현 코드 대신 질의로 표현할 수 있다.)으로 컬렉션 데이터를 처리할 수 있다. 또한 스트림을 이용하면 멀티 스레드 코드를 구현하지 않아도 데이터를 투명하게 병렬로 처리할 수 있다.

##### 스트림과 컬렉션의 차이
먼저 컬렉션은 DVD와 같다. 영화가 있다고 치면 영화를 처음부터 끝까지 다운받은 상태가 컬렉션이다. 즉 데이터를 전부 가지고 있는 상태에서 그것을 요청에 맞게 처리한다. 반면 스트림은 스트리밍과 비슷하다. 영화로 치면 네트워크에서 영화를 전체 다운받아서 실행시키는 것이 아니라 앞의 5분 앞의 3분 등 필요한 순간순간에 데이터를 조금씩 받아서 실행한다. 즉 요청이 들어와야 데이터 처리를 시작한다. 이렇게 보면 컬렉션이 좋아보일 수 있지만 스트림의 장점은 필요한 데이터만 바로바로 가져오기 때문에 데이터를 완성시키고 처리하는 컬렉션보다 훨씬 빠르다. 다만 단점은 스트림은 1회용이라고 생각하면 편하다. 스트림으로 정의하고 두번 호출하게 되면 IllegalStateException이 발생한다.

##### 외부 반복과 내부 반복
외부반복이란 사용자가 직접 반복해야 되는 것을 말한다 (for-each 등을 사용하여). 반면 스트림 라이브러리는 내장함수에서 반복을 알아서 처리하고 저장해주는 내부 반복을 사용한다.

```JAVA
// 외부 반복
		List<String> names = new ArrayList<>();
		for(Dish d : menu) {
			names.add(d.getName());
		}
		
		// 내부적으로 숨겨졌던 반복자 (iterator)를 통한 외부반복
		List<String> names2 = new ArrayList<>();
		Iterator<Dish> iterator = menu.iterator();
		while (iterator.hasNext()) {
			Dish d = iterator.next();
			names2.add(d.getName());
		}
		
		// 내부반복
		List<String> names3 = menu.stream().map(Dish::getName).collect(Collectors.toList());
```

##### 중간연산
filter 나 sorted 같은 중간 연산은 다른 스트림을 반환한다. 따라서 여러 중간연산을 연결해서 질의를 만들 수 있다.

##### 최종연산
최종연산은 스트림 파이프라인에서(중간연산들) 결과를 도출한다 보통 최종연산에 의해 List, Integer, void 등 스트림 이외의 결과가 반환된다.