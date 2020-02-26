# 리팩토링 / 테스팅 / 디버깅



- 람다 표현식으로 코드 리펙토링하기
- 람다 표현식으로 객체지향 설계 패턴에 미치는 영향
- 람다 표현식 테스팅
- 람다 표현식과 스트림 API 사용 코드 디버깅
- 람다표현식으로 전략 / 템플릿 메서드 / 옵저버 / 의무 체인 / 팩토리 등의 객체 지향 디자인 패턴을 어떻게 간소화할 수 있는지 살펴보기.



**코드 가독성이 좋다는 것**은 추상적인 표현이므로 이를 정확하게 정의하기 어렵다. 일반적으로 코드 가독성이 좋다는 것은 '어떤 코드를 다른 사람도 쉽게 이해할 수 있음'을 의미, 즉 코드 가독성을 개선한다는 것은 우리가 구현한 코드를 다른 사람이 쉽게 이해하고 유지보수 할 수 있게 만드는 것.



### 1. 익명 클래스를 람다 표현식으로 리팩토링 하기

```java
Runnable r1 = new Runnable(){
  public void run(){
    System.out.println("Hello");
  }
}

Runnable r2 = () ->  System.out.println("Hello");
```

익명 클래스에서 사용한 this와 super는 람다 표현식에서 다른 의미를 갖음.

익명 클래스에서 this는 익명클래스 자신을 가리키지만 람다에서 this는 람다를 감싸는 클래스를 가리킨다.

익명 클래스를 감싸고 있는 클래스의 변수를 가릴 수 있다(shadow variable)  하지만 람다 표현식에서는 변수를 가릴 수 없다.

```java
int a = 10;
Runnable r1 = () -> {
  int a = 2;
  System.out.println("Hello");
};

//위 코드는 동작하지 않음
```



익명 클래스를 람다 표현식로 바꾸면 메서드를 호출할 때 Runnable과 Task 모두 대상 형식이 될 수 있으므로 모호함 발생

```java
doSomething(() -> System.out.println("Hello"));
```

이럴 때는 명시적 형변환(Task) 를 이용해 모호함을 제거

```java
doSomething((Task)() -> System.out.println("Hello"));
```

### 2. 람다 표현식을 메서드 레퍼런스로 리팩토링 하기



메서드 레퍼런스를 활용하면서 comparing과 maxBy와 같은  `정적 헬퍼 메서드`를 활용하는 것이 좋다.

```java
inventory.sort(
	(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

를 다음과 같이 수정한다.

inventory.sort(comparing(Apple::getWeight));
```

최댓값이나 합계를 계산할 때 람다 표현식과 저수준 리듀싱 연산을 조합하는 것보다 Collectors API를 사용하면 코드의 의도가 더 명확해진다.

```java
int totalCalories = menu.stream().map(Dish::getCalories)
  															 .reduce(0, (c1, c2) -> c1 + c2);

를 다음과 같이 수정한다.

int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
```

## 01. 코드 유연성 개선하기.



## 함수형 인터페이스 적용

 람다 표현식을 이용하려면 함수형 인터페이스가 필요합니다. 따라서 함수형 인터페이스를 코드에 추가해야 하는데, 이 때 사용될 수 패턴으로

- 조건부 연기 실행 (Conditional deferred execution)
- 실행 어라운드 (execute around)

즉, 두 가지 자주 사용하는 패턴이 있다.



#### 조건부 연기 실행

```java
if(logger.isLoggable(Log.FINGER)){
  logger.finger("Problem: " + generate());
}
```

이런 코드가 있다고 하자. 이런 코드의 문제점은?

- logger 의 상태가 isLoggable이라는 메서드에 의해 클라이언트 코드로 노출
- 메시지를 로깅할 때마다 logger 객체의 상태를 매번 확인해야 할까? 코드가 지저분



그러면? 

> logger.log(Level.FINER, "Problem: " + generate())

이런 코드가 정답일까??



람다 표현식을 사용하면

*특정 조건에서만 메시지가 생성될 수 있도록 메시지 생성 과정을 연기(defer) 해야 한다. 자바 8에서는 logger문제를 해결할 수 있도록 Supplier를 인수로 갖는 오버로드된 log메서드를 제공 새로  추가된 log 메서드의 시그니처이다.*

```java
public void log(Level level, Supplier<String> msgSupplier){
  if(logger.isLoggable(level)){
    log(level, msgSupplier.ger)
  }
}

logger.log(Level.FINER, () -> "Problem " + generate()) 로 변경 될 수 있다.
```

위 문제점이였던 클라 코드의 노출을 막고, 가독성이 높아진다.



