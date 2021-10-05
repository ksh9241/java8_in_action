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

##### 스트림 중간연산
- filter : 조건을 처리하는 if문으로 볼 수 있다.
- distinct : DB에 존재하는 중복값 제거랑 같은 의미를 가지고 있다.
- forEach : 스트림 값을 반복하여 출력한다. 반환타입은 void 이다.
- limit : 출력할 값의 개수를 지정할 수 있다.
- skip : 입력값의 개수만큼 건너뛰고 데이터를 처리한다.

##### flatMap
flatMap은 각 배열을 스트림이 아니라 스트림의 콘텐츠로 매핑한다. 즉, map(Arrays::stream)과 달리 flatMap은 하나의 평면화된 스트림을 반환한다. 요약하자면 flatMap 메서드는 스트림의 각 값을 다른 스트림으로 만든 다음에 모든 스트림을 하나의 스트림으로 연결하는 기능을 수행한다.

##### 검색과 매칭
특정 속성이 데이터 집합에 있는지 여부를 검색하는 데이터 처리도 자주 사용된다. 스트림 API는 allMatch, anyMatch, noneMatch, findFirst, findAny 등 다양한 유틸리티 메서드를 제공한다.
- 스트림 쇼트서킷 기법 (자바의 &&, ||와 같은 연산을 활용함.)
	- anyMatch : 스트림 요소 중 일치하는 조건이 하나라도 있을 경우 true를 반환
	- allMatch : 스트림 요소 전체가 조건과 일치해야 true를 반환
	- noneMatch : 모든 요소들이 주어진 조건을 만족하지 않는지 검사 (책을 읽고 검색을해도 이해가 완전히 되지않음.)

##### Optional이란 ?
Optional<T> 클래스는 값의 존재나 부재 여부를 표현하는 컨테이너 클래스이다.
- isPresent() 는 Optional이 값을 포함하면 true를, 포함하지 않으면 false를 반환한ㄷ.
- get() 값이 존재하면 값을 반환하고 없으면 NoSuchElementException을 반환한다.
- orElse는 값이 있으면 값을 반환하고 값이 없으면 기본값을 반환한다.

##### 리듀싱
리듀싱이란 모든 스트림 요소를 처리해서 값으로 도출하는 것. 함수형 프로그래밍 용어로는 이 과정이 마치 종이를 작은 조각이 될 때까지 반복해서 접는 것과 비슷하다고 하여 폴드라고 부른다.
리듀스는 T타입의 초기값과 BinaryOperator<T>를 인자값으로 받는다. T타입과 BinaryOperator의 요청 (+, *, / , -, ...) 을 처리하여 반환한다.
- 초깃값 없음
	- 초기값을 받지않도록 오버로드된 Reduce도 있다. 그러나 이 reduce는 Optional 객체를 반환한다.

리듀스 장점 : 리듀스를 이용하면 내부 반복이 추상화되면서 내부 구현에서 병렬로 reduce를 실행할 수 있게 된다. forEach등 반복적인 합계에서는 sum 변수를 공유해야 하므로 쉽게 병렬화하기 어렵다.

##### 기본형 특화 스트림
아래의 3가지 기본형 특화 스트림은 박싱비용을 피할수있도록 만든 스트림이다. 아래의 3가지 스트림은 sum, max, min 등 숫자관련 연산 메서드를 제공한다.
- IntStream
- DoubleStream
- LongStream

##### iterate 와 generate의 차이
- iterate : 내부 로직에서 처리되는 로직의 값이 새로 생성되고 기존에 있던 값은 쓰지 않는 불변 상태를 유지한다.
- generate : 내부 로직에서 쓰던 객체의 값을 변경하여 사용하는 가변 상태의 객체이다.
스트림을 병렬 처리하면서 올바른 결과를 얻으려면 불변 상태 기법을 고수해야한다.

##### 요약연산
Collectors 클래스는 Collectors.summingInt 라는 특별한 요약 팩토리 메서드를 제공한다. summingInt는 객체를 int로 매핑하는 함수를 인수로 받는다. summingInt의 인수로 전달된 함수는 객체를 int로 매핑한 컬렉터를 반환한다.
- 값을 합산하는 API
	- summingInt
	- summingLong
	- summingDouble

- 값의 평균을 연산하는 API
	- averagingInt
	- averaginLong
	- averagingDouble

- 합계, 평균, 최대값, 카운트 중 2개 이상을 사용할 때
	- IntSummaryStatistics
	- LongSummaryStatistics
	- DoubleSummaryStatistics

##### 범용 리듀싱

```JAVA
int totalCalories2 = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (i, j) -> i + j));
		System.out.println(totalCalories2);
```
- 첫 번째 인수는 리듀싱 연산의 시작값이거나 스트림에 인수가 없을 때는 반환값이다.
- 두 번째 인수는 객체를 인수로 변환할 때 사용한 변환 함수이다.
- 세 번째 인수는 같은 종류의 두 항목을 하나의 값으로 더하는 BinaryOperator이다.

##### Collect(reducing) 와 reduce의 차이
Collect(reducing) 는 가변형 연산
reduce는 불변형 연산
reduce는 불변형 연산이기 때문에 값이 변할 때마다 새로운 객체에 값을 할당해줘야 한다. 이는 성능저하와 동시에 병렬로 수행할 수 없는 단점도 가지고 있다.

##### 그룹화
데이터 집합을 하나 이상의 특성으로 분류해서 그롭화하는 연산도 데이터베이스에서많이 수행 되는 작업이다. 자바 8 에서는 함수형을 이용하여 한줄로 그룹화를 구현할 수 있다.

- groupingBy : Collectors의 메서드로 매개변수의 값을 통해 그룹으로 나누어 리스트로 반환한다.

##### 분할
분할은 Partitioning function 이라 불리는 프레디케이트를 분류 함수로 사용하는 특수한 그룹화 기능이다.
분할 함수는 불린을 반환하므로 map의 key는 boolean 타입이고 최대 두 개의 그룹으로 분류된다.

##### 분할의 장점
분할함수의 장점은 조건의 해당하는 것, 해당하지 않는 것을 모두 정리하여 반환하는 것이 장점이다.

##### Collector 인터페이스
- Supplier<A> supplier()

	- Supplier 메서드는 빈 결과로 이루어진 Supplier를 반환한다. 즉 supplier는 수집 과정에서 빈 누적자 인스턴스를 만드는 파라미터가 없는 함수이다. ToListCollector처럼 누적자를 반환하는 컬렉터에서는 빈 누적자가 비어있는 스트림의 수집 과정의 결과가 될 수 있다. (수집 과정이란 스트림이 실행되는 중간연산 단계를 말한다. 즉 위 내용은 중간연산에서 어떠한 값도 담지 않으면 supplier의 누적자가 반환되어 최종 결과값이 될 수 있다는 말이다.)

```JAVA
public Supplier <List<T>> supplier() {
	return () -> new ArrayList<T>();
}

// 생성자 레퍼런스를 전달하는 방법
public Supplier <List<T>> supplier() {
	return ArrayList::new;
}
```

- BiConsumer<A, T> accumulator()

	- accumulator 메서드는 리듀싱 연산을 수행하는 함수를 반환한다. 누적자(A) 와 n번째 요소를 함수에 적용한다. 함수의 반환값은 void 이며 매개변수로 들어온 누적자 (A)에 값을 처리한다.

```JAVA
public BiConsumer<List<T>, T> accumulator() {
	return (list, item) -> list.add(item);
}

// 메서드 레퍼런스 사용
public BiConsumer<List<T>, T> accumulator() {
	return List::add;
}
```

