# 스트림으로 데이터 수집

- Collectors 클래스로 컬렉션을 만들고 사용하기
- 하나의 값으로 데이터 스트림 리튜스하기
- 특별한 리듀싱 요약 연산
- 데이터 그룹화의 분할
- 자신만의 커스텀 컬렉션 개발





### Collection, Collector, Collect 헷갈리지 않기!



바로 실전으로

```java
public static List<Transaction> transactions = Arrays.asList(new Transaction(Currency.EUR, 1500.0),
            new Transaction(Currency.USD, 2300.0),
            new Transaction(Currency.GBP, 9900.0),
            new Transaction(Currency.EUR, 1100.0),
            new Transaction(Currency.JPY, 7800.0),
            new Transaction(Currency.CHF, 6700.0),
            new Transaction(Currency.EUR, 5600.0),
            new Transaction(Currency.USD, 4500.0),
            new Transaction(Currency.CHF, 3400.0),
            new Transaction(Currency.GBP, 3200.0),
            new Transaction(Currency.USD, 4600.0),
            new Transaction(Currency.JPY, 5700.0),
            new Transaction(Currency.EUR, 6800.0));
```

***01. 통화별로 트랙잭션을 그룹화한 다음에 해당 통화로 일어난 모든 트랜잭션 합계를 계산하시오.***

```java
public static class Transaction {
        private final Currency currency;
        private final double value;

        public Transaction(Currency currency, double value) {
            this.currency = currency;
            this.value = value;
        }

        public Currency getCurrency() {
            return currency;
        }

        public double getValue() {
            return value;
        }

        @Override
        public String toString() {
            return currency + " " + value;
        }
    }

    public enum Currency {
        EUR, USD, JPY, GBP, CHF
    }
```

```java
private static void groupFunctionally() {
  Map<Currency, List<Transaction>> transactionsByCurrencies = 
    transactions
    .stream()
    .collect(groupingBy(Transaction::getCurrency));
 
        System.out.println(transactionsByCurrencies);
    }
```



***02.트랜잭션을 비싼 트랜잭션과 저렴한 트랜잭션 두 그룹으로 분류하시오***

***03. 트랜잭션을 도시 등 다수준으로 그룹화하시오. 그리고 각 트랜잭션이 비싼지 저렴한지 구분하시오.***





## 00. Collector 란?

함수형 프로그래밍에서는 '무엇'을 원하는지 직접 명시할 수 있어서 어떤 방법으로 이를 얻을지는 신경 쓸 필요가 없다. 

## 01. Serve As Reducing Fucntion

내부적으로 reduce를 한다. 그럼 reduce와 차별이 되는 건 뭘까?



위 통화예제에서 보여주는 것처럼 Collector 인터페이스의 메서드를 어떻게 구현하느냐에 따라 스트림에 어떤 리듀싱 연산을 수행할지 결정된다. 더하여 Collectors 유틸리티 클래스는 자주 사용하는 컬렉터 인스턴스를 손쉽게 생성할 수 있는 정적 팩토리 메서드를 제공. 가장 대표적으로 `ToList`

*다양한 요소 누적 방식 Collector 인터페이스에 정의*

