package java_8_in_action.new_date_time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

import static java.time.temporal.TemporalAdjusters.*;

public class TemporalAdjusters {
	public static void main(String[] args) {
		LocalDate d1 = LocalDate.of(2014, 3, 18);
		LocalDate d2 = d1.with(nextOrSame(DayOfWeek.SUNDAY)); // 차주 요일
		LocalDate d3 = d2.with(lastDayOfMonth());			  // 달의 마지막 날
		
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(d3);
		
		System.out.println();
		
		LocalDate date = LocalDate.of(2021, 9, 24);
		date = date.with(new NextWorkingDay());
		
		System.out.println(date);
		
		// NextWorkingDay 람다표현식으로 코드작성
		LocalDate date_Lambda = LocalDate.of(2021, 9, 24);
		date_Lambda = date_Lambda.with(temporal -> {
			DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
			int dayToAdd = 1;
			if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
			else if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
			return temporal.plus(dayToAdd, ChronoUnit.DAYS);
		});
		
		System.out.println(date_Lambda);
		
		
		// TemporalAdjuster를 람다 표현식으로 정의하되 모듈화 시키기
		TemporalAdjuster nextWorkingDay = java.time.temporal.TemporalAdjusters.ofDateAdjuster(temporal -> {
			DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
			int dayToAdd = 1;
			if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
			else if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
			return temporal.plus(dayToAdd, ChronoUnit.DAYS);
		});
		
		LocalDate date_private = LocalDate.of(2021, 9, 24);
		date_private = date_private.with(nextWorkingDay);
		System.out.println(date_private);
	}
}

class NextWorkingDay implements TemporalAdjuster {
	@Override
	public Temporal adjustInto(Temporal temporal) {
		DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK)); // 현재 날짜 읽기
		int dayToAdd = 1;				// 보통 하루 추가
		if (dow == DayOfWeek.FRIDAY) { 	// 금요일일 경우 주말을 넘겨야 하기 때문에 3일 추가
			dayToAdd = 3;
		} else if (dow == DayOfWeek.SATURDAY) {	// 토요일일 경우 일요일을 넘겨야 하기 때문에 2일 추가
			dayToAdd =2;
		}
		return temporal.plus(dayToAdd, ChronoUnit.DAYS); // 적정한 날 수만큼 추가된 날짜를 반환
	}
}
