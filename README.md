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