#### 실행 어라운드

매번 같은 준비, 종료 과정을 반복적으로 수행하는 코드가 있다면 이를 람다로 변환할 수 있다.

```java
String oneLine = processFile((BufferedReader b) -> b.readLine());
String twoLine = processFile((BufferedReader b) -> b.readLine() + b.readLine());

public static String processFile(BufferedReaderProcessor p) throws IOException {
  try(BufferedReader br = new BufferedReader(new FileReader("java.txt"))){
    return p.process(br); //인수로 전달된 BufferedReaderProcessor를 실행
  }
}

// IOException을 던질 수 있는 람다의 함수형 인터페이스
public interface BufferedReaderProcessor { 
  String process(BufferedReader br) throws IOException;
}
```

## 02. 객체지향 디자인 패턴 리펙토링 하기.



- 전략 패턴
- 템플릿 메서드
- 옵저버
- 의미 체인
- 팩토리



### 전략 패턴

Predicate 를 활용해서 전략 패턴 구현

```java
public class StrategyMain {

    public static void main(String[] args) {
        // old school
        Validator v1 = new Validator(new IsNumeric());
        System.out.println(v1.validate("aaaa"));
        Validator v2 = new Validator(new IsAllLowerCase ());
        System.out.println(v2.validate("bbbb"));


        // with lambdas
        Validator v3 = new Validator((String s) -> s.matches("\\d+"));
        System.out.println(v3.validate("aaaa"));
        Validator v4 = new Validator((String s) -> s.matches("[a-z]+"));
        System.out.println(v4.validate("bbbb"));
    }

    interface ValidationStrategy {
        public boolean execute(String s);
    }

    static private class IsAllLowerCase implements ValidationStrategy {
        public boolean execute(String s){
            return s.matches("[a-z]+");
        }
    }
    static private class IsNumeric implements ValidationStrategy {
        public boolean execute(String s){
            return s.matches("\\d+");
        }
    }

    static private class Validator{
        private final ValidationStrategy strategy;
        public Validator(ValidationStrategy v){
            this.strategy = v;
        }
        public boolean validate(String s){
            return strategy.execute(s); }
    }
}

```



### 템플릿 메서드

알리즘의 개요를 제시한 다음에 일부를 고칠 수 있는 유연함을 제공해야 할 때 템플릿 메서드 디자인 패턴을 사용한다. 다시말해, 템플릿 메서드는 '이 알고리즘을 사용하고 싶은데 그대로는 안 되고 조금 고쳐야 하는' 상황에 적합하다.



이전에는

```java
package chap8;


abstract class OnlineBanking {
    public void processCustomer(int id){
        Customer c = Database.getCustomerWithId(id);
        makeCustomerHappy(c);
    }
    abstract void makeCustomerHappy(Customer c);


    // dummy Customer class
    static private class Customer {}
    // dummy Datbase class
    static private class Database{
        static Customer getCustomerWithId(int id){ return new Customer();}
    }
}

// 그 뒤에 OnlineBaning을 상속받아 makeCustomerHappy를 구현해야 한다.
```



그러나 람다 표현식을 활용하게 된다면,

```java
import java.util.function.Consumer;


public class OnlineBankingLambda {

    public static void main(String[] args) {
        new OnlineBankingLambda().processCustomer(1337, (Customer c) -> System.out.println("Hello!"));
    }

    public void processCustomer(int id, Consumer<Customer> makeCustomerHappy){
        Customer c = Database.getCustomerWithId(id);
        makeCustomerHappy.accept(c);
    }

    // dummy Customer class
    static private class Customer {}
    // dummy Database class
    static private class Database{
        static Customer getCustomerWithId(int id){ return new Customer();}
    }
}

```

와 같이 상속을 받지 않고 구현할 수 있다.



### 옵저버

어떤 이벤트가 발생 했을 때 한 객체(주제- subject라 불리는) 가 다른 객체 리스트(**옵저버** 라 불리는)에 자동으로 알림을 보내야 하는 상황에서 옵저버 디자인 패턴을 사용



