package java_8_in_action.functionalprograming;

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
