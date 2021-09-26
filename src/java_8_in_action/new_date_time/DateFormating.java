package java_8_in_action.new_date_time;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class DateFormating {
	public static void main(String[] args) {
		/** 절대적인 방법으로 속성 바꾸기 */
		LocalDate d1 = LocalDate.of(2021, 9, 26);
		LocalDate d2 = d1.withYear(2020);
		LocalDate d3 = d2.withMonth(3);
		LocalDate d4 = d3.with(ChronoField.MONTH_OF_YEAR, 7);
		
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(d3);
		System.out.println(d4);
		
		System.out.println();
		
		/** 상대적인 방식으로 LocalDate 속성 바꾸기*/
		LocalDate dd1 = LocalDate.of(2021, 4, 04);
		LocalDate dd2 = dd1.plusWeeks(2); // 선언된 객체의 날짜에 + 주(7일)을 더한다.
		LocalDate dd3 = dd2.minusYears(2); // 선언된 객체의 연도를 뺀다. 
		
		
		System.out.println(dd1);
		System.out.println(dd2);
		System.out.println(dd3);
		
	}
}
