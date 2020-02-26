```
Chapter 3 리액티브 연산자 입문
__3.1 map() 함수
__3.2 flatMap() 함수
____3.2.1 구구단 만들기
__3.3 filter() 함수
__3.4 reduce() 함수
____3.4.1 데이터 쿼리하기
__3.5 마치며
```

#### 리액티브 연산자 분류

| 연산자                                          | 설명                                                         |
| ----------------------------------------------- | ------------------------------------------------------------ |
| 생성(Creating) 연산자                           | Observable, Single 클래스 등으로 데이터의 흐름을 만들어내는 함수. 2장에서 배웠던 create(), just(), fromArray() 등의 함수와 4장에서 소개할 interval(), range(), timer(), defer()등이 있습니다. |
| 변환(Transforming) 연산자                       | 어떤 입력을 받아서 원하는 출력 결과를 내는 전통적인 의미의 함수입니다. map(), flatMap()등의 함수. |
| 필터(Filter) 연산자                             | 입력 데이터 중에서 원하는 데이터만 걸러냅니다. filter(), first(), take()등의 함수 |
| 합성(Combining)연산자                           | 생성, 변환, 필터 연산자가 주로 단일 Observable을 다룬다면 합성 연산자는 여러 Observable을 조합하는 역할을 합니다. 한 개의 Observable뿐만 아니라 여러 갱의 Observable을 생성하고 조합해보는 것이 RxJava 프로그래밍 |
| 오류처리(Error Handling)연산자                  | onErrorReturn(), onErrorResumeNext()와 retry()등             |
| 유틸리티(Utility)연산자                         | 주로 연산자로는 subscribeOn()과 observeOn()등이 있으며 비동기 프로그래밍을 지원 |
| 조건(Conditional)연산자                         | Observable의 흐름을 제어하는 역할                            |
| 수학과 집합형(Mathematical and Aggregate)연산자 | 수학함수와 연관있는 연산자                                   |
| 배압(Back pressure)연산자                       | 배압이슈에 대응하는 연산자                                   |


### Map() 함수
입력값을 어떤 함수에 넣어서 원하는 값으로 변환하는 함수. String을 String으로 변환할 수도 있고 String을 Integer나 다른 객체로 변환할 수도 있습니다. 지금까지 알고 있던 객체 지향 프로그래밍과 다른 점은 '어떤 함수에 넣어서'

