# 새로운 날짜와 시간 API

- 자바8에서 새로운 날짜와 시간 라이브러리를 제공하는 이유
- 사람이나 기계가 이해할 수 있는 날짜와 시간 표현 방법
- 시간의 양 정의하기
- 날짜 조작, 포매팅, 파싱
- 시간대와 캘린더 다루기



*자바 1.0 에서는 java.util.Data 클래스 하나로 날짜와 시간 관련 기능을 제공*

`Date date = new Date(114, 2, 18);`

\> Tue Mar 18 00:00:00 CET 2014



`Date` 는 JVM 기본시간대인 CET, 즉 중앙 유럽시간대를 사용, 그렇다고 Data클래스가 자체적으로 시간대 정보를 알고 있는 것도 아니다.

`Data` 대신 `Calendar` 클래스가 등장 했지만, 역시 쉽게 에러를 일으키는 설계 문제를 갖고 있었다. 예를 들어 Calendar에서는 1900년도에서 시작하는 오프셋은 없앴지만 여전히 달의 인덱스는 0부터 시작했다.

더군다나, 개발자들이 `Data` 와`Calendar` 을 혼동. 게다가 `DateFormat` 같은 일부 기능은 Date 클래스에서만 작동.



`DataForamt` 에도 문제가 있었는데, 이는 스레드에 안전하지 않았다. 즉, 두 스레드가 동시에 하나의 포매터로 날짜를 파싱할 때 예기치 못한 결과가 일어날 수 있다.

마지막으로 Date와 Calendar는 모두 가변 클래스다. 가변 클래스라는 설계는 유지보수가 아주 어려워진다.





# 자바8에서는 LocalDate, LocalTime, Instant, Duration, Period 제공된다.



## LocalDate와 LocalTime

새로운 날짜와 시간 API를 사용할 때 처음 접하게 되는 것은 **LocalDate**

LocalDate인스턴스는 시간을 제외한 날짜를 표현하는 불변 객체. 특히, `LocalDate` 객체는 어떤 시간대 정보도 포함하지 않는다.



정적 팩토리 메서드 of로 LocalDate인스턴스를 만들 수 있다.

```java
LocalDate date = LocalDate.of(2014, 3, 18);
int year = date.getYear(); // 2014
Month month = date.getMonth(); // MARCH
int day = date.getDayOfMonth(); // 18
DayOfWeek dow = date.getDayOfWeek(); // TUESDAY
int len = date.lengthOfMonth(); // 31 (days in March)
boolean leap = date.isLeapYear(); // false (not a leap year)
```

팩토리 메서드 now는 시스템 시계의 정보를 이용해서 현재 날짜 정보를 얻는다.

LocalDate today = LocalDate.now();



`TemporalField` 는 시간 관련 객체에서 어떤 필드의 값에 접근할지 정의하는 인터페이스.

열거자 `ChronoField` 는 TemporalField 인터페이스를 정의하므로 다음 코드에서 보여주는 것처럼 `ChronField` 의 열거자 요소를 이용해서 원하는 정보를 쉽게 얻을 수 있다.

```java
int y = date.get(ChronoField.YEAR);
int m = date.get(ChronoField.MONTH_OF_YEAR);
int d = date.get(ChronoField.DAY_OF_MONTH);
```



13:45:20 같은 시간은 LocalTime 클래스로 표현. 오버로드 버전의 두 가지 정적 메서드 of로 LocalTime 인스턴스를 만들 수 있음.

즉, 시간과 분을 인수로 받는 of메서드와 시간과 분, 초를 인수로 받는 of메서드가 있다.

```java
LocalTime time = LocalTime.of(13, 45, 20); // 13:45:20
int hour = time.getHour(); // 13
int minute = time.getMinute(); // 45
int second = time.getSecond(); // 20
System.out.println(time);
```



이번에는 String 값을 파싱해서 사용하는 parse 정적 메서드를 사용할 수 있다.

```java
LocalDate date = LocalDate.parse("2014-03-18");
LocalTime time = LocalDate.parse("13:45:20")
```