- Function<A, R> finisher()

	- finisher 메서드는 스트림 탐색을 끝내고 누적자 객체를 최종 결과로 변환하면서 누적 과정을 끝낼 때 호출할 함수를 반환해야 한다. 누적자 (A) 객체가 이미 최종 결과인 상황도 있는데 이럴 때는 변환 과정이 필요하지 않으므로 finisher 메서드는 항등 함수를 반환한다.

```JAVA
public Function<List<T>, List<T>> finisher() {
	return Function.identity();
}
```

- BinaryOperator<List<T>> combiner()

	- 리듀싱 연산에서 사용할 함수를 반환하는 combiner을 살펴보자. combiner은 스트림의 서로 다른 서브파트를 병렬로 처리할 때 누적자가 이 결과를 어떻게 처리할지 정의한다. toList의 combiner은 비교적 쉽게 구현할 수 있다. 즉 스트림의 두 번째 서브파트에서 수집한 항목 리스트를 첫 번째 서브파트 결과 리스트 뒤에 추가한다.

```JAVA
public BinaryOperator<List<T>> combiner() {
	return (list1, list2) -> {
		list1.addAll(list2);
		return list1;
	}
}
```

- Set<Characteristics> characteristics()

	- characteristics 메서드는 컬렉터의 연산을 정의하는 Characteristics 형식의 불변 집합을 반환한다. Characteristics는 다음 세 항목을 포함하는 열거형이다.
		
		- UNORDERED : 리듀싱 결과는 스트림 요소의 방문 순서나 누적 순서에 영향을 받지 않는다.
		- CONCURRENT : 다중스레드에서 accumulator 함수를 동시에 호출할 수 있고, 병렬 리듀싱을 수행할 수 있다. 하지만 UNORDERED를 함께 설정하지 않으면 정렬되어 있지 않은 상황에서만 병렬 리듀싱을 수행할 수 있다.
		- IDENTITY_FINISH : 리듀싱 과정의 최종 결과로 누적자 객체를 바로 사용할 수 있다. 또한 누적자 A를 결과 R로 안전하게 형변환 할 수 있다.

##### 병렬스트림
컬렉션에 ParallelStream을 호출하면 병렬 스트림이 생성된다.
병렬 처리에서 중요한 부분은 iterate를 사용하지 말고 LongStream.rangeClosed를 사용해야 한다.
병렬 처리는 올바른 자료구조를 선택하여 코드를 작성해야 순차 (자바 반복문)실행 보다 빠른 속도로 처리된다.
병렬처리는 멀티코어 간의 데이터 이동도 많고 재귀, 서브스트림 리듀싱 등 비용이 비싸다.
적절한 상황에서 사용하는 것이 바람직하다.

##### LongStream.rangeClosed 장점
- LongStream.rangeClosed는 기본형 long을 직접 사용하므로 박싱과 언박싱 오버헤드가 사라진다. (박싱 언박싱 작업이 필요없다는 말)
- LongStream.rangeClosed는 쉽게 청크로 분할할 수 있는 숫자 범위를 생성한다. 예를 들어 1 - 20 범위의 숫자를 각각 1 - 5, 6 - 10, 11- 15, 16 - 20 범위의 숫자로 분할할 수 있다.

##### 병렬 스트림 주의 사항

```JAVA
public static long sideEffectSum (long n) {
	Accumulator accumulator = new Accumulator();
	LongStream.rangeClosed (1, n).forEach (accumulator::add);
	return accumulator.total;
}

public class Accumulator {
	public long total = 0;
	public void add (long value) { total += value; }
}
```

위의 코드를 그대로 사용 할 경우 데이터 레이스의 문제가 일어난다. total의 값이 불변객체여야 하는데 값이 자꾸 바뀌어 버리는 형태로 병렬처리가 진행되면 데이터 값이 정확하지 않는다.

##### RecursiveTask 활용
스레드 풀을 이용하려면 RecursiveTask<R> 의 서브클래스를 만들어야 한다. 여기서 R은 병렬화된 태스크가 생성하는 결과 형식 또는 결과가 없을 때는 RecursiveAction 형식이다. RecursiveTask를 정의하려면 추상 메서드 compute를 구현해야 한다.
- compute 메서드 : 태크스를 서브태스크로 분할하는 로직과 더 이상 분할할 수 없을 때 개별 서브태스크의 결과를 생산할 알고리즘을 정의한다.

```JAVA
if (태스크가 충분히 작거나 더 이상 분할할 수 없으면) {
	순차적으로 태스크 계산
} else {
	태스크를 두 서브태스크로 분할 (재귀로 조건 충족할 때까지 호출)
}
``` 
분할 후 정복 (divide-and-conquer) 알고리즘 병렬화 버전이다.

일반적으로 애플리케이션에서는 둘 이상의 ForkJoinPool을 사용하지 않는다. 즉 소프트웨어의 필요한 곳에서 언제든 가져다 쓸 수 있도록 ForkJoinPool을 한번만 인스턴스화해서 정적필드에 싱글턴으로 저장한다.

##### 포크/조인 프레임워크를 제대로 사용하는 방법
- join 메서드는 두 서브태스크가 모두 시작된 다음에 사용해야 한다. join 메서드를 먼저 호출하면 태스크가 생산하는 결과가 준비될 때까지 호출자를 블록시킨다.
- RecursiveTask 내에서는 ForkJoinPool의 invoke 메서드를 사용하지 말고 compute 나 fork메서드를 사용해라. 순차 코드에서 병렬 계산을 시작할 때만 invoke를 사용한다.
- 서브태스크를 나눌 때 좌, 우 모두 fork 메서드를 호출하는 것보다 fork와 compute를 호출하는 것이 효율적이다. 두 서브태스크가 한 태스크에는 같은 스레드를 재사용할 수 있으므로 불필요한 태스크를 할당하는 오버헤드를 피할 수 있다.

##### 작업 훔치기
ForkJoinPool의 모든 스레드를 거의 공정하게 분할한다. 각각의 스레드는 자신에게 할당된 태스크를 포함하는 이중연결 리스트를 참조하면서 작업이 끝날 때 마다 큐의 헤드에서 다른 태스크를 가져와서 작업을 처리한다. 방법은 작업이 끝난 스레드가 작업중인 스레드 큐의 끝 (꼬리) 에서 작업을 훔쳐온다.

##### Spliterator
Spliterator은 분할할 수 있는 반복자 라는 의미이다. Iterator처럼 Spliterator는 소스의 요소 탐색 기능을 제공한다는 점은 같지만 Spliterator는 병렬 작업에 특화되어 있다.

```JAVA
// Spliterator Interface
public interface Spliterator<T> {
	boolean tryAdvance (Consumer<? super T> action);
	Spliterator<T> trySplit();
	long estimateSize();
	int characteristics();
}

/*
여기서 T는 Spliterator에서 탐색하는 요소의 형식 (타입)을 가리킨다.
- tryAdvance : Spliterator의 요소를 하나씩 순차적으로 소비하면서 탐색해야 할 요소가 남아있으면 참, 없으면 거짓을 반환한다. (일반적인 Iterator 동작과 같다.)
- trySplit : Spliterator의 일부 요소(자신이 반환한 요소)를 분할해서 두 번째 Spliterator를 생성하는 메서드이다.
- estimateSize : 탐색해야 할 요소 수 정보를 제공할 수 있다.
- characteristics : Spliterator 자체의 특성 집합을 포함하는 int를 반환한다.
*/
```

##### Spliterator 추상 메서드
- tryAdvance : 문자열에서 현재 인덱스에 해당하는 문자를 Consumer에 제공한다음에 인덱스를 증가시킨다.

