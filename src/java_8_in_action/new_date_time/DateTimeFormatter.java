package java_8_in_action.new_date_time;

import java.time.LocalDate;
import java.util.Locale;


public class DateTimeFormatter {
	public static void main(String[] args) {
		
		// Date를 문자열로 변환
		LocalDate date = LocalDate.of(2021, 9, 25);
		String s1 = date.format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE); // 20210925
		String s2 = date.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE); // 2021-09-25
		
		System.out.println(s1);
		System.out.println(s2);
		
		// 문자열을 Date로 변환
		LocalDate d1 = LocalDate.parse("2021-09-28", java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDate d2 = LocalDate.parse("20210928", java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
		
		System.out.println(d1);
		System.out.println(d2);
		
		java.time.format.DateTimeFormatter italianFormatter = java.time.format.DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);
		LocalDate date1 = LocalDate.of(2014, 3, 18);
		String formattedDate = date1.format(italianFormatter);
		LocalDate date2 = LocalDate.parse(formattedDate, italianFormatter);
		
		System.out.println(formattedDate);
		System.out.println(date2);
	}
}