![](http://reactivex.io/documentation/operators/images/map.png)

```java
Function<String, Integer> ballToIndex = ball -> {
            switch (ball) {
                case "RED": return 1;
                case "YELLOW" : return 2;
                case "GREEN" : return 3;
                case "BLUE" : return 5;
                default: return -1;
            }
        };

        String[] balls = {"RED", "YELLOW", "GREEN", "BLUE"};
        Observable<Integer> source = Observable.fromArray(balls)
                .map(ballToIndex); //

        source.subscribe(System.out::println);
```

### flatMap() 함수

Map()함수를 좀 더 발전시킨 함수. Map()함수는 원하는 입력값을 어떤 함수에 넣어서 변환할 수 있는 일대일 함수지만, flatMap()함수는 똑같이 함수에 넣더라도 결과가 Observable로 나온다는 것이 다르다. 즉, map()함수가 일대일 함수라면 flatMap()함수는 일대다 혹은 일대일 Observable 함수입니다. 결괏값으로 Observable이 나온다는 것을 이해하기 어렵다.

![](http://reactivex.io/documentation/operators/images/mergeMap.png)

```Java
Function<String, Observable<String>> getDoubleDiamonds =
                ball -> Observable.just(ball + "<>", ball + "<>",ball + "<>");

        String[] balls = {"1", "3", "5"};
        Observable<String> source = Observable.fromArray(balls)
                .flatMap(getDoubleDiamonds);
        source.subscribe(System.out::println);

결과:
1<>
1<>
1<>
3<>
3<>
3<>
5<>
5<>
5<>
```
flapMap() 함수의 인자로 Observable이 다시 나온다는 것만 기억하자!

#### 구구단

```Java
Scanner sc = new Scanner(System.in);
        int dan = Integer.parseInt(sc.nextLine());
        for (int i = 1; i <= 9; ++i) {
            System.out.println(dan + " * " + i + " = " + dan * i);
        }

//        Step 01 : for문을 Observable로 변환
        Observable<Integer> source = Observable.range(1, 9);
        source.subscribe(row -> System.out.println(dan + " * " + row + " = " + dan * row));
//
//        Step 02 : 사용자 함수 정의하기
        Function<Integer, Observable<String>> gugudan = num ->
                Observable.range(1, 9).map(row -> num + " * " + row + " = " + dan*row);

        Observable<String> source2 = Observable.just(dan).flatMap(gugudan);

        source2.subscribe(System.out::println);

//        Step 03: flatMap()함수를 좀 더 활용하기
		Observable<String> source = Observable.just(dan)
            .flatMap(num -> Observable.range(1,9)
            .map(row -> num + " * " + row + " = " + dan*row));

//        Step 03-2: flatMap()함수를 좀 더 활용하기
		Observable<String> source = Observable.just(dan)
            .flatMap(num -> Observable.range(1,9),
                     (gugu, i) -> gugu + " * " + i + " = " + gugu*i);

```

#### filter() 함수
 Observable에서 원하는 데이터만 걸러내는 역할, 즉 필요없는 데이터는 제거하고 오직 관심있는 데이터만 filter()함수를 통과하게 됩니다.

- first() : Observable의 첫 번째 값 필터, 없으면 기본값 리턴
- last() : Observable의 마지막 값 필터
- take() : 최초 N개 값만 가져옴
- takeLast() : 마지막 N개 값만 필터함
- skip() : 최초 N개 값을 건너 뜀
- skipLast() : 마지막 N개 값을 건너뜀

```Java
Integer[] numbers = {100, 200, 300, 400, 500};
Single<Integer> single;
Observable<Integer> source;

//        1. first
single = Observable.fromArray(numbers).first(-1);
single.subscribe(data -> System.out.println("first() value = " + data));

//        2. last
single = Observable.fromArray(numbers).last(999);
single.subscribe(data -> System.out.println("last() value = " + data));

//        3. take
source = Observable.fromArray(numbers).take(3);
source.subscribe(data -> System.out.println("take(3) values = " + data));

//        4. takeLast(N)
source = Observable.fromArray(numbers).takeLast(3);
source.subscribe(data -> System.out.println("takeLast(3) values = " + data));

//        5. skip(N)
source = Observable.fromArray(numbers).skip(2);
source.subscribe(data -> System.out.println("Skip(2) values = " + data));

//        6. skipLast(N)
source = Observable.fromArray(numbers).skipLast(2);
source.subscribe(data -> System.out.println("SkipLast(2) values = " + data));

first() value = 100
last() value = 500
take(3) values = 100
take(3) values = 200
take(3) values = 300
takeLast(3) values = 300
takeLast(3) values = 400
takeLast(3) values = 500
Skip(2) values = 300
Skip(2) values = 400
Skip(2) values = 500
SkipLast(2) values = 100
SkipLast(2) values = 200
SkipLast(2) values = 300
```

#### reduce() 함수
 발행한 데이터를 모두 사용하여 어떤 최종 결과 데이터를 합성할 때 활용합니다.
 보통 Observable에 입력된 데이터를 필요한 map()함수로 매핑하고, 원하는 데이터만 추출할 때는 불필요한 데이터를 걸러내는 filter()함수를 호출합니다. 또한 상황에 따라 발행된 데이터를 취합하여 어떤 결과를 만들어낼 때는 reduce 계열의 함수를 사용합니다.

![ ](http://reactivex.io/documentation/operators/images/reduce.png)

```java
String[] balls = {"1", "3", "5"};
        Maybe<String> source4 = Observable.fromArray(balls)
                .reduce((ball1, ball2) -> ball2 + "(" + ball1+ ")");
        source4.subscribe(System.out::println);
결과
5(3(1))
```

### 데이터 쿼리하기
 가상의 상점에서 발생한 매출의 총합을 계산
- TV : 2500
- camera : 300
- TV : 1600
- Phone : 800

1. 전체 매출 데이터를 입력
2. 매출 데이터 중 TV매출을 필터링함.
3. TV 매출의 합을 구함

```Java
//      1. 데이터 입력 => 왼쪽에는 상품 이름, 오른쪽에는 매출액
        List<Pair<String, Integer>> sales = new ArrayList<>();
        sales.add(Pair.of("TV", 2500));
        sales.add(Pair.of("Camera", 300));
        sales.add(Pair.of("TV", 1600));
        sales.add(Pair.of("Phone", 800));

        Maybe<Integer> tvSales = Observable.fromIterable(sales)
            
//      2. 매출 데이터 중 TV 매출을 필터링함
        .filter(sale -> "TV".equals(sale.getLeft()))
        .map(sale -> sale.getRight())

//      3. TV매출의 합
        .reduce((sale1, sale2) -> sale1 + sale2);
        tvSales.subscribe(tot -> System.out.println("TV sales: $" + tot));
```
