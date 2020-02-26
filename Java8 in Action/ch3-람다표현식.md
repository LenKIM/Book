# 3장. 람다 표현식



## 1 람다란 무엇인가 

*람다 표현식* 메서드로 전달할 수 있는 익명 함수를 단순화한 것.

- 익명
- 함수
- 전달
- 간결성

## 2 어디에, 어떻게 람다를 사용할까? 

 함수형 인터페이스라는 문맥에서 람다 표현식을 사용할 수 있음.

- 함수형 인터페이스 => 정확히 하나의 추상 메서드를 지정하는 인터페이스

  ```java
  public interface Predicate<T> {
  	boolean test(T t);
  }
  
  그외
  
  public interface Callable<V> {
  	V call();
  }
  ```

  *람다  표현식*으로 함수형 인터페이스의 추상 메서드 구현을 직접 전달 할 수 있으므로 **전체 표현식을 함수형 인터페이스의 인스턴스로 취급**(기술적으로 따지면 함수형 인터페이스를 concreate 구현한 클래스의 인스턴스) 할 수 있다.

- 함수 디스크럽터 => 함수형 인터페이스의 추상 메서드 시그너처(signature)는 람다 표현식의 시그너처라 하는데, 람다 표현식의 시그너처를 서술하는 메서드를 **함수 디스크립터**

> @FunctionalInterface
>
> 그저 *함수형 인터페이스* 임을 가리키는 어노테이션

## 3 람다 활용: 실행 어라운드 패턴

자원 처리(예를 들면 데이터베이스의 파일 처리) 에 사용하는 순환 패턴은 자원을 열고, 처리한 다음에, 자원을 닫는 순서로 이루어진다. setup과 cleanup과정은 대부분 비슷하다. 즉, 실제 자원을 처리하는 코드를 설정과 정리 두 과정이 둘러싸는 형태를 갖는다. 

이와 같은 코드를 **실행 어라운드 패턴** (execute arount pattern) 이라 한다.

```java
// 1. 동작 파라미터화를 기억하라.
public static String processFile() throws IOException {
  try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))){
    return br.readLine();
  }
}
```

```java
// 2. 함수형 인터페이스를 이용해서 동작 전달
public interface BufferedReaderProcessor {
	String process(BufferedReader b) throws IOException;
}

public static String processFile(BufferedReaderProcessor p) throws IOException {
...
}
```

```java
// 3. 동작 실행
public static String processFile(BufferedReaderProcessor p) throws IOException {
  try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))){
    return p.process(br);
}
```

```java
// 4. 람다 전달.
String oneLine = processFile((BufferedReader br) -> br.readLine());

String twoLines = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```



## 4 함수형 인터페이스 사용

자바 8 라이브러리 설계자들은 java.util.function 패키지로 여러 가지 새로운 함수형 인터페이스를 제공. 이 절에서는 Predicate, Consumer, Function 인터페이스 그외 다양한 함수형 인터페이스를 소개한다.

#### 4-1. Predicate

```java
@FunctionalInterface
public unterface Predicate<T> {
  boolean test(T t);
}

//---
//example

public static <T> List<T> filter(List<T> list, Predicate<T> p){
  List<T> results = new ArrayList<>();
  for(T s: list){
    if(p.test(s)){
      results.add(s);
    }
  }
  return results;
}

Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();

List<String> nonEmpty = filter(listOfString, nonEmptyStringPredicate);
```

그 외

```java
default Predicate<T> and(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }
    
    OR 도 가능
```



#### 4-2 Consumer

`java.util.function.Consumer<T> 인터페이스` 제네릭 형식 T객체를 받아 Void 반환

```java
@FunctionalInterface
public interface Consumer<T> {
  void accept(T t);
}

public static <T> void forEach(List<T> list, Consumer<T> c){
  for(T i:list){
    c.accept(i)
  }
}
```



#### 4-3 Function

`java.util.function.Function<T,R>` 제네릭 형식 T를 인수로 받아서 제네릭 형식 R객체를 반환하는 apply라는 추상 메서드를 정의.

*입력을 출력으로 매핑하는 람다를 정의할 때 Function 인터페이스 활용*

```java
@FunctionalInterface
public interface Function<T, R> {
  R apply(T t);
}

public static <T, R> List<R> map(List<T> list, Function<T, R> f){
  List<R> result = new ArrayList<>();
  for(T s : list){
    result.add(f.apply(s));
  }
  return result;
}

// [7, 2, 6]
List<Integer> l = map(
Arrays.asList("lambdas", "in", "action"), (String s) -> s.length());
```