- trySplit : 반복될 자료구조를 분할하는 로직을 포함하므로 Spliterator에서 가장 중요한 메서드이다. 우선 분할 동작을 중단할 한계를 설정해야 한다. 주의할 점은 너무 많은 태스크(분할)을 만들지 않도록 신경써야한다. 반대로 분할이 필요한 상황에는 파싱해야 할 문자열 청크의 중간 위치를 기준으로 분할하도록 지시한다. 이 때 단어의 중간이 length의 가운데일 경우 빈 문자가 나올 때까지 분할 위치를 이동시킨다. 분할할 위치를 찾았으면 새로운 Spliterator를 만들고 현재위치부터 분할된 위치까지의 문자를 탐색한다.

- estimatedSize : 탐색해야 할 요소의 개수는 Spliterator가 파싱할 문자열 전체 길이와 현재 반복중인 위치의 차(마이너스)다.

- characteristics : 프레임워크에 Spliterator가 
	- ORDERED : 문자열의 문자 등장 순서가 유의미함.
	- SIZED : estimatedSize 메서드의 반환값이 정확함.
	- SUBSIZED : trySplit으로 생성된 Spliterator도 정확한 크기를 가짐.
	- NONNULL : 문자열에는 null 문자가 존재하지 않음.
	- IMMUTABLE : 문자열 자체가 불변 클래스이므로 문자열을 파싱하면서 속성이 추가되지 않음.
 등의 특성임을 알려준다.

### 8장. 리팩토링, 테스팅, 디버깅

##### 가독성과 유연성을 개선하는 리팩토링
- 가독성이란 ? : 추상적인 표현이라 정확하게 정의하긴 어렵지만, 다른사람도 쉽게 이해할 수 있음을 의미한다. 코드 가독성을 높이려면 코드의 문서화를 잘하고, 표준 코딩 규칙을 준수하는 등 노력을 기울여야 한다.

##### 익명클래스를 람다 표현식으로 리팩토링하기
익명클래스를 람다표현식으로 리팩토링 하는 이유 : 익명클래스는 코드를 장황하게 만들고 쉽게 에러를 일으킨다. 람다 표현식을 이용해서 간결하고, 가독성이 좋은 코드로 구현할 수 있다.

```JAVA
// 익명 클래스를 이용한 코드
Runnable r1 = new Runnable() {
	public void run() {
		System.out.println("Hello");
	}
}

// 람다 표현식을 사용한 코드
Runnable r2 = () -> System.out.println("Hello");
```

하지만 모든 익명 클래스를 람다 표현식으로 변환할 수 있는 것은 아니다.
1. 익명 클래스에서 사용한 this와 super는 람다표현식에서 다른 의미를 갖는다.
- 익명클래스에서 this는 익명 클래스 자신을 가리키지만 람다에서 this는 람다를 감싸는 클래스를 가리킨다.

2. 익명클래스는 감싸고 있는 클래스의 변수를 가릴 수 있다. (섀도우 변수). 하지만 다음 코드에서 보여주는 것처럼 람다 표현식으로는 변수를 가릴 수 없다. (아래 코드는 컴파일되지 않는다.)

```JAVA
int a = 10;
// 컴파일 에러
Runnable r1 = () -> {
	int a = 2;
	System.out.println(a);
}

// 정상 작동한다.
Runnable r2 = new Runnable() {
	public void run() {
		int a = 2;
		System.out.println(a);
	}
}
```
마지막으로 익명 클래스를 람다 표현식으로 바꾸면 콘텍스트 오버로딩에 따른 모호함이 초래될 수 있다. 익명 클래스는 인스턴스화할 때 명시적으로 형식이 정해지는 반면 람다의 형식은 콘텍스트에 따라 달라지기 때문이다.

##### 람다표현식을 메서드 레퍼런스로 리팩토링하기
람다 표현식은 쉽게 전달할 수 있는 짧은 코드다. 하지만 람다표현식 대신 메서드 레퍼런스를 이용하면 가독성을 높일 수 있다.

```JAVA
// 람다 표현식으로 칼로리 리스트 그룹화
Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream()
						.collect(
							groupingBy(dish -> {
								if (dish.getCalories() <= 400) return CaloricLevel.DIET;
								else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
								else return CaloricLevel.FAT;
						}));

// 메서드 레퍼런스 사용
Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = 
					menu.stream().collect(groupingBy(Dish::getCaloricLevel));

class Dish {
	public CaloricLevel getCaloricLevel() {
		if (this.getCalories() <= 400) return CaloricLevel.DIET;
		else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
		else return CaloricLevel.FAT;
	}
}
```

##### 명령형 데이터 처리를 스트림으로 리팩토링하기
이론적으로는 반복자를 이용한 기존의 모든 컬렉션 처리 코드를 스트림 API로 바꿔야 한다. 이유는 스트림 API는 데이터 처리 파이프라인의 의도를 더 명확하게 보여준다. 명령형 코드는 두 가지 패턴(필터링과 추출)으로 엉킨 코드이다. 이 코드의 전체 구현을 자세히 살펴본 이후에야 전체 코드의 의도를 이해할 수 있다. 또한 병렬처리하기 매우 어렵다.

```JAVA
// 명령형 코드
List<String> dishNames = new ArrayList<>();
for (Dish dish : menu) {
	if (dish.getCalories() > 300) {
		dishNames.add(dish.getName());
	}
}

// 스트림
menu.parallelStream()
	.filter(d -> d.getCalories() > 300)
	.map(Dish::getName)
	.collect(toList());
```

##### 함수형 인터페이스 적용
먼저 람다 표현식을 이용하려면 함수형 인터페이스가 필요하다. 따라서 함수형 인터페이스를 코드에 추가해야 한다.

##### 조건부 연기 실행
실제 작업을 처리하는 코드 내부에서 제어 흐름문이 복잡하게 얽힌 코드를 흔히 볼 수 있다. 흔히 보안검사나 로깅 관련 코드가 이처럼 사용된다.

```JAVA
// 아래 코드의 문제점
// logger의 상태가 isLoggable이라는 메서드에 의해 클라이언트 코드로 노출됨.
// 메시지를 로깅할 때마다 logger 객체의 상태를 매번 확인해야 함.
if (logger.isLoggable(Log.FINER)) {
	logger.finer("Problem: " + generateDiagnostic());
}

// 수정
public static void log(Level level, Supplier<String> msgSupplier) {
		if (logger.isLoggable(level)) {
			log(level, msgSupplier.get()); //람다 실행
		}
	}

logger.log(Level.FINER, "Problem: "+ generateDiagnostic());
```

##### 실행 어라운드
실행 어라운드 패턴이란 매번 같은 준비, 종료 과정을 반복적으로 수행하는 것을 말한다. 이러한 코드가 있다면 이를 람다로 변환할 수 있다. 종료 과정을 처리하는 로직을 재사용함으로써 코드 중복을 줄일 수 있다.

```JAVA
String oneLine = processFile((BufferedReader b) -> b.readLine()); // 람다 전달
String twoLine = processFile((BufferedReader b) -> b.readLine() + b.readLine()); // 다른 람다 전달

public static String processFile(BufferedReaderProcesser p) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader("java8inaction/chap8/data.txt"))) {
			return p.process(br); // 인수로 전달된 BufferedReaderProcessor 를 실행
		}
	}
	
	public interface bufferedReaderProcessor { // IOException을 던질 수 있는 람다의 함수형 인터페이스
		String process(BufferedReader b) throws IOException;
	}
```