![image-20190815144132155](http://ww3.sinaimg.cn/large/006tNc79gy1g60b6qkqmnj310u0nghdt.jpg)



## 02.미리 정의된 컬렉션

Collectors에서 제공하는 메서드의 기능은 크게 세 가지로 구분 할 수 있다.

- 스트림 요소를 하나의 값으로 **리듀스하고 요약**
- **요소 그룹화**
- **요소 분할**



### 02.1.리듀스과 요약

- counting()  
  : Stream 에 오직 Int property만 가져와 처리.  

  `long howManyDishs = menu.stream().collect(Collectors.counting());`  

  `long howManyDishs = menu.stream().count();`
  
- Collectors.maxBy , Collectors.minBy  
  : Stream 에 오직 Int property만 가져와 처리.  

  `Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories)`  

  `Comparator<Dish> dishCaloriesComparator = menu.stream().collect(maxBy(dishCaloreisComparator)) `
  
- 그 외 합계 평균등을 반환하는 연산에도 reduce 기능이 자중 사용되고 이걸 요약 연산이라 한다.

- `averagingInt(Dish::getCalories)`

- 통계적인 역할을 하는 Collector : `summarizingInt` 



### 02.1.1 문자열 연결

`String shortMenu = menu.stream().map(Dish::getName).collect(joining());`



`joining` 메서드는 내부적으로 `StringBuilder`를 이용해서 문자열을 하나로 만든다. 

만약, Dish 클래스가 요리명을 반환하는 `toString` 메서드를 포함하고 있다면 위 과정을 생략한다.



### 02.1.2 범용 리듀싱 요약 연산

reducing 팩토리 메서드로도 정의 할 수 있음.   
즉, 범용 Collectors.reducing으로 구현 할 수 있다.

```java
int totalCalories = menu.stream().collect(
  reducing(
    0, <- 리듀싱 연산의 시작값이거나 스트림에 인수가 없을 때는 반환값
    Dish::getCalories,  <- 요리를 칼로리 정수로 변환할 때 사용한 변환 함수
    (i, j) -> i + j) <- 같은 종류의 두 항목을 하나의 값으로 더하는 BinaryOperator
);

Optional<Dish> mostCalorieDish =
  menu.stream()
  .collect(reducing(
		(d1, d2) -> d1.getCalories() > d2.getCalories() ? d1: d2)); <- 인수가 한개일 경우에는 초기값을 넘겨주지 않기 때문에 Optioanl 로 남겨진다.
```



> *Notes*
>
> Collect와 Reduce는 무엇이 다를까?
>
> collect 메서드는 도출하려는 결과를 누적하는 컨테이너를 바꾸도록 설계된 메서드인 반면,  
> reduce는 두 값을 하나로 도출하는 불변형 연산이라는 점에서 의미론적인 문제가 일어난다.



> *Notes*
>
> 제네릭 와일드 카드 '?' 사용법 
>
> public static <T> Collector<T, ?, Long> counting() {
>
> ​	return reducing(0L, e -> 1L, Long::sum);
>
> }
>
> ?는 컬렉션의 누적자 형식이 알려지지 않았음을, 즉 누적자의 형식이 자유로움을 의미한다. 위 예제에서는 Collectors 클래스에서 원래 정의된 메서드 시그너처를 그대로 사용했을 뿐이다. 

### 02.1.3 같은 연산도 다양한 방식으로 수행 가능

```java 
int totalCalories = menu.stream().collect(
  reducing(
    0, <- 초깃값
    Dish::getCalories,  <- 변환 함수
    Integer::sum) <- 합계 함수
);
```



## 02.2.1 그룹화

***데이터 집합을 하나 이상의 특성으로 분류해서 그룹화하는 연산도 데이터베이스에서 많이 수행되는 작업.***

트랜잭션 통화 그룹화 예제에서 확인했듯이 명령형으로 그룹화를 구현하려면 까다롭고, 할일이 많으며, 에러도 많이 발생한다. 

*하지만 자바 8의 함수형을 이용하면 가독성 있는 한 줄의 코드로 그룹화를 구현할 수 있다.*

```java
Map<Dish.Type, List<Dish>> dishesByType = menu
.stream()
.collect(groupingBy(Dish::getType));

// {FISH=[prawns ...], OTHER=[french, fries, rice ...], MEAT= [pork, beef ...]}
```



`groupingBy` 가 바로 **분류함수**.

```java
groupingBy(Function, Collector)
groupingBy(Function, Supplier(.get), Collector)
  
groupingByConcurrent(Function)
```



```java
public enum CaloricLevel {DIET, NORNAL, FAT}

Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = 
  
  menu
  .stream()
  .collect( groupingBy(dish -> {
    if(dish.getCalories() <= 400) {
      return CaloricLevel.DIET;
    } else if(dish.getCalories() <= 700){
      return CaloricLevel.NORMAL;
    } else {
            return CaloricLevel.FAT;
		}
  }));
```

이런식으로도 활용할 수 있다.

더욱 진화시켜서 2번째 수준까지 분류를 시도해보자.

```java
public enum CaloricLevel {DIET, NORNAL, FAT}

// collect(groupingBy(function, groupingBy(function))) <- 이런 형태.

Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream().collect(
  groupingBy(Dish::getType,  groupingBy(dish -> {
      if(dish.getCalories() <= 400) {
		        	return CaloricLevel.DIET;
      } else if(dish.getCalories() <= 700){
       	 			return CaloricLevel.NORMAL;
      } else {
              return CaloricLevel.FAT;
      }
    )
  }));
  
//결과
{MEAT={DIET=[chiken], NORMAL=[beef], FAT=[pork]}, FISH={DIET=[prawbs], NORMAL=[salmon]}, OTHER={DIET=[rice, seasomal fruit], NORMAL=[pizze]}}
```



***보통 groupingBy의 연산을 '버킷(bucket)' 개념으로 생각하면 쉽다. ***



### 02.2.2 서브그룹으로 데이터 수집.



데이터를 서브그룹으로 나눈뒤 요약함수를 활용해서 데이터를 수집할 수 있다.

```java
// collect(groupingBy(function, Collector)) <- 이런 형태.
Map<Dish.type, Long> typesCount = menu
	.stream()
  .collect(groupingBy(Dish::getType, counting()));
```

\> groupingBy(f) 는 사실 groupingBy(f, toList())  이다.



![image-20190815151912962](http://ww1.sinaimg.cn/large/006tNc79gy1g60c9x6p2gj30u00xlu15.jpg)



### 02.2.3 컬렉터 결과를 다른 형식으로 적용하기

마지막 그룹화 연산에서 맵의 모든 값을  Optioanl 로 감쌀 필요가 없으므로 Optional을 삭제할 수 있다.



즉, 다음처럼 팩토리 메서드 Collector.collectingAndThen으로 컬렉터가 반환한 결과를 다른 형식으로 활용할 수 있다.

```java
private static Map<Dish.Type, Dish> 
mostCaloricDishesByTypeWithoutOprionals() {
  
        return menu.stream()
        .collect(
        groupingBy(Dish::getType, collectingAndThen(
        														reducing(
        														(d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2), Optional::get)));
```

```java
private static Object mostCaloricPartitionedByVegetarian() {
  return menu.stream().collect(
    groupingBy(Dish::isVegetarian,
                   collectingAndThen(
                     maxBy(comparingInt(Dish::getCalories)),
                     Optional::get)));
}

3가지 요리로 분류후 각 요리에서 가장 칼로리가 높은 음식을 표현한다.
```



### 02.2.4 groupingBy와 함께 사용하는 다른 컬렉터 예제

```java
private static Map<Dish.Type, Integer> sumCaloriesByType() {
        return menu.stream().collect(groupingBy(Dish::getType,
                summingInt(Dish::getCalories)));
    }

//Sum calories by type: {MEAT=1900, FISH=850, OTHER=1550}

private static Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType() {
        return menu.stream().collect(
                groupingBy(Dish::getType, mapping(
                        dish -> { if (dish.getCalories() <= 400) 
                          return CaloricLevel.DIET;
                        else if (dish.getCalories() <= 700) 
                          return CaloricLevel.NORMAL;
                        else return CaloricLevel.FAT; },
                        toSet() )));
    }
////Caloric levels by type: {MEAT=[FAT, NORMAL, DIET], FISH=[DIET, NORMAL], OTHER=[NORMAL, DIET]}
```

## 02.3.1 분할(partitioning function)



```
partitioningBy 은

partitioningBy(Predicate, Collector)
partitioningBy(Predicate)

가능하다.
```

*앞에서 살펴본 GroupingBy와 다른 점은 그룹화를 시키는 것이 아니라. True, False로 구분짓게 만들어 준다는 점에서 다르다.*

```java
public static final List<Dish> menu =
  Arrays.asList( new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 400, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH));

Map<Boolean, List<Dish>> partitionedMenu  = menu
  .stream()
  .collect(partitioningBy(
    Dish::isVegetarian) 
          ); <- 분할 함수
// Dishes partitioned by vegetarian: {false=[pork, beef, chicken, prawns, salmon], true=[french fries, rice, season fruit, pizza]}
private static Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType() {
  return menu
    .stream()
    .collect(partitioningBy(
      Dish::isVegetarian, <- 분할 함수
      groupingBy(Dish::getType))); <- 두번째 컬렉터
}

// Vegetarian Dishes by type: {false={FISH=[prawns, salmon], MEAT=[pork, beef, chicken]}, true={OTHER=[french fries, rice, season fruit, pizza]}}
```

위 코드를 실행하면 다음과 같은 맵이 나온다.

```
{false = [port, beef, ....], true=[french fries, rice, ...]}
```

이제 이 결과를

```java
List<Dish> vegetarianDishes = partitionedMenu.get(true);
```

으로 결과를 가져올 수 있다.

*결과적으로, 분할 함수의 장점은 참, 거짓 두 가지 요소의 스트림 리스트를 모두 유지한다는 것이다.*



**분할 함수 또한 n 수준으로 서브그룹을 그룹화할 수 있다.**



### 숫자를 소수와 비소수로 분할하기.

정수 n을 인수로 받아서 2에서 n까지의 자연수를 소수(prime)와 비소수(nonprome)로 나누는 프로그램 구현하기.



```java
public boolean isPrime(int candidate){
  return IntStream.range(2, candidate) // 2부터 candidate 미만 사이의 자연수를 생성
    						.noneMatch(i -> candidate % 1 == 0); // 스트림의 모든 정수로 candidate를 나눌 수 없으면 참을 반환한다.
}
```

조금 더 심화시키면 소수의 대상을 주어진 수의 제곱근 이하의 수로 제한할 수 있다,

```java
public boolean isPrime(int candidate){
  int candidateRoot = (int) Math.sqrt((double)candidate);
  return IntStream.rangeClosed(2, candidateRoot)
    						.noneMatch(i -> candidate % 1 == 0);
}
```



이제 n개의 숫자를 포함하는 스트림을 만든 다음에 우리가 구현한 isPrime 메서드를 프레디케이트로 이용하고 partitioningBy 컬렉터로 리듀싱해서 소수 분류

```java
public Map<Boolean, List<Integer>> partitionPrimes(int n){
  return IntStream.rangeClosed(2, n).boxed()
    							.collect(
  											partitioningBy(candidate -> isPrime(candidate)));
}
```



그 외 다양한 Collectors 를 살펴보면

![image-20190815153624208](http://ww2.sinaimg.cn/large/006tNc79gy1g60crr0vdrj310i0cm1ik.jpg)



![image-20190815153614197](http://ww2.sinaimg.cn/mw1024/006tNc79gy1g60crkp61wj30u014lb2b.jpg)





## 03. Collector 인터페이스

Collector 인터페이스는 리듀싱 연산(즉, 컬렉터)을 어떻게 구현할지 제공하는 메서드 집합으로 구성된다.

이번에는 Collector 인터페이스를 직접 구현해서 더 효율적으로 문제를 해결하는 컬렉터를 만드는 방법을 보자.



먼저 Collector 인터페이스 를 살펴보면

```java
public interface Collector<T, A, R> {
			   Supplier<A> supplier;
         BiConsumer<A, T> accumulator;
         BinaryOperator<A> combiner;
         Function<A, R> finisher;
         Set<Characteristics> characteristics;
}
```



- T는 수집될 스트림 항목의 제네릭 형식이다.
- A는 누적자, 즉 수집 과정에서 중간 결과를 누적하는 객체의 형식이다.
- R은 수집 연산 결과 객체의 형식(항상 그런 것은 아니지만 대개 컬렉션 형식)이다.



예를 들어 Stream\<T> 의 모든 요소를 List\<T>로 수집하는 ToListCollector\<T>라는클래스로 구현할 수 있다.

> public class ToListCollector<T> implements Collector<T, LIST\<T>, LIST\<T>



여기서는 가장 만만한 Collector 인 tolist를 살펴보자.

이제 각 인터페이스의 메서드를 살펴보면,

`Supplier<A> supplier;` 

**새로운 결과 컨테이너 만들기**  
: supplier 메서드는 빈 결과로 이루어진 Supplier 를 반환. 즉, Supplier 는 수집 과정에서 빈 누적자 인스턴스를 만드는 파라미터가 없는 함수.

```java
public Supplier<List<T>> supplier(){
  return () -> new ArrayList<T>();
}

/// 생성자 레퍼런스를 쓰면
public Supplier<List<T>> supplier(){
  return ArrayList::new;
}
```

`BiConsumer<A, T> accumulator;`

**accumulator 메서드 : 결과 컨테이너에 요소 추가하기.**

: accumulator 메서드는 리듀싱 연산을 수행하는 함수를 반환한다. 스트림에서 n번째 요소를 탐색할 때 두 인수, 즉 누적자(스트림의 첫 n-1개 항목을 수집한 상태)와 n번째 요소를 함수에 적용한다. 

함수의 반환값은 void,

 **즉 요소를 탐색하면서 적용하는 함수에 의해 누적자 내부 상태가 바뀌므로 누적자가 어떤 값인지 단정할 수 없다.**

```java
public BiConsumer<List<T>, T> accumlator(){
  return (list, item) -> list.add(item);
}

// 생성자 래퍼런스
public BiConsumer<List<T> supplier(){
  return List::add;
}
```

`Function<A, R> finisher;`

**최종 변환값을 결과 컨테이너로 적용하기.**

: 스트림 탐색을 끝내고 누적자 객체를 최종결과로 변환하면서 누적 과정을 끝낼 때 호출할 함수를 반환해야 한다.  

때로는 ToListCollector에서 볼 수 있는 것처럼 누적자객체가 이미 최종 결과인 상황도 있다. 

이럴 때는 변환 과정이 필요하지 않으므로 finisher 메서드는 항등 함수를 반환.

```java
public Function<List<T>, List<T>> finisher() {
  return Function.identity();
}
```

`BinaryOperator<A> combiner;`

**: 두 결과 컨테이너 병합**

마지막으로 리듀싱 연산에서 사용할 함수를 반환하는 네 번째 메서드 combiner

combiner는 두 스트림의 서로 다른 서브 파트를 병렬로 처리할 때 누적자가 이 결과를 어떻게 처리할지 정의.

```java
public BinaryOperator<List<T>> combiner() {
  return (list1, list2) -> {
    list1.addAll(list2);
    return list1;
  }
}
```

![image-20190815172328523](http://ww1.sinaimg.cn/large/006tNc79gy1g60fv79vk9j31060sknpd.jpg)



combiner를 이용하면 스트림의 리듀싱을 병렬로 수행할 수 있다. 스트림의 리듀싱을 병렬로 수행할 때 자바 7의 포크/조인 프레임워크와 Spliterator를 사용한다.

다음은 병렬 리듀싱 수행과정을 보여준다.

- 스트림을 분할해야 하는지 정의하는 조건이 거짓으로 바뀌기 전까지 원래 스트림을 재귀적으로 분할 한다.(보통 분산된 작업의 크기가 너무 작아지면 병렬 수행의 속도는 순차 수행의 속도보다 느려진다. 즉, 병렬 수행의 효과가 상쇄된다. 일반적으로 프로세싱 코어의 개수를 초과하는 병렬 작업은 효율적이지 않다.)
- 이제 위 그림에서 보여주는 것처럼 모든 **서브스트림** 의 각요소에 리듀싱 연산을 순차적으로 적용해서 서브스트림을 병렬로 처리할 수 있다.
- 마지막에는 컬렉터의 Combiner 메서드가 반환하는 함수로 모든 부분결과를 쌍으로 합친다. 즉, 분할된 모든 서브스트림의 결과를 합치면서 연산이 완료된다.

![image-20190815173112018](http://ww1.sinaimg.cn/large/006tNc79gy1g60g39dk87j30uu0u0x6p.jpg)





**마지막으로 Characteristics 메서드**

컬렉터의 연산을 정의하는 Characteristics 형식의 불변 집합을 반한한다. Characteristic는 스트림을 병렬로 리듀스할 것인지 그리고 병렬로 리듀스한다면 어떤 최적화를 선택해야 할지 힌트를 제공.

Characteristics는 다음 세 항목을 포함하는 열거형이다.

`UNORDERED`

리듀싱 결과는 스트림 요소의 방문 순서나 누적 순서에 영향을 받지 않는다.

`CONCURRENT`

다중 스레드에서 accumulator 함수를 동시에 호출할 수 있으며 이 컬렉터는 스트림의 병렬 리듀싱을 수행할 수 있다. 컬렉터의 플래그에 UNORDERED를 함께 설정하지 않았다면 데이터 소스가 정렬되어 있지 않은(즉, 집합처럼 요소의 순서가 무의미한) 상황에서만 병렬 리듀싱을 수행

`IDENTITY_FINISH`

finisher 메서드가 반환하는 함수는 단순히 identity를 적용할 뿐이므로 이를 생략할 수 있다. 따라서 리듀싱 과정의 최종 결과로 누적자 객체를 바로 사용할 수 있다. 또한 누적자 A를 결과 R로 안전하게 형변환할 수 있다.



```java
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import static java.util.stream.Collector.Characteristics.*;

public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

    @Override
    public Supplier<List<T>> supplier() {
        return () -> new ArrayList<T>();
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return (list, item) -> list.add(item);
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
        return i -> i;
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT));
    }
}
```



### 컬렉션 구현을 만들지 않고도 커스텀 수집 수행하기.



IDENTITY_FINISH 수집 연산에서는 Collector 인터페이스를 완전히 새로 구현하지 않고도 같은 결과를 얻을 수 있다. Stream은 세 함수(supplier, accumulator, combiner)를 인수로 받아 collect메서드를 오버로드하며 각각의 메서드는 Collector 인터페이스의 메서드가 반환하는 함수와 같은 기능을 수행한다. 예를 들어 다음처럼 스트림의 모든 항목을 리스트에 수집하는 방법도 있다.

```java
List<Dish> dishes = menuStream.collect(
											ArrayList::new, //supplier
											List::add, // accumlator
											List::addAll); //combiner
```



좀더 축약이지만 가독성 떨어짐.

// 일부 내용 정리 필요.

### 마지막으로 직접 커스텀 컬렉터를 구현해서 성능 개선하기.



직접 만들어 보기.

커스텀 컬렉터로 n까지의 자연수를 소수와 비소수로 분할 할 수 있다.

이전에 개발 했던 것은

```java
public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
  return IntStream.rangeClosed(2, n).boxed()
    .collect(partitioningBy(candidate -> isPrime(candidate)));
}

public static boolean isPrime(int candidate) {
  return IntStream.rangeClosed(2, candidate-1)
    .limit((long) Math.floor(Math.sqrt((double) candidate)) - 1)
    .noneMatch(i -> candidate % i == 0);
}
```

아래는 커스텀 collector를 위한 isPrime

```java
public static boolean isPrime(List<Integer> primes, Integer candidate) {
  double candidateRoot = Math.sqrt((double) candidate);
  //return primes.stream().filter(p -> p < candidateRoot).noneMatch(p -> candidate % p == 0);
  return takeWhile(primes, i -> i <= candidateRoot).stream().noneMatch(i -> candidate % i == 0);
}

public static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
  int i = 0;
  for (A item : list) {
    if (!p.test(item)) {
      return list.subList(0, i);
    }
    i++;
  }
  return list;
}
```



```java
public static class PrimeNumbersCollector
  implements Collector<
  Integer, 										<= T
Map<Boolean, List<Integer>>,  <= A
Map<Boolean, List<Integer>>>  <= R
{ 
  // pulbic interface Collector<T, A, R>
  // T는 스트림 요소의 형식
  // A는 중간 결과를 누적하는 객체의 형식
  // R은 collect 연산의 최종 결과 형식

  @Override
  public Supplier<Map<Boolean, List<Integer>>> supplier() {
    return () -> new HashMap<Boolean, List<Integer>>() {{
      put(true, new ArrayList<Integer>());
      put(false, new ArrayList<Integer>());
    }};
  }
// 누적자로 사용할 맵을 만들면서 true, false 키와 빈 리스트로 초기화를 했음. 수집 과정에서 빈 리스트에 각각 소수와 비소수를 추가할 것이다. 
// 스트림의 요소를 어떻게 수집할 지 결정하는 것은 accumulator는 최적화의 핵심.


  @Override
  public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
    return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
      acc.get(
      isPrime(
      acc.get(true), candidate) <= isPrime의 결과에 따라 소수/비소수 리스트
      ).add(candidate); <= candidate를 알맞은 리스트에 추가.
    };
  }
  
  

  @Override
  public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
    return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
      map1.get(true).addAll(map2.get(true));
      map1.get(false).addAll(map2.get(false));
      return map1;
    };
  }

// 병렬 실행할 수 있는 컬렉터 만들기.

  @Override
  public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
    return i -> i;
  }

  @Override
  public Set<Characteristics> characteristics() {
    return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH));
  }
```



## 요약

- collect는 스트림의 요소를 요약 결과를 누적하는 다양한 방법(컬렉터라 불리는)을 인수로 갖는 최종 연산이다.
- 스트림의 요소를 하나의 값으로 리듀스하고 요약하는 컬렉터뿐 아니라 최솟값, 최댓값, 평균값을 계산하는 컬렉터 등이 미리 정의되어 있다.
- 미리 정의된 컬렉터인 groupingBy로 스트림의 요소를 그룹화하거나, partitioningBy로 스트림의 요소를 분할 할 수 있다.
- 컬렉터는 다수준의 그룹화, 분할, 리듀싱 연산을 적합하게 설계되어 있다.
- Collector 인터페이스에 정의된 메서드를 구현해서 커스텀 컬렉터를 개발할 수 있다.