자바의 모든 형식은 참조형(reference type) (예를 들면 Byte, Integer, Object, List) 아니면 기본형에 해당한다. 하지만 제네릭 파라미터(예를 들면 Consumer\<T>의 T) 에는 참조형만 사용할 수 있다.

```java
List<Integer> list = new ArrayList<>();
for (int i = 300; i < 400; i++){
  list.add(i);
}
```

하지만 *이런 변환 과정은 비용이 소모된다.* 박싱한 값은 기본형을 감싸는 래퍼며 힙에 저장된다. 따라서 박싱한 값은 메모리에 더 소비하며 기본형을 가져올 때도 메모리를 탐색하는 과정이 필요하다.

자바8에서는 기본형을 입출력으로 사용하는 상황에서 오토박싱 동작을 피할 수 있도록 특별한 버전의 함수형 인터페이스를 제공한다. 예를 들어 아래 예제에서 IntPredicate는 1000이라는 값을 박싱하지 않지만, Predicate\<Integer>는 1000 이라는 값을 Integer 객체로 박싱.

```java
public interface IntPrediate{
  boolean test(int t);  
}

IntPredicate evenNumbers = (int i) -> i % 2 == 0;
evenNumber.test(1000);

Predicate<Integer> oddNumbers = (Integer i) -> i % 2 == 1;
oddNumber.test(1000);
```