##### 람다를 이용한 디자인 패턴
- 전략
전략 패턴은 한 유형의 알고리즘을 보유한 상태에서 런타임에 적절한 알고리즘을 선택하는 기법이다. 다양한 프레디케이트로 목록을 필터링 (예를 들어 무거운사과, 초록사과 )하는 방법이 전략 패턴의 예시이다. 다양한 기준을 갖는 입력값을 검증하거나, 다양한 파싱 방법을 사용하거나, 입력 형식을 설정하는 등 다양한 시나리오에 전략 패턴을 활용할 수 있다.
	- 람다 표현식 사용
	validationStrategy는 함수형 인터페이스이며 Predicate<String> 과 같은 함수 디스크립터를 가지고 있다. 따라서 다양한 전략을 구현하는 새로운 클래스를 구현할 필요 없이 람다 표현식을 직접 전달하면 코드가 간결해진다.

- 템플릿 메서드
알고리즘의 개요를 제시한 다음에 알고리즘의 일부를 고칠 수 있는 유연함을 제공해야 할 때 템플릿 메서드 디자인 패턴을 사용한다. 쉽게 말해 '이 알고리즘을 사용하고 싶은데 그대로는 안되고 조금 고쳐야 하는' 상황에 적합하다.

```JAVA
public class TemplateMethodPattern {
	public static void main(String[] args) {
		new OnlineBankingLambda().processCustomer(1337, (Customer c) -> System.out.println("Hello "+c.getName()));
	}
}

abstract class OnlineBanking {
	public void processCustomer ( int id) {
		Customer c = Database.getCustomerWithId(id);
		makeCustomerHappy(c);
	}
	
	abstract void makeCustomerHappy (Customer c);
	
	// 람다 표현식 사용 하기 위한 메서드 생성
	public void processCustomer(int id, Consumer<Customer> makeCustomerHappy) {
		Customer c = Database.getCustomerWithId(id);
		makeCustomerHappy.accept(c);
	}
}
```

- 옵저버
어떤 이벤트가 발생했을 때 한 객체 (주체) 가 다른 객체 (리스트)에 자동으로 알림을 보내야 하는 상황에서 옵저버 디자인 패턴을 사용한다. GUI Application에서 옵저버 패턴이 자주 등장한다.
 * ObserverPattern.java 로직 참조

```JAVA
// 람다로 옵저버 만들기
public class ObserverPattern {
	public static void main (String [] args) {
		Feed f = new Feed();
		
		// 람다로 표현하기
		f.registerObserver((String tweet) -> {
			if (tweet != null && tweet.contains("money")) {
				System.out.println("Breaking news in NY! " + tweet);
			}
		});
		
		f.registerObserver((String tweet) -> {
			if (tweet != null && tweet.contains("queen")) {
				System.out.println("Yet another news in London... " + tweet);
			}
		});
		
		f.notifyObserver("The queen said her favourite book is Java 8 in action");
	}
}

interface Observer {
	void notify(String tweet);
}

interface Subject {
	void registerObserver (Observer o);
	void notifyObserver (String tweet);
}

class Feed implements Subject {
	private final List<Observer> observer = new ArrayList<>();
	
	@Override
	public void registerObserver(Observer o) {
		this.observer.add(o);
	}

	@Override
	public void notifyObserver(String tweet) {
		observer.forEach(o -> o.notify(tweet));
	}
}

// 자바로 옵저버 만들기
public class ObserverPattern {
	public static void main (String [] args) {
		Feed f = new Feed();
		f.registerObserver(new NYTimes());
		f.registerObserver(new Guardian());
		f.registerObserver(new LeMonde());
		
		f.notifyObserver("The queen said her favourite book is Java 8 in action");
	}
}

interface Observer {
	void notify(String tweet);
}

interface Subject {
	void registerObserver (Observer o);
	void notifyObserver (String tweet);
}

class NYTimes implements Observer {

	@Override
	public void notify(String tweet) {
		if (tweet != null && tweet.contains("money")) {
			System.out.println("Breaking news in NY! " + tweet);
		}
	}
}

class Guardian implements Observer {

	@Override
	public void notify(String tweet) {
		if (tweet != null && tweet.contains("queen")) {
			System.out.println("Yet another news in London... " + tweet);
		}
	}
}

class LeMonde implements Observer {

	@Override
	public void notify(String tweet) {
		if (tweet != null && tweet.contains("wine")) {
			System.out.println("Today cheese, wine and news!" + tweet);
		}
	}
}

class Feed implements Subject {
	private final List<Observer> observer = new ArrayList<>();
	
	@Override
	public void registerObserver(Observer o) {
		this.observer.add(o);
	}

	@Override
	public void notifyObserver(String tweet) {
		observer.forEach(o -> o.notify(tweet));
	}
}
```
간단한 코드는 람다 표현식으로 클래스를 따로 구현하지 않고 정의할 수 있지만 여러 메서드를 정의하는 등 복잡하다면 기존의 클래스로 구현하는 방식이 바람직할 수도 있다.

- 의무체인
작업처리 객체의 체인(동작 체인 등) 을 만들 때는 의무 체인 패턴을 사용한다. 한 객체가 어떤 작업을 처리한 다음에 다른 객체로 결과를 전달하고, 다른 객체도 해야 할 작업을 처리한 다음에 또 다른 객체로 전달하는 식이다. 일반적으로 다음으로 처리할 객체 정보를 유지하는 필드를 포함하는 작업 처리 추상 클래스로 의무 체인 패턴을 구성한다.
* 의무체인 패턴은 함수 체인가 비슷하다. 

- 팩토리
인스턴스화 로직을 클라이언트에 노출하지 않고 객체를 만들 때 팩토리 디자인 패턴을 사용한다.

##### 디버깅
문제가 발생한 코드를 디버깅할 때 개발자는 다음 두 가지를 가장 먼저 확인해야 한다.
- 스택 트레이스
- 로깅
하지만 람다 표현식과 스트림은 기존의 디버깅 기법을 무력화한다.

##### 람다와 스택 트레이스
유감스럽게도 람다 표현식은 이름이 없기 때문에 조금 복잡한 스택 트레이스가 생성된다. 다음은 고의적으로 문제를 일으키도록 구현한 코드이다.

```JAVA
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

// 예외
at java_8_in_action.refactoring_testing_debug.LambdaDebugging.lambda$0(LambdaDebugging.java:9)
	at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
	at java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators.java:948)
	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:472)
	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151)
	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:174)
	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:418)
	at java_8_in_action.refactoring_testing_debug.LambdaDebugging.main(LambdaDebugging.java:9)
```
첫줄부터 이름이 이상하다. 람다는 이름이 없기 때문에 컴파일러가 람다를 참조하는 이름을 만들어 냈다. 미래의 자바컴파일러가 개선해야 할 부분이다. (메서드레퍼런스가 어떤 상황에선 메서드명이 표기되고 어떤 상황에선 컴파일러가 임의로 이름을 짓는다.)

##### 정보로깅
스트림은 forEach로 호출하는 순간 전체 스트림이 소비된다. 스트림 중간중간 로그를 확인하는 방법에는 peek이라는 스트림 연산을 활용할 수 있다. peek은 스트림의 각 요소를 소비한 것처럼 동작을 실행한다. 하지만 forEach처럼 실제로 소비하지않는다.

### 디폴트 메서드
디폴트 메서드는 인터페이스에서 메서드를 정의하는 것을 말한다. 왜 이러한 기능이 생긴 이유는 라이브러리 설계자의 입장에서 인터페이스의 새로운 메서드를 추가하기 위해 사용되는 경우가 많다.
커스텀 인터페이스의 경우 추상메서드를 생성하고 인터페이스를 상속받는 클래스에 재정의를 해주면 되지만 라이브러리같은 경우 그런식으로 새로운 메서드를 추가하게 되면 바이너리 호환성, 소스 호환성, 동작 호환성에 문제가 발생한다.