더하여, `parse` 에는 `DateTomeFormatter` 를 전달 할 수 있는데, 이는 `DateFormat ` 클래스를 대체하는 클래스다. 만약 파싱할 수 없을 때는 DateTimeParseException(RuntimeException을 상속받는 예외) 을 일으킨다.



## 날짜와 시간 조합

`LocalDateTime` 은  `LocalDate` 와 `LocalTime` 을 쌍으로 갖는 복합 클래스.

```java
LocalDateTime dt1 = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45, 20); // 2014-03-18T13:45
LocalDateTime dt2 = LocalDateTime.of(date, time);
LocalDateTime dt3 = date.atTime(13, 45, 20);
LocalDateTime dt4 = date.atTime(time); // 새로운 LocalDateTime을 만든다.
LocalDateTime dt5 = time.atDate(date);
System.out.println(dt1);
```

반대로 추출은 이렇게

`LocalDate date1 = dt1.toLocalDate();`

`LocalTime time1 = dt1.toLocalTime();`





## Instant: 기계의 날짜와 시간

사람은 보통 주, 날짜, 시간, 분으로 날짜와 시간을 계산한다. 하지만 기계에서는 이와같은 단위로 시간을 표현하기가 어려워 **기계의 관점에서는 연속된 시간에서 특정 지점을 하나의 큰 수로 표현하는 것이 가장 자연스러운 시간 표현 방법이다.**

즉, Instant 클래스는 유닉스 에포크 시간(1970 1 1 0 0 0 UTC)을 기준으로 특정 지점까지의 시간을 초로 표현.

팩토리 메서드 `ofEpochSecond`에 초를 넘겨줘서 `Instant` 클래스 인스턴스를 만들 수 있다.

```java
Instant instant = Instant.ofEpochSecond(44 * 365 * 86400);
Instant now = Instant.now();
Instant instant = Instant.ofEpochSecond(2, 1_000_000_000); // 2초 이후의 1억 나노초(1초)
```

다시한번 말하지만 `Instant` 는 기계를 위한 것, 그러므로 `int day = Instant.now().get(ChronoField.DAY_OF_MONTH); ` 하면 java.time.temporal.UnsupportedTemporalTypeException 예외가 발생한다.



## Duration과 Period

Temporal 인터페이스를 구현하는데,  Temporal 인터페이스는 특정 시간을 모델링하는 객체의 값을 어떻게 읽고 조작할지 정의한다.

이번에는 Duration 클래스의 정적 팩토리 메서드 between으로 두 시간 객체 사이의 지속시간을 만들 수 있다.

```java
Duration d1 = Duration.between(LocalTime.of(13, 45, 10), time);
Duration d2 = Duration.between(instant, now);
System.out.println(d1.getSeconds()); // 10
System.out.println(d2.getSeconds()); // 179643836
```



```java
Duration threeMinutes = Duration.ofMinutes(3);
Duration threeMinutes = Duration.of(3, ChronoUnit.MINUTES);

Period tenDays = Period.ofDays(10);
Period threeWeeks = Period.ofWeeks(3);
Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
```

![image-20190831140929326](/Users/lenkim/Library/Application Support/typora-user-images/image-20190831140929326.png)



지금까지 살펴본 모든 클래스는 불변이다. 불변클래스는 함수형 프로그래밍 그리고 스레드 안전성과 도메인 모델의 일관성을 유지하는 데 좋은 특징이다.



## 날짜 조정, 파싱, 포매팅

```java
LocalDate date = LocalDate.of(2014, 3, 18);
date = date.with(nextOrSame(DayOfWeek.SUNDAY));
System.out.println(date);
date = date.with(lastDayOfMonth());
System.out.println(date);
```

정확히 표현하자면 get 과 with 메서드로  Temporal 객체의 필드값을 일거나 고칠 수 있다. 어떤 Temporal 객체가 지정된 필드를 지원하지 않으면 `UnsupportedTemporalTypeException` ㅇl 발생한다.

또는 이렇게도 활용 가능하다.

```java
LocalDate date1 = LocalDate.of(2014, 3, 18);
LocalDate date2 = date1.plusWeeks(1);
LocalDate date3 = date2.minusYears(3);
LocalDate date4 = date3.plus(6, ChronoUnit.MONTHS);
```