![image-20190807212111567](http://ww3.sinaimg.cn/large/006tNc79gy1g5rdscest0j30uw0u0e88.jpg)



> **예외, 람다, 함수형 인터페이스의 관계**
>
> 함수형 인터페이스는 확인된 예외를 던지는 동작을 허용하지 않는다. 즉, 예외를 던지는 람다 표현식을 만들려면 확인된 예외를 선언하는 함수형 인터페이스를 직접 정의하거나 람다를  try/catch 블록으로 감싸야 한다.

## 5 형식 검사, 형식 추론, 제약 

- 형식 검사
- 같은 람다, 다른 함수형 인터페이스

> 특별한 void 호환 규칙
>
> //Predicate는 불린 반환값을 갖는다.
>
> Predicate\<String> p = s -> list.add(s);
>
> //Consumer는 void 반환값을 갖는다.
>
> Consumer\<String> b = s ->list.add(s);



*Tips*

다음 함수는 실행될 수 있을까?

`Objec o = () -> {System.out.println("Tricky example"); };`

NoNo~!

`Runnable r = () -> {System.out.println("Tricky example"); };`

요게 정답!



- 형식 추론 가능 -> `List<Apple> greenApples = filter(inventory, a -> "green".equals(a.getColor()));`

- 지역 변수 사용  

  ```java
  int portNumber = 1337;
  Runnable r = () -> System.out.println(portNumber);
  
  portNumber = 31337; // 권장하지 못한다. 왜냐하면 portNumber가 final 처럼 쓰여야 하기 때문이다.
  ```

  지역변수를 자유변수(free variable) 이라고 하고 이와 같은 동작을 **람다 캡처링(capturing lambda)** 

   그러나, 여기서도 한가지 제약이 있는데, 인스턴스 변수는 힙에 저장되는 반면 지역 변수는 스택에 저장된다. 람다에서 지역변수에 바로 접근 가능 할 수 있다는 가정 하에 람다가 스레드에서 실행 된다면 변수를 할당한 스레드가 사라져서 변수 할당이 해제되었는데도 람다를 실행하는 스레드에서는 해당 변수에 접근하려 할 수 있다. 따라서 자바 구현에서는 원래 변수에 접근을 허용하는 것이 아니라 자유 지역 변수의 복사본을 제공한다. 따라서 복사본의 값이 바뀌지 않아야 하므로 지역변수에는 한 번만 값을 할당해야 한다는 제약이 생긴 것.
  

## 6 메서드 레퍼런스 

```java
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

--------------------------------------------
// 메서드 레퍼런스를 활용하면
 inventory.sort(comparing(Apple::getWeight)); 
```



왜? 메서드 래퍼런스를 쓰는가?

*특정 메서드만을 호출하는 람다의 축약형*

예를 들어 람다가 '이 메서드를 직접 호출해' 라고 명령한다면 메서드를 어떻게 호출해야 하는지 설명을 참조하기보다는 직접 참조하는 것이 편리.

**즉, 가독성을 높일 수 있음.**

![image-20190808105854211](/Users/lenkim/Library/Application Support/typora-user-images/image-20190808105854211.png)



그 외에도

**생성자 레퍼런스**

```java
Supplier<Apple> c1 = Apple::new;
Apple a1 = c1.get()
---
Function<Integer, Apple> c2 = Apple::new;
Apple a2 = c2.apply(110);
---
List<Integer> weights = Arrays.asList(7,3,4,10);
List<Apple> apples = map(weights, Apple::new);
---
Bifunction<String, Integer, Apple> c3 = Apple:new;
Apple c3 = c3.apply("Green", 110);
---
  
```



이런 행위가 가져다주는 이점은 뭘까???

인스턴스화 하지 않고도 생성자에 접근할 수 있는 기능을 다양한 상황에 응용할 수 있다.

예를 들어 Map으로 생성자와 문자열값을 관련시킬 수 있다. 그리고 String과 Integer가 주어졌을 때 다양한 무게를 갖는 여러 종류의 과일을 만드는 giveMeFruit 만들 수 있다.

```java
static Map<String, Function<Integer, Fruit>> map = new HashMap<>();

static {
  map.put("Apple", Apple::new);
  map.put("orange", Orange::new);
  ...
}

public statuc Fruit giveMeFruit(String fruit, Integer weight){
  return map.get(fruit.toLowerCase())
    .apply(weight);
}
```

```java
// 활용 예시
public interface  TriFunction<T, U, V, R>{
        R apply(T t, U u, V v);
    }
    TriFunction<Integer, Integer, Integer, Color> colorTriFunction = Color::new;
```

## 7 람다, 메서드 레퍼런스 활용하기!

```java
public class Sorting {

    public static void main(String...args){

        // 1
        List<Apple> inventory = new ArrayList<>();
        inventory.addAll(Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red")));

        // [Apple{color='green', weight=80}, Apple{color='red', weight=120}, Apple{color='green', weight=155}]
        inventory.sort(new AppleComparator());
        System.out.println(inventory);

        // reshuffling things a little
        inventory.set(1, new Apple(30, "green"));
        
        // 2
        // [Apple{color='green', weight=30}, Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        inventory.sort(new Comparator<Apple>() {
            public int compare(Apple a1, Apple a2){ //동작이 파라미터화 되었다!
                return a1.getWeight().compareTo(a2.getWeight()); 
        }});
        System.out.println(inventory);

        // reshuffling things a little
        inventory.set(1, new Apple(20, "red"));
        
        // 3
        // [Apple{color='red', weight=20}, Apple{color='green', weight=30}, Apple{color='green', weight=155}]
        inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));
        System.out.println(inventory);
        
        // reshuffling things a little
        inventory.set(1, new Apple(10, "red"));
        
        // 4
        // [Apple{color='red', weight=10}, Apple{color='red', weight=20}, Apple{color='green', weight=155}]
        inventory.sort(comparing(Apple::getWeight));
        System.out.println(inventory);       
    }

    public static class Apple {
        private Integer weight = 0;
        private String color = "";

        public Apple(Integer weight, String color){
            this.weight = weight;
            this.color = color;
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

        public String toString() {
            return "Apple{" +
                   "color='" + color + '\'' +
                   ", weight=" + weight +
                   '}';
        }
    }

    static class AppleComparator implements Comparator<Apple> {
        public int compare(Apple a1, Apple a2){
            return a1.getWeight().compareTo(a2.getWeight());
        }
    }
}
```

<u>즉, 코드 자체로 'Apple을 weight 별로 비교해서 inventory를 sort하라.'</u>

## 8 람다 표현식을 조합할 수 있는 유용한 메서드 

`Comparator, Function, Predicate 같은 함수형 인터페이스`

간단한 여러 개의 람다 표현식을 조합해서 복잡한 람다 표현식을 만들 수 있다는 것.

예를 들어 두 프레디케이트를 조합해서 커다란 프레디 케이트를 만들 수 있고, 한 함수의 결과가 다른 함수의 입력이 되도록 두 함수를 조합할 수도 있다.



그럼 바로 **example**을 살펴보자.

**Comparator**

1. Comparator 조합  

   ```java
   Comparator<Apple> c = Comparator.comparing(Apple::getWeight);
   Comparator<Apple> c = Comparator.comparing(Apple::getWeight);
   
   Inventory.sort(comparing(Apple::getWeight).reversed()); //무게를 내림차순 정렬
   ```

2. Comparator 연결  
   만약 무게가 같은 두 사과가 존재한다면?  

   ```java
   inventory.sort(comparing(Apple::getWeight)
                 .reversed()
                 .thenComparing(Apple::getCountry)); // 두 사과의 무게가 같으면 국가별로 정렬
   ```

**Predicate 조합**  

Predicate는 negate, and, or 세 가지 메서드를 제공, 예를 들면 '빨간색이 아닌 사과'

```java
Predicate<Apple> notRedApple = redApple.negate(); // 기존 프레디케이트 객체 결과를 반전시킨 객체를 만든다.

//또는 and연산자를 이용해서 빨간색이면서 무거운 사과를 선택하도록 두 람다를 조합
Predicate<Apple> redAndHeavyApple = redApple.and(a -> a.getWeight() > 150);

//그 뿐만아니라
Predicate<Apple> redAndHeavyAppleOrGreen = redApple.and(a -> a.getWeight() > 150).or(a -> "green".equals(a.getColor()));

// 요 말 뜻은 and, or 등을 왼쪽에서 오른쪽으로 연결, 즉, a.or(b).and(c) -> (a || b ) && c 와 같다.
```

**Function 조합**

Function인터페이스는 Function인스턴스를 반환하는 andThen, compose 두 가지 디폴트 메서드를 제공.

`andThen`

: 주어진 함수를 먼저 적용한 결과를 다른 함수의 입력으로 전달하는 함수를 반환

```java
Function<Integer, Integer> f = x -> x + 1;
Function<Integer, Integer> g = x -> x * 2;
Function<Integer, Integer> h = f.andThen(g);
int result = h.apply(1) // 4결과 냄.
```

`compose`

: 인수로 주어진 함수를 먼저 실행한 다음에 그 결과를 외부 함수의 인수로 제공한다.

즉, f.andThen(g) 에서 andThen 대신에 compose를 사용하면 g(f(x))가 아니라 f( g(x ) ) 라는 수식이 됨.

```java
Function<Integer, Integer> f = x -> x + 1;
Function<Integer, Integer> g = x -> x * 2;
Function<Integer, Integer> h = f.compose(g);
int result = h.apply(1) // 3결과 냄.
```

## 9 비슷한 수학적 개념 

## 10 요약 

1. **람다 표현식** 은 익명 함수의 일종.  
   이름은 없지만, 파라미터 리스트, 바디, 반환 형식을 가지며 예외를 던질 수 있다.
2. 람다 표현식으로 간결한 코드를 구현할 수 있다.
3. 함수형 인터페이스는 하나의 추상 메서드만을 정의하는 인터페이스다.
4. 함수형 인터페이스를 기대하는 곳에서만 람다 표현식을 사용할 수 있다.
5. 람다 표현식을 이용해서 함수형 인터페이스의 추상 메서드를 즉석으로 제공할 수 있으며, 람다 표현식 전체가 함수형 인터페이스의 인스턴스로 취급된다.
6. java.util.function 패키지는 Predicate<T>, Function<T,R>, Supplier<T>, Consumer<T>, BinaryOperator<T> 등을 포함해서 자주 사용하는 다양한 함수형 인터페이스를 제공한다.
7. 자바 8은 Predicate<T> 와 Function<T,R> 같은 제네릭 함수형 인터페이스와 관련한 박싱 동작을 피할 수 있도록 IntPredicate, IntToLongFunction 등과 같은 기본형 특화 인터페이스도 제공한다.
8. 실행 어라운드 패턴(예를 들면 자원 할당, 자원 정리 등 코드 중간에 실행해야 하는 메서드에 꼭 필요한 코드)을 람다와 활용하면 유연성과 재사용성을 추가로 얻을 수 있다.
9. 람다 표현식의 기대 형식을 대상 형식이라고 한다.
10. 메서드 레퍼런스를 이용하면 기존의 메서드 구현을 재사용하고 직접 전달할 수 있다.
11. Comparator, Predicate, Function 같은 함수형 인터페이스는 람다 표현식을 조합할 수 있는 다양한 디폴트 메서드를 제공한다.