```JAVA
// default method example (sort도 디폴트 메서드이다.)
default void sort (Comparator<? super E> c) {
	Collections.sort(this, c);
}
```

- 바이너리 호환성 
	- 뭔가를 바꾼 이후에도 에러 없이 기존 바이너리가 실행될 수 있는 상황을 바이너리 호환성이라고 한다.
	- 바이너리 실행에는 인증, 준비, 해석 등의 과정이 포함된다.

- 소스 호환성
	- 코드를 고쳐도 기존 프로그램을 성공적으로 재컴파일할 수 있음을 의미한다.
	- 예로 인터페이스에 메서드를 추가하면 소스 호환성이 아니다. 추가한 메서드를 구현해야 하기 때문이다.

- 동작 호환성
	- 코드를 바꾼 다음에도 같은 입력값이 주어지면 프로그램이 같은 동작을 실행한다는 의미다.
	- 예를 들어 인터페이스에 메서드를 추가하더라도 프로그램에서 추가된 메서드의 호출이 없을 때 동작 호환성은 유지된다.

##### 디폴트 메서드 활용 패턴
- 선택형 메서드
default 메서드를 사용하면 기본 구현이 제공되므로 메서드를 재정의할 필요가 없어 불필요한 코드를 줄일 수 있다. 예시로 Iterator interface를 보면 remove() 메서드가 있다.

- 동작 다중 상속
디폴트 메서드를 이용하면 기존에는 불가능했던 동작 다중 상속 기능도 구현할 수 있다.

	- 다중 상속 형식 : 여기서 ArrayList는 한 개의 클래스를 상속받고, 여섯 개의 인터페이스를 구현한다. 결과적으로 ArrayList는 AbstractList, List, RandomAccess, Cloneable, Serializable, Iterable, Collection의 서브형식이 된다. 따라서 디폴트 메서드를 사용하지 않아도 다중 상속을 활용할 수 있다.

```JAVA
class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable,
Serializable, Iterator<E>, Collection<E> {

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
```

##### 옳지 못한 상속
예를 들어 한개의 메서드를 재사용하기 위해 100개의 메서드를 재사용해야 하는 인터페이스를 사용하는 것은 좋은 생각이 아니다. 이럴 때는 델리게이션(delegation), 즉 멤버 변수를 이용해서 클래스에서 필요한 메서드를 직접 호출하는 메서드를 작성하는 것이 좋다. 종종 final로 선언된 클래스를 볼 수 있다. 다른 클래스가 이 클래스를 상속받지 못하게 함으로써 원래 동작이 바뀌지 않길 원하기 때문이다.

##### 알아야 할 세 가지 해결 규칙
1. 클래스가 항상 이긴다. 클래스나 슈퍼클래스에서 정의한 메서드가 디폴트 메서드보다 우선권을 갖는다.
2. 1번 규칙 이외의 상황에서는 서브인터페이스가 이긴다. 상속관계를 갖는 인터페이스에서 같은 시그니처를 갖는 메서드를 정의할 때는 서브 인터페이스가 이긴다. 즉 B가 A를 상속받는다면 B가 A를 이긴다.
3. 여전히 디폴트 메서드의 우선순위가 결정되지 않았다면 여러 인터페이스를 상속받는 클래스가 명시적으로 디폴트 메서드를 오버라이드하고 호출해야 한다.

##### 디폴트 메서드를 제공하는 서브인터페이스가 이긴다.
Test2.java에서 보면 
D = A의 hello상속 D에서 정의한 메서드가 없으므로 A의 hello적용
B = A 상속관계지만 B의 메서드를 정의하였기 때문에 B의 hello가 우선순위
D 가 메서드를 재정의하면 D가 호출됨. (1번 규칙에 의한 클래스 우선)

##### 충돌 문제
만약 인터페이스 A와 B가 동일한 메서드를 가지고 있을 때 C클래스에서 상속 받은 뒤 호출을 하게되면 규칙에 의한 정의가 되지 않기 때문에 사용자가 메서드를 오버라이딩해야 한다.

### null 대신 Optional

##### 보수적인 자세로 NullPointException 줄이기
if로 null조건 체크 후 메서드 호출하기 : 조건이 여러개일수록 depth 가 늘어나는 반복 패턴 코드를 '깊은 의심 (deep doubt)' 이라고 부른다. 이를 반복하다 보면 코드의 구조가 엉망이 되고 가독성이 떨어진다.

조건당 if하나씩 쓰기 : depth는 줄었지만 값 하나당 if를 하나씩 사용해야 하는 좋지 않은 코드임은 분명하다. 유지보수가 어려워지기 때문이다. 또한 중복코드가 너무많고 오타 등의 실수가 생길 수 있다.

##### null 때문에 발생하는 문제
- 에러의 근원이다
- 코드를 어지럽힌다. : 때로는 중첩된 null 확인 코드를 추가해야 하므로 코드 가독성이 떨어진다.
- 아무 의미가 없다.
- 자바 철학에 위배된다. : 자바는 개발자로부터 모든 포인터를 숨겼지만 null은 예외다.
- 형식 시스템에 구멍을 만든다. : null은 어디든 할당할 수 있다. 시스템의 다른 부분으로 null이 퍼졌을 때 어떤 의미로 사용되는지 알 수 없다.

##### Optional 클래스
Optional 은 선택형값을 캡슐화하는 클래스다. 값이 있다면 Optional<T>로 감싼다. 값이 없을 경우 Optional.empty 메서드로 Optional을 반환한다. Optional.empty는 Optional의 특별한 싱클턴 인스턴스를 반환하는 정적 팩토리 메서드이다. null과 비교했을 때 의미상으론 비슷하지만 null은 Exception을 발생하지만, Optional.empty()는 객체이므로 다양한 방식으로 활용할 수 있다.

#### Optional 적용 패턴
##### Optional 객체 만들기
- 빈 Optional
	- Optional<Car> optCar = Optional.empty();

- 값이 있는 Optional
	- Optional<Car> optCar = Optional.of(car);
	- Car가 null이라면 즉시 NullPointException이 발생한다. (Optional을 사용하지 않았다면 car 프로퍼티에 접근하려 할 떄 에러가 발생한다.)

- null값으로 Optional
	- Optional<Car> optCar = Optional.ofNullable(car);
	- car가 null이면 빈 Optional객체가 반환된다.

##### 디폴트 액션과 Optional 언랩
Optional 클래스는 Optional 인스턴스에서 값을 읽을 수 있는 다양한 인스턴스 메서드를 제공한다.
- get() : 값을 읽는 가장 간단한 메서드면서 동시에 가장 안전하지 않은 메서드이다. 값이 있을 경우 값을 반환하지만 없을 경우 NoSuchElementException을 발생시킨다. Optional에 값이 있다는 것을 확신하지 않는 경우 사용하지 않는 것이 바람직하다.

- orElse : Optional에 값이 없을 경우 default값을 제공할 수 있다.

- orElseGet(Supplier<? extends T> other) : orElse 메서드에 대응하는 게으른 버전의 메서드다. Optional의 값이 없을때만 Supplier가 실행된다. 디폴트 메서드를 만드는 데 시간이 걸리거나(효율성문제) Optional이 비어있을 때만 디폴트 값을 생성하고 싶을 때 사용한다.

- orElseThrow(Supplier<? extends X> exceptionSupplier) : Optional이 비어있을 떄 예외를 발생시킨다는 점에서 get() 메서드와 비슷하다. 하지만 이 메서드는 발생시킬 예외의 종류를 선택할 수 있다.