get과 with 와 유사한 plus, minus 메서드 도 사용가능하다. 

```java
public interface Temporal extends TemporalAccessor {

    /**
     * Checks if the specified unit is supported.
     * <p>
     * This checks if the specified unit can be added to, or subtracted from, this date-time.
     * If false, then calling the {@link #plus(long, TemporalUnit)} and
     * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
     *
     * @implSpec
     * Implementations must check and handle all units defined in {@link ChronoUnit}.
     * If the unit is supported, then true must be returned, otherwise false must be returned.
     * <p>
```

여기에 정의되어 있음.

![image-20190831141837457](/Users/lenkim/Library/Application Support/typora-user-images/image-20190831141837457.png)



## TemporalAdjusters 사용

지금까지 살펴본 날짜 조정 기능은 비교적 간단한 편. 때로는 다음 주 일요일, 돌아오는 평일, 어떤 달의 마지막 날 등 좀 더 복잡한 날짜 조정 기능이 필요할 것이다. 이때는 오버로드된 버전의  with메서드에 좀 더 다양한 동작을 수행할 수 있도록 하는 기능을 제공하는  TemporalAdjuster를 전달하는 방법으로 문제를 해결할 수 있다.

```java
import static java.time.temporal.TemporalAdjusters.*

LocalDate date1 = LocalDate.of(2014, 3, 18); // 2014-03-18
LocalDate date2 = date1.with(nextOrSame(DayofWeek.SUNDAY)) // 2014-03-23
LocalDate date3 = date2.with(lastDayofMonth()); // 2014-03-31
```

![image-20190831142604682](/Users/lenkim/Library/Application Support/typora-user-images/image-20190831142604682.png)



만약 좀 더 복잡한 날짜 조정 기능을 사용하고 싶다면, 커스텀 TemporalAdjuster 구현을 할 수 있다.



```java
@FunctionalInterface
public interface TemporalAdjuster {

    /**
     * Adjusts the specified temporal object.
     * <p>
     * This adjusts the specified temporal object using the logic
     * encapsulated in the implementing class.
     * Examples might be an adjuster that sets the date avoiding weekends, or one that
     * sets the date to the last day of the month.
     * <p>
     * There are two equivalent ways of using this method.
     * The first is to invoke this method directly.
     * The second is to use {@link Temporal#with(TemporalAdjuster)}:
     * <pre>
     *   // these two lines are equivalent, but the second approach is recommended
     *   temporal = thisAdjuster.adjustInto(temporal);
     *   temporal = temporal.with(thisAdjuster);
     * </pre>
     * It is recommended to use the second approach, {@code with(TemporalAdjuster)},
     * as it is a lot clearer to read in code.
     *
     * @implSpec
     * The implementation must take the input object and adjust it.
     * The implementation defines the logic of the adjustment and is responsible for
     * documenting that logic. It may use any method on {@code Temporal} to
     * query the temporal object and perform the adjustment.
     * The returned object must have the same observable type as the input object
     * <p>
     * The input object must not be altered.
     * Instead, an adjusted copy of the original must be returned.
     * This provides equivalent, safe behavior for immutable and mutable temporal objects.
     * <p>
     * The input temporal object may be in a calendar system other than ISO.
     * Implementations may choose to document compatibility with other calendar systems,
     * or reject non-ISO temporal objects by {@link TemporalQueries#chronology() querying the chronology}.
     * <p>
     * This method may be called from multiple threads in parallel.
     * It must be thread-safe when invoked.
     *
     * @param temporal  the temporal object to adjust, not null
     * @return an object of the same observable type with the adjustment made, not null
     * @throws DateTimeException if unable to make the adjustment
     * @throws ArithmeticException if numeric overflow occurs
     */
    Temporal adjustInto(Temporal temporal);
}

private static class NextWorkingDay implements TemporalAdjuster {
        @Override
        public Temporal adjustInto(Temporal temporal) {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
            if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        }
    }
```



## 날짜와 시간 객체 출력과 파싱

날짜와 시간 관련 작업에서 포매팅과 파싱은 서로 떨어질 수 없는 관계.

