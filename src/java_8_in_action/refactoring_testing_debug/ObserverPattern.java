package java_8_in_action.refactoring_testing_debug;

import java.util.ArrayList;
import java.util.List;

public class ObserverPattern {
	public static void main (String [] args) {
		Feed f = new Feed();
//		f.registerObserver(new NYTimes());
//		f.registerObserver(new Guardian());
//		f.registerObserver(new LeMonde());
		
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

//class NYTimes implements Observer {
//
//	@Override
//	public void notify(String tweet) {
//		if (tweet != null && tweet.contains("money")) {
//			System.out.println("Breaking news in NY! " + tweet);
//		}
//	}
//}
//
//class Guardian implements Observer {
//
//	@Override
//	public void notify(String tweet) {
//		if (tweet != null && tweet.contains("queen")) {
//			System.out.println("Yet another news in London... " + tweet);
//		}
//	}
//}
//
//class LeMonde implements Observer {
//
//	@Override
//	public void notify(String tweet) {
//		if (tweet != null && tweet.contains("wine")) {
//			System.out.println("Today cheese, wine and news!" + tweet);
//		}
//	}
//}

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