- ifPersent(Consumer<? super T> consumer) : 값이 존재할 때 인수로 넘겨준 동작을 실행할 수 있다. 값이 없으면 아무일도 일어나지 않는다.

##### 두 Optional합치기

```JAVA
static public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car) {
//		if (person.isPresent() && car.isPresent()) {
//			return Optional.of(findCheapestInsurance(person.get(), car.get())); 
//		} else {
//			return Optional.empty();
//		}
		
		// 람다로 변환
		return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
		
		/* 첫 번째 Optional에 flatMap을 호출했으므로 비어있다면 인수를 전달할 람다를 실행하지 않고 빈 Optional을 반환한다.
		 * 반면 person에 값이 존재한다면 flatMap메서드에 필요한 Optional<Insurance>를 반환하는 Function의 입력으로 person을 사용한다.
		 * 두 번째 Optional에 map을 호출하는데 car의 값이 비었으면 Function은 빈 Optional을 반환하므로 nullSafeFindCheapestInsurance 메서드는 빈 Optional을 반환한다.
		 * 마지막으로 person과 car 모두 값이 있으면 findCheapestInsurance 메서드를 안전하게 호출할 수 있다.*/
	}
```

##### 잠재적으로 null이 될 수 있는 대상을 Optional로 감싸기

```JAVA
public class Example2 {
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();

		// 수정 전 : null 반환
		Object value = map.get("key");
		System.out.println(value);
		
		// 수정 후 : 예외 발생
		Optional<Object> value2 = Optional.ofNullable(map.get("key"));
		Object obj = value2.get();
		System.out.println(obj);
	}
}
```

##### 기본형 Optional과 이를 사용하지 말아야 하는 이유
스트림처럼 Optional도 기본형으로 특화된 OptionalInt, OptionalLong, OptionalDouble 등의 클래스를 제공한다. 스트림의 경우 많은 요소를 가질 때는 기본형 특화 스트림을 이용해 성능을 향상 시킬 수 있지만 Optional은 최대 요소 수는 한 개이므로 기본형 특화 클래스로 성능을 개선할 수 없다. 기본형 특화 Optional은 map, flatMap, filter 등을 지원하지 않으므로 기본형 특화 Optional을 사용할 것을 권장하지 않는다. 또한 다른 일반 Optional과 혼용할 수 없다.

### 조합할 수 있는 비동기 프로그래밍
병령성 : 하나의 동작을 여러 서브 동작으로 분할하고 각각의 서브동작을 다른코어, 다른 CPU, 심지어 다른 기기로 할당할 수 있다.

동시성 : 하나의 CPU 사용을 가장 극대화할 수 있도록 느슨하게 연관된 여러 작업을 수행해야 하는 상황이라면 원격 서비스 결과를 기다리거나, 데이터베이스 결과를 기다리면서 스레드를 블록하기 원치 않을 것이다.

##### Future
Future는 비동기 계산을 모델링하는 데 사용하는 인터페이스이며, 계산이 끝났을 때 결과에 접근할 수 있는 레퍼런스를 제공한다. 시간이 걸릴 수 있는 작업을 Future 내부로 설정하면 호출자 스레드가 결과를 기다리는 동안 다른 유용한 작업을 수행할 수 있다. 예시로 드라이크리닝 서비스를 맡기는 동작에 비유할 수 있다. 세탁소 주인은 드라이클리닝이 언제 끝날지 적힌 영수증(Future)을 줄 것이다. 드라이크리닝이 진행되는동안 우리는 원하는 일을 할 수 있다. Future는 저수준의 스레드에 비해 직관적으로 이해하기 쉽다는 장점이 있다. Future를 이용하려면 시간이 오래 걸리는 작업을 Collable 객체 내부로 감싼 다음에 ExecutorService에 제출해야 한다.

##### Future 제한
- 필요한 선언형 기능
	- 두 개의 비동기 계산 결과를 하나로 합친다. 두 가지 계산 결과는 서로 독립적일 수 있으며 또는 두 번째 결과가 첫 번째 결과에 의존하는 상황일 수 있다.

	- Future 집합이 실행하는 모든 태스크의 완료를 기다린다.

	- Future 집합에서 가장 빨리 완료되는 태스크를 기다렸다가 결과를 얻는다 (예를 들어 여러 태스크가 다양한 방식으로 같은 결과를 구하는 상황)

	- 프로그램적으로 Future를 완료시킨다. (즉, 비동기 동작에 수동으로 결과 제공).

	- Future 완료 동작에 반응한다.(즉, 결과를 기다리면서 블록되지 않고 결과가 준비되었다는 알림을 받은 다음에 Future의 결과로 원하는 추가 동작을 수행할 수 있다.)

위의 선언형으로 이용할 수 있도록 자바 8에서는 CompletableFuture 클래스 (Future 인터페이스를 구현한 클래스) 를 살펴본다. CompletableFuture는 Stream과 비슷한 패턴, 즉 람다 표현식과 파이프라이닝을 활용한다.
Future <-> CompletableFuture == Collection <-> Stream
와 같은 관계로 비유할 수 있다.

##### 동기 API와 비동기 API
동기 API : 메서드를 호출한 다음 메서드의 수행이 완료될 때까지 기다렸다가 반환되는 값으로 동작을 수행한다. 이처럼 동기 API를 사용하는 상황을 블록호출 (blocking call) 이라고 한다.

비동기 API : 메서드가 즉시 반환되며 끝내지 못한 나머지 작업을 호출자 스레드와 동기적으로 실행될 수 있도록 다른 스레드에 할당한다. 이와 같은 비동기 API를 사용하는 상황을 비블록호출 (non-blocking call) 이라고 한다.

##### 팩토리 메서드 supplyAsync로 CompletableFuture 만들기

```JAVA
// getPrice 동기 메서드를 비동기 메서드로 변환
	public Future<Double> getPriceAsync (String product) {
		CompletableFuture<Double> futurePrice = new CompletableFuture<>();
		new Thread( () -> {
			try {
				double price = calculatePrice(product);
				// 계산이 정상적으로 종료되면 Future에 가격 정보를 저장한 채로 Future를 종료한다.
				futurePrice.complete(price); 
			} catch (Exception e) {
				// 도중에 문제가 발생하면 발생한 에러를 포함시켜 Future를 종료한다.
				futurePrice.completeExceptionally(e);
			}
		}).start();
		
		return futurePrice;
	}
	
	// getPrice 람다로 변환
	public Future<Double> getPriceAsync_Lambda (String product) {
		return CompletableFuture.supplyAsync(() -> calculatePrice(product));
	}
```

SupplyAsync : Supplier를 인수로 받아서 CompletableFuture를 반환한다.
CompletableFuture는 Supplier를 실행해서 비동기적으로 결과를 생성한다. ForkJoinPool의 Executor중
하나가 Supplier를 실행할 것이다.하지만 두번째 인수를 받는 오버로드 버전의 supplyAsync 메서드를 이용해서 다른 Executor를 지정할 수 있다. 결국 모든 다른 CompletableFuture의 팩토리 메서드에 Executor를 선택적으로 전달할 수 있다.

##### 스레드 풀 크기 조절
Nthreads = Ncpu * Ucpu * (1 + W/C)
- Ncpu : Runtime.getRuntime().availableProcessors() 가 반환하는 코어 수
- Ucpu : 0과 1 사이의 값을 갖는 CPU 활용 비율
- W/C : 대기시관과 계산시간의 비율

##### 커스텀 Executor 만들기

