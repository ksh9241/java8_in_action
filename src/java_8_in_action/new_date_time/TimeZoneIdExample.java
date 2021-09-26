package java_8_in_action.new_date_time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeZoneIdExample {
	public static void main(String[] args) {
		ZoneId zone = ZoneId.of("Europe/Rome");
		
		LocalDate date1 = LocalDate.of(2021, Month.APRIL, 25);
		ZonedDateTime zdt1 = date1.atStartOfDay(zone);
		
		LocalDateTime date2 = LocalDateTime.of(2021, Month.APRIL, 18, 11, 25);
		ZonedDateTime zdt2 = date2.atZone(zone);
		
		Instant instant = Instant.now();
		ZonedDateTime zdt3 = instant.atZone(zone);
		
		System.out.println(zdt1);
		System.out.println(zdt2);
		System.out.println(zdt3);
	}
}
