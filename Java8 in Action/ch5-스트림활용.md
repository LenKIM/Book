# 스트림 활용

- 필터링, 슬라이싱, 매칭
- 검색, 매칭, 리듀싱
- 특정 범위의 숫자와 같은 숫자 스트림 사용하기
- 다중 소스로부터 스트림 만들기
- 무한 스트림



***여기서는 기본적인 filter, map 등의 기본연산은 생략하고, 조금 더 실용적인 스트림 활용만을 정리해보자.***



- 두 개의 숫자 리스트가 있을 때 모든 숫자 쌍의 리스트를 반환하시오. 예를 들어 두 개의 리스트 [1,2,3]과 [3,4]가 주어지면 [(1,3), (1,4), (2,3), (2,4), (3,3), (3,4)] 를 반환  

```java
List<Integer> num1 = Arrays.asList(1,2,3);
List<Integer> num2 = Arrays.asList(3,4);

List<int[]> pairs = num1.stream()
  											.flatMap(i -> num2.stream()
                                					.map(j -> new int[]{i,j}))
  											.collect(toList());
```



## 검색과 매칭



`allMatch`, `anyMatch`, `noneMatch`, `findFirst`, `findAny`



- **predicate가 적어도 한 요소와 일치하는지 확인**   

  ```java
  if(menu.stream().anyMatch(Dish::isVegetarian)){
    System.out.println("The menu is (somewhat) vegetarian friendly!!")
  }
  ```

- **predicate가 모든 요소와 일치하는지 검사**  

  ```java
  boolean isHealthy = menu.stream()
    											.allMatch(d -> d.getCalories() < 1000);
  ```

  **noneMatch**

  allmatch와 반대 연산을 수행. 즉, nonMatch는 주어진 Predicate와 일치하는 요소가 없는지 확인

  ```java
  boolean isHealthy = menu.stream()
    											.noneMatch(d -> d.getCalories() >= 1000);
  ```

  `allMatch`, `anyMatch`, `noneMatch` 는 쇼트서킷 기법. 즉 && ,|| 같은 연산.

  > 쇼트서깃이란?
  >
  > 떄로는 전체 스트림을 처리하지 않았더라도 결과를 반환 할 수 있다. 예를 들어 여러 and연산으로 연결된 커다란 불린 표현식을 평가한다고 가정하자. 표현식에서 하나라도 거짓이라는 결과가 나오면 나머지 표현식의 결과과 상관없이 전체 결과도 거짓이 된다.
  >
  > 즉, 모든 스트림의 요소를 처리하지 않고도 결과를 반환 할 수 있다. 원하는 요소를 찾았으면 즉지 결과를 반환. 마찬가지로 스트림의 모든 요소를 처리할 필요 없이 주어진 크기의 스트림을 생성하는 limit 쇼트 서킷 연산.

- 요소 검색

  - `findAny`, 즉 쇼트서킷을 이용해서 결과를 찾는 즉시 실행을 종료.  

    > Optional 이란?
    >
    > Optional<T> 클래스는 값의 존재나 부재여부를 표현하는 컨테이너 클래스로, null은 쉽게 에러를 일으킬 수 있으므로 해당 기능을 만듬.
    >
    > A. `isPresent()` - Optional이 값을 포함하면 참(true) 을 반환하고, 값을 포함하지 않으면 거짓을 반환
    >
    > B. `ifPresent(Consumer<T> block)` 은 값이 있으면 주어진 블록을 실행
    >
    > C. `T get()` 은 값이 존재하면 값을 반환, 값이 없으면 NoSuchElementException 
    >
    > D. `T orElse(T other)` 은 값이 있으면 반환, 없으면 기본값.

```
findFirst, findAny는 언제 사용할까?

병렬 실행에서는 첫 번째 요소를 찾기 어렵다. 따라서 요소의 반환 순서가 상관없다면 병렬 스트림에서는 제약이 적은 findAny를 사용.
```



## Reducing

- map과 reduce 메서드를 이용해서 스트림의 요리 개수를 계산하자.

  ```java
  int count = menu.stream().map(d -> 1).reduce(0, (a,b) -> a + b);
  ```

>**reduce 메서드의 장점과 병렬화**
>
>기존의 단계적 반복으로 합계를 구하는 것과 reduce를 이용해서 합계를 구하는 것은 어떤 차이가 있을까? reduce를 이용하면 내부 반복이 추상화되면서 내부 구현에서 병렬로 reduce를 실행 할 수 있게 된다. 반복적인 합계에서는 sum 변수를 공유해야 하므로 쉽게 병렬화하기 어렵다. 
>
>강제적으로 동기화시킨다 하더라도 결국 병렬화로 얻어야 할 이득이 스레드 간의 소모적인 경쟁 때문에 상쇄되어 버린따는 사실을 알게 될 것이다! 사실 이 작업을 병렬화하려면 입력을 분할하고, 분할된 입력을 더한 다음에, 더한 값을 합쳐야 한다. 지금 까지 살펴본 코드와는 조금 다른 코드가 나타난다.