```JAVA
private final Executor executor = 
			Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() { // 상점 수 만큼의 스레드를 갖는 풀을 생성한다 (스레드 수의 범위는 0 과 100 사이).
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setDaemon(true); // 프로그램 종료를 방해하지 않는 데몬 스레드를 사용한다.
					return t;
				}
	});
```
우리가 만드는 풀은 데몬 스레드 (daemon thread) 를 포함한다. 자바에선 일반스레드가 실행중이면 자바 프로그램은 종료되지 않는다. 따라서 어떤 이벤트를 한없이 기다리면서 종료되지 않는 일반 스레드가 문제가 될 수 있다. 반면 데몬 스레드는 자바 프로그램이 종료될 때 강제로 실행이 종료될 수 있다.

##### CompletableFuture 메서드
- RunAsync : 파라미터가 없기 때문에 반환값이 없다.
- SupplyAsync : 파라미터로 Supplier를 받는데 이는 단순히 하나의 값만 리턴하는 인터페이스이다. 때문에 결과를 리턴할 수 있다.
- completedFutrue : 입력받은 값으로 이미 완료된 CompletableFuture를 생성하는 간단한 API이다.
- thenAccept : 파라미터로 Consumer를 받는다. Complition stage에서 어떤 completion이 발생하면 thenAccept 는 CompletableFuture에서 받은 결과를 Consumer에게 넘겨준다.
- thenApply : Callback 처리를 중첩시키고 싶을 때 사용한다.
- thenCompose : 콜백함수 thenAccept에서 CompletableFuture를 사용하게 된 경우 사용한다.
- thenCombine : 서로 다른 Future가 동시에 수행이 되어 그 결과가 머지됩니다.
- allOf : 2개 이상의 독립적인 Future들을 모두 동시에 수행한 후 머가지 되는 경우 사용됩니다. 리턴값이 void입니다. 따라서 콜백이 아니면 결과를 얻을 수 없습니다.
- anyOf : CompletableFuture 각각이 종료되면 CompletableFuture.anyOf() 의 리턴이 되며 타입은 CompletableFuture<Object> 입니다. 중요한 건 모두 한번씩 수행을 합니다.

### 새로운 날짜와 시간 API
자바 1부터 사용한 Date는 초기값이 1970년도로 설정되어 있고, 데이터가 직관적이지 않다. 자바 1.1에 해결책은 내놓기 위해 Calender 클래스를 대안으로 제공하였지만 어떤 클래스를 사용할 지 혼란만 초래했다. 또한 DataFormat 같은 일부 기능은 Date 클래스에서만 작동한다. 마지막으로 Date와 Calender는 가변 클래스로서 데이터 값이 변할 수 있다. 때문에 유지보수가 어려워진다.  자바 8에 들어서 이러한 문제에 대한 해결책으로 java.time 패키지를 추가한다.

##### LocalDate와 LocalTime
LocalDate 인스턴스는 시간을 제외한 날짜를 표현하는 불변 객체이다. 특히 LocalDate는 어떤 시간대 정보도 포함하지 않는다. LocalDate 인스턴스는 년도, 월, 일 등을 반환한다.

##### Instant : 기계의 날짜와 시간
Instant 클래스는 유닉스 에포크 시간 (1970년 1월 1일 0시 0분 0초 UTC)을 기준으로 특정 지점까지의 시간을 초로 표현한다. 팩토리 메서드 ofEpochSecond에 초를 넘겨줘서 Instant 클래스 인스턴스를 만들 수 있다. Instant 클래스는 나노 초 (10억분의 1초) 의 정밀도를 제공한다.

##### Duration과 Period 정의
LocalDate 관련 클래스는 Temporal 인터페이스를 구현하는데 Temporal 인터페이스는 특정 시간을 모델링하는 객체의 값을 어떻게 읽고 조작할지 정의한다.
- Duration : 파라미터 2개의 시간값의 차이 값을 반환한다. (시 분 초) [LocalDateTime, LocalTime, Instant 타입을 받을 수 있다.]

- Period : 파라미터 2개의 날짜값의 차이를 반환한다. (년 월 일) [LocalDate]

##### TemporalAdjusters 사용하기
다음주 일요일, 돌아오는 평일, 어떤 달의 마지막 날 등 좀 더 복잡한 날짜 조정 기능이 필요할 때 사용한다.

##### 날짜와 시간 객체 출력과 파싱
java.time.format 패키지가 추가되었다. 패키지 내 가장 중요한 클래스는 DateTimeFormatter이다. 정적 팩토리 메서드와 상수를 이용해서 손쉽게 포매터를 만들 수 있다.

##### 다양한 시간대와 캘린더 활용
기존의 java.util.timezone을 대체할 java.time.zoneId 클래스가 새롭게 등장했다. 날짜와 시간 API에서 제공하는 다른 클래스와 마찬가지로 불변 클래스이다.

### 함수형 관점으로 생각하기
함수형이란 일급함수와 관련이 있으며 때로는 객체 변화를 제한하는 것이 함수형과 밀접한 관련을 갖는다.

##### 시스템 구현과 유지보수
synchronized가 있다면 동시성 버그를 매우 고치기 어렵다. 자바 8의 스트림을 이용하면  locking 의 문제를 신경쓰지 않을 수 있다. 단 자바 8 스트림을 이용하려면 상태없는 동작이어야 한다는 조건을 만족해야 한다. (즉, 스트림 처리 파이프라인의 함수는 다른 누군가가 변수의 값을 바꿀 수 있는 상태에 있는 변수를 사용하지 않는다.) 함수형 프로그래밍의 장점은 no side effect(부작용 없음) 와 immutability (불변성)이라는 개념이 있다.

##### 공유된 가변 데이터
공유된 가변데이터는 다양한 클래스에서 사용된다. 이 경우 예상치 못한 값으로 반환되어 오류가 발생할 확률이 올라간다. 만약 반대로 어떤 자료구조도 바뀌지 않는 시스템이 있다고 가정했을 때 값이 변하지 않기 때문에 유지보수가 쉬워진다. 클래스의 상태, 객체의 상태를 바꾸지 않으며 return문을 통해서만 자신의 결과를 반환하는 메서드를 순수 메서드 또는 부작용없는 메서드라고 부른다.
구체적으로 부작용이란 함수 내에 포함되지 못한 기능이다. 부작용의 예로는 다음과 같다.
- 자료구조를 고치거나 필드에 값을 할당
- 예외 발생
- 파일에 쓰기 등의 I/O 동작 수행

불변 객체를 이용하여 부작용을 없애는 방법도 있다. 불변 객체는 인스턴스화 한 다음에 객체의 상태를 바꿀 수 없기 떄문에 예상치 못한 예외의 발생이 없다. 또한 불변 객체는 복사하지않고 공유할 수 있으며 스레드 안전성을 제공한다.

##### 함수형 프로그래밍
함수형 프로그래밍은 선언형 프로그래밍을 따르는 대표적인 방식이며, 부작용 없는 계산을 지향한다. 함수형 프로그래밍은 인수가 (파라미터) 가 같다면 수학적 함수를 반복적으로 호출했을 때 항상 같은 결과가 반환된다. 결론적으로 함수 그리고 if-then-else 등의 수학적 표현만 사용하는 방식을 순수 함수형 프로그래밍이라고 하며 시스템의 다른 부분에 영향을 미치지 않는다면 내부적으로 함수형이 아닌 기능도 사용하는 방식을 함수형 프로그래밍이라고 한다.

