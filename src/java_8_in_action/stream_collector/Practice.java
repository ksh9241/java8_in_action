package java_8_in_action.stream_collector;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Practice {
	public static void main(String [] args) {
		Trader raoul = new Trader("Raoul", "Combridge");
		Trader mario = new Trader("Mario", "Milan");
		Trader alan = new Trader("Alan", "Combridge");
		Trader brian = new Trader("Brian", "Combridge");
		
		List<Transaction> transaction = Arrays.asList(
			new Transaction(brian, 2011, 300),	
			new Transaction(raoul, 2012, 1000),	
			new Transaction(raoul, 2011, 400),	
			new Transaction(mario, 2012, 710),	
			new Transaction(mario, 2012, 700),	
			new Transaction(alan, 2012, 950)	
		);
		
		// 문제1. 2011년에 일어난 모든 트랜잭션을 찾아 오름차순으로 정리하시오
		List<Transaction> result = transaction.stream().filter(a -> a.getYear() == 2011).sorted(Comparator.comparing(Transaction::getValue)).collect(toList());
		System.out.println(result);
		
		System.out.println();
		// 문제2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
		List<String> resultCity = transaction.stream().map(t -> t.getTrader().getCity()).distinct().collect(toList());
		System.out.println(resultCity);
		
		System.out.println();
		// 문제3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오
		List<Trader> resultName = transaction.stream().map(Transaction::getTrader).filter(a -> a.getCity().equals("Combridge")).distinct().sorted(comparing(Trader::getName)).collect(toList());
		System.out.println(resultName);
		
		System.out.println();
		// 문제4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오
		List<Trader> resultNameAll =  transaction.stream().map(Transaction::getTrader).sorted(comparing(Trader::getName)).distinct().collect(toList());
		String traderStr = transaction.stream().map(t -> t.getTrader().getName()).distinct().sorted().reduce("" , (n1, n2) -> n1 + n2);
		System.out.println(traderStr);
		
		System.out.println();
		// 문제5. 밀라노에 거래자가 있는가?
		boolean resultCheck = transaction.stream().anyMatch(d -> d.getTrader().getCity().equals("Milan"));
		if(resultCheck) {System.out.println("거래자있음");}
		
		// 문제6. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오
		List<Integer> value = transaction.stream().filter(t -> t.getTrader().getCity().equals("Combridge")).map(Transaction::getValue).collect(toList());
		System.out.println(value);
		
		transaction.stream().filter(t -> t.getTrader().getCity().equals("Combridge")).map(Transaction::getValue).forEach(System.out::println);
		
		System.out.println();
		// 문제7. 전체 트랜잭션 중 최댓값은 얼마인가?
		int max = transaction.stream().map(t -> t.getValue()).reduce(0, (a, b) -> a > b ? a : b);
		System.out.println(max);
		// 문제8. 전체 트랜잭션 중 최솟값은 얼마인가?
		
		//Optional<Integer> minOp = transaction.stream().map(t -> t.getValue()).reduce(Integer::min);
		Optional<Integer> minOp = transaction.stream().map(t -> t.getValue()).reduce((a, b) -> a > b ? b : a);
		int min = minOp.get();
		System.out.println(min);
	}
}

class Trader {
	String name;
	String city;
	
	public Trader() {}
	
	public Trader(String name, String city) {
		this.name = name;
		this.city = city;
	}
	
	@Override
	public String toString() {
		return "name == " + this.name + " city == "+this.city;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCity () {
		return this.city;
	}
}

class Transaction {
	Trader trader;
	int year;
	int value;
	
	public Transaction () {}
	
	public Transaction (Trader trader, int year, int value) {
		this.trader = trader;
		this.year = year;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "trader == "+this.trader+" year == "+this.year+" value == "+this.value;
	}
	
	public int getYear() {
		return this.year;
	}
	
	public Trader getTrader() {
		return this.trader;
	}
	
	public int getValue() {
		return this.value;
	}
}