```java
import java.util.ArrayList;
import java.util.List;


public class ObserverMain {

    public static void main(String[] args) {
        Feed f = new Feed();
        f.registerObserver(new NYTimes());
        f.registerObserver(new Guardian());
        f.registerObserver(new LeMonde());
        f.notifyObservers("The queen said her favourite book is Java 8 in Action!");


        Feed feedLambda = new Feed();

        feedLambda.registerObserver((String tweet) -> {
            if(tweet != null && tweet.contains("money")){
                System.out.println("Breaking news in NY! " + tweet); }
        });
        feedLambda.registerObserver((String tweet) -> {
            if(tweet != null && tweet.contains("queen")){
                System.out.println("Yet another news in London... " + tweet); }
        });

        feedLambda.notifyObservers("Money money money, give me money!");

    }


    interface Observer{
        void inform(String tweet);
    }

    interface Subject{
        void registerObserver(Observer o);
        void notifyObservers(String tweet);
    }

    static private class NYTimes implements Observer{
        @Override
        public void inform(String tweet) {
            if(tweet != null && tweet.contains("money")){
                System.out.println("Breaking news in NY!" + tweet);
            }
        }
    }

    static private class Guardian implements Observer{
        @Override
        public void inform(String tweet) {
            if(tweet != null && tweet.contains("queen")){
                System.out.println("Yet another news in London... " + tweet);
            }
        }
    }

    static private class LeMonde implements Observer{
        @Override
        public void inform(String tweet) {
            if(tweet != null && tweet.contains("wine")){
                System.out.println("Today cheese, wine and news! " + tweet);
            }
        }
    }

    static private class Feed implements Subject{
        private final List<Observer> observers = new ArrayList<>();
        public void registerObserver(Observer o) {
            this.observers.add(o);
        }
        public void notifyObservers(String tweet) {
            observers.forEach(o -> o.inform(tweet));
        }
    }
}
```



### 의무 체인

작ㅂ 처리 객체의 체인(동작 체인 등)을 만들 때는 의무 체인 패턴을 사용한다. 한 객체가 어떤 작업을 처리한 다음에 다른 객체로 결과를 전달하고, 다른 객체도 해야 할 작업을 처리한 다음에 또 다른 객체로 전달하는 식이다.

```java
import java.util.function.Function;
import java.util.function.UnaryOperator;


public class ChainOfResponsibilityMain {

    public static void main(String[] args) {
        ProcessingObject<String> p1 = new HeaderTextProcessing();
        ProcessingObject<String> p2 = new SpellCheckerProcessing();
        p1.setSuccessor(p2);
        String result1 = p1.handle("Aren't labdas really sexy?!!");
        System.out.println(result1);


        UnaryOperator<String> headerProcessing =
                (String text) -> "From Raoul, Mario and Alan: " + text;
        UnaryOperator<String> spellCheckerProcessing =
                (String text) -> text.replaceAll("labda", "lambda");
        Function<String, String> pipeline = headerProcessing.andThen(spellCheckerProcessing);
        String result2 = pipeline.apply("Aren't labdas really sexy?!!");
        System.out.println(result2);
    }

    static private abstract class ProcessingObject<T> {
        protected ProcessingObject<T> successor;

        public void setSuccessor(ProcessingObject<T> successor) {
            this.successor = successor;
        }

        public T handle(T input) {
            T r = handleWork(input);
            if (successor != null) {
                return successor.handle(r);
            }
            return r;
        }

        abstract protected T handleWork(T input);
    }

    static private class HeaderTextProcessing
            extends ProcessingObject<String> {
        public String handleWork(String text) {
            return "From Raoul, Mario and Alan: " + text;
        }
    }

    static private class SpellCheckerProcessing
            extends ProcessingObject<String> {
        public String handleWork(String text) {
            return text.replaceAll("labda", "lambda");
        }
    }
}
```



### 팩토리

```java
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


public class FactoryMain {

    public static void main(String[] args) {
        Product p1 = ProductFactory.createProduct("loan");

        Supplier<Product> loanSupplier = Loan::new;
        Product p2 = loanSupplier.get();

        Product p3 = ProductFactory.createProductLambda("loan");

    }

    static private class ProductFactory {
        public static Product createProduct(String name){
            switch(name){
                case "loan": return new Loan();
                case "stock": return new Stock();
                case "bond": return new Bond();
                default: throw new RuntimeException("No such product " + name);
            }
        }

        public static Product createProductLambda(String name){
            Supplier<Product> p = map.get(name);
            if(p != null) return p.get();
            throw new RuntimeException("No such product " + name);
        }
    }

    static private interface Product {}
    static private class Loan implements Product {}
    static private class Stock implements Product {}
    static private class Bond implements Product {}

    final static private Map<String, Supplier<Product>> map = new HashMap<>();
    static {
        map.put("loan", Loan::new);
        map.put("stock", Stock::new);
        map.put("bond", Bond::new);
    }
}

```

## 정보 로깅

`peek` 이라는 스트림 연산을 활용. peek 은 스트림의 각 요소를 소비한 것처럼 동작을 실행한다. 하지만 forEach처럼 실제로 스트림의 요소를 소비하지는 않는다.



