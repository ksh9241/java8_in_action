package java_8_in_action.constructor_reference;

import java.util.Comparator;

public class Apple {

	Integer weight = 0;
	String color = "";
	
	public Apple() {
	}
	
	public Apple(Integer weight) {
		this.weight = weight;
	}
	
	public Apple(String color) {
		this.color = color;
	}
	
	public Apple(String color, Integer weight) {
		this.color = color;
		this.weight = weight;
	}
	
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "weight=="+weight+" color=="+color;
	}
}


