package java_8_in_action.new_date_time;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class DateTimeApi {
	public static void main(String[] args) {
		LocalDateTime ldt = LocalDateTime.now();
		System.out.println(ldt);
		
		LocalDate date = LocalDate.of(2014, 03, 02);
		System.out.println(date);
		
		
		// TemporalField (enum) 을 이용해서 LocalDate값 얻기 
		int year = date.get(ChronoField.YEAR);
		int month = date.get(ChronoField.MONTH_OF_YEAR);
		int day = date.get(ChronoField.DAY_OF_MONTH);
		
		System.out.println(year + "-" + month + "-" + day);
		
		LocalTime time = LocalTime.of(19, 48, 20);
		System.out.println(time);
		
		/** Instant 클래스 사용하여 인스턴스 생성*/
		System.out.println(Instant.ofEpochSecond(3));
		System.out.println(Instant.ofEpochSecond(3, 0));
		System.out.println(Instant.ofEpochSecond(2, 1_000_000_000)); // 2초 이후의 1억 나노초
		System.out.println(Instant.ofEpochSecond(4, -1_000_000_000)); // 4초 이전의 1억 나노초
		
		//int d = Instant.now().get(ChronoField.DAY_OF_MONTH);
		// Instant는 Duration 과 Period클래스를 함께 사용할 수 있기 때문에 에러가 발생한다.
		//System.out.println(d);
		
		/** Duration 만들기*/
		Duration d1 = Duration.between(LocalDateTime.of(2021, 8, 25, 00, 00, 00), LocalDateTime.of(2021, 9, 25, 12, 30, 15)); // 744시간
		Duration d2 = Duration.between(LocalTime.of(19, 31), LocalTime.of(20, 31));	// 1시간
		Duration d3 = Duration.between(Instant.ofEpochSecond(100), Instant.ofEpochSecond(1000)); // 15분
		
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(d3);
		
		// 팩토리 메서드 사용하여 객체 생성
		Duration d4 = Duration.of(3, ChronoUnit.MINUTES); // 파라미터 값 3과 타입 분을 넣어주어 객체 생성
		Duration d5 = Duration.ofMinutes(4); // 팩토리 메서드에서 minutes를 표현하기 때문에 숫자 값만 넣어 객체 생성
		System.out.println(d4);
		System.out.println(d5);
		
		/** Period 만들기*/
		Period tenDays = Period.between(LocalDate.of(2014, 3, 8), LocalDate.of(2014, 3, 18));
		System.out.println(tenDays);
		
		Period fiveDays = Period.ofDays(5);
		Period twoYearThreeMonthOneDay = Period.of(2, 3, 1);
		
		System.out.println(fiveDays);
		System.out.println(twoYearThreeMonthOneDay);
		
	}

}