심지어 포매팅과 파싱 전용 패키지인 java.time.format 이 새로 추가됨. 이 패키지에서 가장 중요한 클래스는  `DateTimeFormatter `

`DateTimeFormatter ` 클래스는  BASIC_ISO_DATE와 ISO_LOCAL_DATE 등의 상수를 미리 정의하고, DateTimeFormatter를 이용해서 날짜와 시간을 특정 형식의 문자열로 만들 수 있다.

```java
LocalDate date = LocalDate.of(2014, 3, 18);
String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE); // 20140318
String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 2014-03-18
```

 반대로,

```java
LocalDate date1 = LocalDate.parse("20140318", DateTimeFormatter.BASIC_ISO_DATE);
LocalDate date2 = LocalDate.parse("2014-03-18", DateTimeFormatter.ISO_LOCAL_DATE);
```

불변클래스라 안전.

이번에는 패턴으로 활용하는 방법.

```java
LocalDate date = LocalDate.of(2014, 3, 18);
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
DateTimeFormatter italianFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);

System.out.println(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
System.out.println(date.format(formatter));
System.out.println(date.format(italianFormatter));
```

또는 빌더를 활용할 수 있음.

```java
LocalDate date = LocalDate.of(2014, 3, 18);
DateTimeFormatter complexFormatter = new DateTimeFormatterBuilder()
  .appendText(ChronoField.DAY_OF_MONTH)
  .appendLiteral(". ")
  .appendText(ChronoField.MONTH_OF_YEAR)
  .appendLiteral(" ")
  .appendText(ChronoField.YEAR)
  .parseCaseInsensitive()
  .toFormatter(Locale.ITALIAN);

System.out.println(date.format(complexFormatter)); //18. marzo 2014
```



## 다양한 시간대와 캘린더 활용방법

지금까지 살펴본 모든 클래스에는 시간대와 관련한 정보가 없었다. 새로운 날짜와 시간  API의 큰 편리함 중 하나는 시간대를 간단하게 처리할 수 있다는 점.

기존  java.util.TimeZone을 대체할 수 있는 java.time.ZoneId 클래스가 등장. 새로운 클래스를 이용하면 서머타임(DST) 같은 복잡한 사항이 자동으로 처리.

표준 시간이 같은 지역을 묶어서 시간대로 규정

 `ZoneRules` 클래스에는  약 40개 정도의 시간대가 존재.

`ZoneId romeZone = ZoneId.of("Europe/Rome")`;

```java
LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
ZonedDateTime zdt1 = date.atStartOfDay(romeZone);

LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
ZonedDateTime zdt2 = dateTime.atZone(romaZone);

Instant instant = Instant.now();
ZonedDateTime zdt3 = instant.atZone(romeZone);
```





## 요약

- 자바8 이전 버전에서 제공하는 기존의 java.util.Date 클래스와 관련 클래스에서는 여러 불일치점들과 가변성, 어설픈 오프셋, 기본값, 잘못된 이름 결정 등의 설계 결합이 존재
- 새로운 날짜와 시간 API에서 날짜와 시간 객체는 모두 불변.
- 새로운  API는 각각 사람과 기계가 편리하게 날짜와 시간 정보를 관리할 수 있으며 기존 인스턴스를 변환하지 않도록 처리결과로 새로운 인스턴스를 생성
- TemporalAdjuster를 이용하면 단순히 값을 바꾸는 것 이상의 복잡한 동작을 수행할 수 있으며 자신만의 커스텀 날짜 변환 기능을 정의.
- 날짜와 시간 객체를 특정 포맷으로 출력하고 파싱하는 포매터를 정의. 패턴을 이용하거나 프로그램으로 포매터를 만들 수 있으며 포매터는 스레드 안정성을 보장.
- 특정 지역/장소에 상대적인 시간대 또는 UTC/GMT 기준의 오프셋을 이용해서 시간대를 정의할 수 있으며 이 시간대를 날짜와 시간 객체에 적용해서 지역화 할수 있다.
- ISO-8601 표준 시스템을 준수하지 않는 캘린더 시스템도 사용할 수 있다.