##### 함수형 자바
자바로는 완벽한 순수 함수형 프로그래밍을 구현하기 어렵다. 자바의 함수형 메서드는 지역변수만을 변경해야 함수형이라고 할 수 있다. 그리고 함수나 메서드에서 참조하는 객체가 있다면 그 객체는 불변 객체여야 한다. 이 외에도 함수나 메서드가 어떤 예외도 일으키지 않아야 한다. 하지만 자바에서는 0을 나눈다던가 예외상황이 발생한다. try - catch 나 throws등을 사용하여 예외를 처리할 순 있지만 함수형 프로그래밍에서는 이런식으로 예외를 처리하지않고, Optional을 이용하여 문제를 해결한다. 혹은 다른 컴포넌트에 영향을 미치지않도록 지역변수로만 예외를 처리하는 방법이 있다. 적절한 상황에 판단하여 사용하는 것이 좋다.

##### 참조 투명성
부작용을 감춰야 한다는 제약은 참조 투명성 (referential transparency) 개념으로 귀결된다. 즉, 같은 인수로 함수를 호출했을 때 항상 같은 결과를 반환한다면 참조적으로 투명한 함수라고 한다. 예시로 String.replace("a","b") 의 코드가 존재한다면 항상 같은 결과로 반환되기 때문에 참조적으로 투명하다고 할 수 있다. (replace 메서드는 this의 값을 갱신하지 않고 새로운 String으로 반환된다.) 참조 투명성은 프로그램 이해에 큰 도움을 준다. 연산을 기억화(memorization) 또는 캐싱 (caching) 을 통해 다시 계산하지 않고 저장하는 최적화 기능도 제공한다.

##### 꼬리 호출 최적화
일반적으로 반복코드보다 재귀 코드가 더 비싸다. 이유는 재귀함수를 호출할 때마다 호출 스택에 각 호출 시 생성되는 정보를 저장할 새로운 스택 프레임이 만들어진다. 즉 입력값에 비례해서 메모리 사용량이 증가한다. 큰 값을 사용하면 StackOverflowError이 발생한다. 이를 해결하기 위해 꼬리 호출 최적화라는 해결책을 제공한다.

### 함수형 프로그래밍 기법

##### 고차원함수
Comparator.comparing 처럼 다음 중 하나 이상의 동작을 수행하는 함수를 고차원 함수라 부른다.
- 하나 이상의 함수를 인수로 받음
- 함수를 결과로 반환

##### 부작용과 고차원 함수
스트림 연산으로 전달하는 함수는 부작용이 없어야 하며, 부작용을 포함하는 함수를 사용하면 부정확한 결과가 발생하거나 레이스컨디션 때문에 예상치 못한 결과가 발생할 수 있다. 고차원 함수를 적용할 때에도 같은 규칙이 적용된다. 어떤 인수가 전달 될 지 알 수 없으므로 부작용을 포함할 가능성을 염두해 두어야 한다.

##### 커링
커링은 x와 y라는 두 인수를 받는 함수 f를 한 개의 인수를 받는 g라는 함수로 대체하는 기법이다. 함수 g와 원래 함수 f가 최종적으로 반환하는 값은 같다. 즉, f(x, y) = (g(x))(y)가 성립한다.

```JAVA
public class Curring {
	public static void main(String[] args) {
		
		double xx = converter(5 , 9.0/5, 32);
		System.out.println(xx);
		
		DoubleUnaryOperator xxx = curriedConerter(9.0/5, 32);
		System.out.println(xxx.applyAsDouble(5));
	}
	
	// 코드의 문제점 이 코드는 섭씨를 화씨로 변환하는 기능만 사용하는 메서드이다. ( 온도를 제외한 킬로미터, 마일 등의 단위로 변환 불가 )
	// x = 변환값, f = 변환 요소, b = 기준치 조정 요소 
	static double converter (double x, double f, double b) {
		return x * f + b;
	}
	
	// f = 변환 요소, b = 기준치 만 넘겨주어 변환하는 공통함수로 사용할 수 있다.
	static DoubleUnaryOperator curriedConerter (double f, double b) {
		return (double x) -> x * f + b;
	}
}
```

##### 영속 자료구조
함수형 프로그램에서 사용하는 자료구조이다. 함수형 프로그램에서는 함수형 자료구조, 불변 자료구조 등의 용어도 사용하지만 보통은 영속 자료구조라 부른다. 여기서 말하는 영속은 DB에 프로그램 종료 후에도 데이터가 남아있음을 의미하는 영속과는 다른 의미이다. 함수형 메서드에서는 전역 자료구조나 인수로 전달된 구조를 갱신할 수 없다. 자료 구조를 바꾼다면 같은 메서드를 두번호출 했을 때 결과가 달라지면서 참조 투명성에 위배되고 인수를 결과로 단순하게 매핑할 수 있는 능력이 상실되기 떄문이다.

##### 자료구조를 갱신할 때 발생하는 문제

```JAVA
public class Persistent {
	public static void main(String[] args) {
		TrainJourney a = new TrainJourney(10000, null);
		TrainJourney b = new TrainJourney(20000, null);
		TrainJourney c = new TrainJourney(30000, null);
		System.out.println(a);
		a = link (a, b);
		System.out.println(a);
		a = link (a, c);
		System.out.println(a);
	}
	
	// 아래 코드는 a의 TrainJourney에서 마지막 여정을 찾아 a의 리스트 끝부분을 가리키는 null을 리스트 b로 대체한다.
	// 여기서 문제가 발생한다. firstJourney 라는 변수는 x에서 y로의 경로를 포함하고, secondJourney라는 변수는 y에서 z로 경로를 포함한다.
	// link (firstJourney, secondJourney) 를 호출하면 first가 second를 포함하면서 파괴적인 갱신 (first를 변경시킴)이 일어난다.
	// 결과적으로 first 변수는 x에서 y로의 여정이 아니라 x에서 z로의 여정을 의미하게 된다.
	static TrainJourney link (TrainJourney a, TrainJourney b) {
		if (a == null) return b;
		TrainJourney t = a;
		
		// t로 a의 주소를 복사하여 데이터를 변환하기 때문에 a의 값도 변환된다. 이럴 경우 참조 투명성에 위배된다.
		while (t.onward != null) {
			t = t.onward;
		}
		
		t.onward = b;
		return a;
	}
	
	// 함수형에서는 계산 결과를 표현할 자료구조가 필요하면 기존의 자료구조를 갱신하지 않도록 새로운 자료구조를 만들어야 한다.
	static TrainJourney append (TrainJourney a, TrainJourney b) {
		return a == null ? b : new TrainJourney(a.price, append(a.onward, b));
	}
}


class TrainJourney {
	public int price;
	public TrainJourney onward;
	public TrainJourney (int p, TrainJourney t) {
		price = p;
		onward = t;
	}
	
	@Override
	public String toString() {
		if (onward != null) {
			return "price == " + price + " onward.price == " + onward.price;
		}
		return "price == " + price;
	}
}
```

### 스트림과 게으른 평가
스트림은 단 한번만 소비할 수 있다는 제약이 있어서 스트림은 재귀적으로 정의할 수 없다. 문제와 해결방안을 살펴 볼 것이다.

##### 게으른 리스트 만들기
자바 8의 스트림은 요청할 때만 값을 생성하는 블랙박스와 같다. 스트림에 일련의 연산을 적용하면 연산이 수행되지 않고 일단 저장된다. 스트림에 최종연산을 적용해서 실제 계산을 해야 하는 상황에서만 실제 연산이 이루어진다. 게으른 특성 때문에 각 연산별로 스트림을 탐색할 필요 없이 한 번에 여러 연산을 처리할 수 있다. 또한 게으른 리스트는 고차원 함수라는 개념도 지원한다. 함숫값을 자료구조에 저장해서 함숫값을 사용하지 않은 상태로 보관할 수 있다.