![image-20190810115751380](http://ww4.sinaimg.cn/large/006tNc79gy1g5uecuxdlsj30u00u0he0.jpg)





## 숫자형 스트림.

앞에서 언급한 것 중에 하나가 `unboxed`와 `boxed`에 대해서 언급한 바 있다.

박싱하는 것도 비용이 많이 발생하기 때문에 IntStream, LongStream등의 여러 스트림이 있다고 언급했었는데, 여기서 그 부분에 대해서 조금 더 자세히 다뤄보자.

```java
int calories = menu.stream()
  								 .map(Dish::getCalories)
  								 .sum();
```

위 코드에서는 박싱이 자체적으로 이루어져 비용이 발생할 것.



이럴 때 할 수 있는 스트림으로 **기본형 특화 스트림**이 존재한다.

```java
int calories = menu.stream() //Stream<Dish> 반환
  								 .mapToInt(Dish::getCalories) //IntStream 반환
  								 .sum();
```

이번에는 **객체 스트림으로 복원**

```java
IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
Stream<Integer> stream = intStream.boxed();
```



***피타고라스 수를 만들면서 해당 숫자형 스트림을 활용해보자.***



피타고라스 이론 - 

`a * a + b * b = c * c`

예를 들어 (3,4,5) => new int[]{3, 4, 5} 로 표현.



두 수가 피타고라스 수의 일부가 될 수 있는 좋은 조합인지 어떻게 확인할 수 있을까?

a * a + b * b 의 제곱근이 정수인지 확인할 수 있다. =>` '표현식 % 1'의 결과가 0, 즉 소수점이 없어야 한다.`

즉, `filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)` 



**집합 생성**

```java
stream.filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
  .map(b -> new int[] {a, b, (int) Math.sqrt(a * a + b * b)})
```



**b값 생성**

```java
IntStream.rangeClosed(1, 100)
  			 .filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
  			 .boxed()
  			 .map(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)});
```

여기서 `IntStream` 을 쓰면

```java
IntStream.rangeClosed(1, 100)
  			 .filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
  			 .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)});
```



**a값 생성**

```java
Stream<int[]> pythagoreanTriples =
  	IntStream.rangeClosed(1, 100).boxed()
  					 .flatMap(a ->
                     	IntStream.rangeClosed(a, 100)
                     					 .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                     .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
                     );
```



**코드 실행**

이제 코드 구현은 완료되었고 limit 를 이용해서 얼마나 많은 세 수를 포함하는 스트림을 만들 것인지만 결정하면 된다.

---

## 스트림 만들기

#### 값으로 스트림 만들기

```java
Stream<String> stream = Stream.of("Java 8", "Lambdas", "In", "Action");
stream.map(String::toUpperCase).forEach(System.out::println)
```

다음처럼 empty 메서드를 이용해서 스트림을 비울 수 있다.

```java
Stream<String> emptySream = Stream.empty();
```

#### 배열로 스트림 만들기

```java
int[] numbers = {2, 3, 5, 7, 11, 13};
int sum = Arrays.stream(numbers).sum(); // 41
```

#### 파일로 스트림 만들기

```java
long uniqueWords = 0;
try(Stream<String> lines = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())){
  uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
    								 .distinct()
    								 .count();
}
catch( IOException e ){
  
}
```

#### 함수로 무한 스트림 만들기

무한 스트림을 만들 수 있는 2가지 방법 `Stream.iterate` , `Stream.generate` 제공.

```java
Stream.iterate(0, n -> n + 2)
  		.limit(10)
  		.forEach(System.out::println);
```

*예제.*

```java
// 피보나치수열 집합 만들기
// 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55 ...

Stream.iterate(new int[]{0, 1}, ???)
  		.limit(20)
  		.forEach(t -> System.out.println("(" + t[0] + "," + t[1] + ")"));

---

Stream.iterate(new int []{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
  		.limit(10)
  		.map(t -> t[0])
  		.forEach(System.out::println)
```



**generate**

```java
Stream.generate(Math::random)
			.limit(5)
			.forEach(System.out::println)
```

`Generate `  를 쓰는 이유는?

우리가 사용한 공급자 supplier 는 상태가 없는 메서드, 즉 나중에 계산에 사용할 어떤 값도 저장해두지 않는다. 하지만 공급자에 꼭 상태가 없어야 하는 것은 아니다.



#### 요약

- 스트림 API 를 이용하면 복잡한 데이터 처리 질의를 표현할 수 있다.
- filter, distinct, skip, limit 메서드로 스트림의 요소를 추출하거나 변환할 수 있다.
- map, flatMap 메서드로 스트림의 요소를 추출하거나 변환할 수 있다.
- findFirst, findAny 메서드로 스트림의 요소를 검색할 수 있다. allMatch, noneMatch, anyMatch 메서드를 이용해서 주어진 프레디케이트와 일치하는 요소를 스트림에서 검색할 수 있다.
- 이ㄹ 메서드는 쇼트서킷(short-circuit), 즉 결과를 찾는 즉시 반환하며, 전체 스트림을 처리하지는 않는다.
- reduce 메서드로 스트림의 모든 요소를 반복 조합하며 값을 도출할 수 있다. 예를 들어 reduce로 스트림의 최댓값이나 모든 요소의 합계를 계산할 수 있다.
- filter,map 등은 상태를 저장하지 않는  상태없는 상태(stateless operation) 이다. reduce 같은 연산은 값을 계산하는 데 필요한 상태를 저장한다. sorted, distinct 등의 메서드는 새로운 스트림을 반환하기에 앞서 스트림의 모든 요소를 버퍼에 저장해야 한다. 이런 메서드를 상태 있는 연산(stateful operation) 이라는 부른다.
- IntStream, DoubleStream, ,LongStream은 기본형 특화 스트림이다. 이들 연산은 각각의 기본형에 맞게 특화되어 있다.
- 컬렉션뿐 아니라 값, 배열, 파일 iterate와 generate 같은 메서드로도 스트림을 만들 수 있다.
- 크기가 정해지지 않은 스트림을 무한 스트림이라고 한다.

