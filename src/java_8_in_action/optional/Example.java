package java_8_in_action.optional;

import java.util.Optional;

public class Example {
	
	static Insurance cheapestCompany; 
	public static void main(String[] args) {
		Person person = new Person();
		Optional<Person> optPerson = Optional.of(person);
		// 컴파일 되지 않는다. Optional<Person>타입이므로 map 메서드를 호출할 수 있다.
		// 하지만 Car 형식은 Optional<Car> 형식을 반환하기 때문에 컴파일 되지않는다.
		// 즉 map 연산의 결과는 Optional<Optional<Car>> 형식의 객체다.
//		Optional<String> name = optPerson.map(Person::getCar)
//										 .map(Car::getInsurance)
//										 .map(Insurance::getName);
		
		// flatMap을 사용하여 처리하기
		/** flatMap은 함수를 인수로 받아서 다른 스트림을 반환하는 메서드다.
		 * 보통 인수로 받은 함수를 스트림의 각 요소에 적용하면 스트림의 스트림이 만들어진다.
		 * 하지만 flatMap은 인수로 받은 함수를 적용해서 생성된 각각의 스트림에서 콘텐츠만 남긴다.
		 * 즉 함수를 적용해서 생성된 모든 스트림이 하나의 스트림으로 병합되어 평준화된다.*/ 
		
		String result = getCarInsuranceName(optPerson);
		System.out.println(result);
		
		
	}
	
	static public String getCarInsuranceName (Optional<Person> person) {
		return person.flatMap(Person::getCar)
					 .flatMap(Car::getInsurance)
					 .map(Insurance::getName)
					 .orElse("UnKnown");
	}
	
	static public String getCarInsuranceName (Optional<Person> person, int minAge) {
		return person.filter(p -> p.getMinAge() >= minAge)
					.flatMap(Person::getCar)
					.flatMap(Car::getInsurance)
					.map(Insurance::getName)
					.orElse("UnKnown");
	}
	
	static public Insurance findCheapestInsurance (Person person, Car car) {
		// 다양한 보험회사가 제공하는 서비스 조회
		// 모든 결과 데이터 비교
		return cheapestCompany;
	}
	
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
}


// 사람이 차를 소유했을 수도 소유하지 않을 수도 있다.
class Person {
	private Optional<Car> car;
	private int minAge;
	public Optional<Car> getCar () {return car;}
	public int getMinAge() {return this.minAge;}
}

// 자동차가 보험에 가입되어있을수도 아닐수도 있다.
class Car {
	private Optional<Insurance> insurance;
	public Optional<Insurance> getInsurance() {return insurance;}
}

// 보험회사에는 반드시 이름이 있다.
// 보험회사 이름이 없다면 코드가 존재하지 않기 떄문에 Exception을 처리하는 것이 맞기 때문에 모든 null에 대해 Optional을 사용할 필요는 없다.
class Insurance {
	private String name;
	public String getName() {return name;}
}