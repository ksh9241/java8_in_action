package java_8_in_action.constructor_reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;

public class Sorting2 {

	public static void main(String[] args) {
		List<Apple> inventory = new ArrayList<>();
        inventory.addAll(Arrays.asList(new Apple("green" , 80), new Apple("green", 155), new Apple("red", 120)));
        
        // 그냥 정렬하기
        inventory.sort(new Comparator<Apple>() {
			@Override
			public int compare(Apple a1, Apple a2) {
				return a1.getWeight().compareTo(a2.getWeight());
			}
		});
        
        // {80, green}, {120, red}, {155, green}
        System.out.println(inventory);
        
        // 상속받아서 재정의한 클래스 호출하기
        //inventory.sort(new AppleComparator());
        
        
        
        // 람다 표현식 사용하기
        inventory.set(1, new Apple("green" , 30));
        inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));
        
        // {30, green}, {80, green}, {155, green}
        System.out.println(inventory);
        
        
        // 메서드 레퍼런스로 정렬하기
        inventory.set(1, new Apple("red", 20));
        inventory.sort(comparing(Apple::getWeight));
        
        // {20, red}, {30, green}, {155, green}
        System.out.println(inventory);
        
        // 역정렬
        inventory.sort(comparing(Apple::getWeight).reversed());
        // {155, green}, {30, green}, {20, red} 
        System.out.println(inventory);
        
        System.out.println();
        
        inventory.set(0, new Apple("blue", 30));
        System.out.println("before:::"+inventory);
        inventory.sort(comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));
        System.out.println("after:::"+inventory);
        
        
	}
}

class AppleComparator implements Comparator<Apple> {
	@Override
	public int compare(Apple a1, Apple a2) {
		return a1.getWeight().compareTo(a2.getWeight());
	}
}

