## CHAPTER 15 CompletableFuture와 리액티브 프로그래밍 컨셉의 기초



**배울 내용**

- Thread, Future 자바가 풍부한 동시성 API를 제공하도록 강요하는 진화의 힘
- 비동기 API
- 동시 컴퓨팅의 박스와 채널 뷰
- CompletableFuture 콤비네이터로 박스를 동적으로 연결
- 리액티브 프로그래밍용 자바 9 Flow API의 기초를 이루는 발생 구독 프로토콜
- 리액티브 프로그래밍과 리액티브 시스템

--

- 멀티코어 프로세서가 발전하면서 애플리케이션의 속도도 얼마나 멀티코어를 잘 활용할 수 있는지에 달렸다.
- 한 개의 큰 태스크를 병렬로 실행할 수 있는 개별 하위 태스크로 분리.
- 7장에서 설명함..**자바 7 의 fork/join framework 란** 무엇인가? 자바 8 병렬 스트림?
- **최근의 서비스가 쪼개지면서 매쉬업 형태가 많이 일어나고, 네트워크 통신이 증가**
  - 서비스의 응답을 기다리는 동안 연산이 블록되거나 귀중한 CPI 클록 사이클 자원을 낭비X
  - 포크-조인과 병렬 스트림은 한 태스크를 여러 하위 태스크로 나눠서 CPU의 다른 코어 또는 다른 머신에서 이들 하위 태스크를 병렬로 실행한다.
  - 이런 상황에서 Future 인터페이스 CompletableFuture 문제해결사.
  - 자바 9 에 추가된 발행 구독 프로토콜에 기반한 리액티브 프로그래밍 개념을 따르는 Flow API 는 정교한 프로그래밍 접근 방법 제공



## 1. 동시성을 구현하는 자바 지원의 진화

- 처음에는 자바의 Runnable과 Thread를 동기화된 클래스와 메서드를 이용해 잠갔다. 이후에는 좀더 표현력있는 동시성을 지원하는 특히 스레드 실행과 태스크 제출을 분리하는 ExecutorSerice 인터페이스. 높은 수준의 결과, 즉 Runnable, Thread의 변형을 반환하는 Callable\<T> and Future\<T>, 제네릭 등을 지원.

- 멀티코어 CPU 덕택에 개선된 동시성을 지원하는데, 자바7에서 분할 그리고 정복알고리즘 포크/조인 구현을 지원하는 java.util.concurrent.RecursiveTask 가 추가. 자바8에서는 스트림과 새로 추가된 람다 지원에 기반한 병렬 프로세싱 추가.
- 자바는 Future 를 조합하는 기능 - CompletableFuture, 자바 9에서는 분산 비동기 프로그래밍을 명시적으로 지원
- 자바 9에서는 발행-구독 프로토콜 (java.util.concurrent.flow 인터페이스 추가)로 이를 지원한다. 
- CompletableFuture 와 Flow 의 궁극적인 목표는 가능한한 동시에 실행할 수 있는 독립적인 태스크를 가능하게 만들면서 멀티코어 또는 여러 기기를 통해 제공되는 병렬성을 쉽게 이요



### 1.1 스레드와 높은 수준의 추상화

- **병렬 스트림 반복은 명시적으로 스레드를 사용하는 것에 비해 높은 수준의 개념**
  - 스트림을 이용해 스레드 사용 패턴을 **추상화**
  - 스트림으로 추상화하는 것은 디자인 패턴을 적용하는 것과 비슷하지만 대신 쓸모없는 코드가 라이브러리 내부로 구현되면서 복잡성도 줄어든다.



### 1.2 Executor와 스레드 풀

- Executor 프레임워크와 스레드 풀을 통해 스레드의 힘을 높은 수준으로 끌어올리는, 즉 자바 프로그래머가 태스크 제출과 실행을 분리할 수 있는 기능 제공

  - 스레드의 문제

    - 직접 운영체제 스레드에 접근하기 때문에 운영체제가 지원하는 스레드 수를 초과해 사용하면 자바 애플리케이션이 예상치 못한 방식으로 크래시될 수 있으므로 기존 스레드가 실행되는 상태에서 계속 새로운 스레드를 만드는 상황이 일어날 수 있다.

  - 스레드 풀 그리고 스레드 풀이 더 좋은 이유?

    - 스레드 풀에서 사용하지 않는 스레드로 제출된 태스크를 먼저 온 순서대로 실행한다.
    - 이들 실행이 종료되면 다시 풀로 반환
    - 장점은 하드웨어에 맞는 수의 태스크를 유지함과 동시에 수 천개의 태스크를 스레드 풀에 아무 오버헤드 없이 제출할 수 있다는 점.
    - 프로그래머는 Task(Runnable이나 Callable)를 제공하면 **스레드**가 이를 실행

  - 스레드 풀 그리고 스레드 풀이 나쁜 이유

    - k 스레드를 가진 스레드 풀은 오직 k만큼의 스레드를 동시에 실행. 초과로 제출된 태스크는 큐에 저장되며 이전에 태스크 중 하나가 종료되기 전까지는 스레드에 할당하지 않는다. 보통은 문제되지 않지만, 잠을 자거나 I/O를 기다리거나 네트워크 연결을 기다리는 태스크가 있다면 주의해야 한다. 
    - 일부 태스크가 잠을 자면, 스레드 풀에서는 잠을 자는 태스크를 제외한 스레드 만큼만 동작될 것이고 그럼 풀의 성능을 저하시킨다. 핵심은 블록할 수 있는 태스크는 스레드 풀에 제출하지 말아야 한다.

    <img src="https://raw.githubusercontent.com/LenKIM/images/master/2024-06-18/IMG_4210.JPG" alt="IMG_4210" style="zoom:70%;" />

    - 중요한 코드를 실행하는 스레드가 죽는 일이 발생하지 않도록 해야 하는데, 풀의 워커 스레드가 만들어진 다음 다른 태스크 제출을 기다리면서 종료되지 않은 상태일 수 있는데?? **무슨말이지?** - gracefulshutdown()



### 1.3 스레드의 다른 추상화: 중첩되지 않는 메서드 호출

- 테스크나 스레드가 메서드 호출 안에서 시작되면 그 메서드 호출은 반환하지 않고 작업이 끝나기를 기다렸다. 다시 말해 스레드 생성과 join()이 한쌍처럼 중첩된 메서드 호출 내에 추가된다. 이를 엄격한(strict) 포크/조인 이라 부른다.
- 시작된 태스크를 내부 호출이 아니라 외부 호출에서 종료하도록 기다리는 좀 더 여유로운 방식의 포크/조인을 사용해도 안전하다.

<img src="https://raw.githubusercontent.com/LenKIM/images/master/2024-06-18/image-20240618232856043.png" alt="image-20240618232856043" style="zoom:50%;" />



### 1.4 스레드에 무엇을 바라는가?

- 일반적으로 모든 하드웨어 스레드를 활용해 병렬성의 장점을 극대화하도록 프로그램 구조를 만드는 것. 즉, 프로그램을 작은 태스크 단위로 구조화하는 것이 목표.
- 병렬 스트림 처리와 포크/조인을 for 루프와 분할 그리고 정복 알고리즘을 처리하는 방법. 나머지는 스레드를 조작하는 복잡한 코드를 구현하지 않고 메서드를 호출하는 방법 살펴보기

## 2. 동기 API와 비동기 API

```java
class ThreadExample {
  ...
}
```

**future 를 통해 좀더 단순화**



문제의 해결은 비동기 API 라는 기능으로 API를 바꿔서 해결할 수 있다.

- 첫번째는, 자바 5에 소개된 Future 는 자바 8 의 CompletableFuture로 이들을 조합할 수 있게 되면서 기능 풍부
- 두번째는 발행-구독 프로토콜에 기반한 자바 9의 java.util.concurrent.Flow 인터페이스를 이용하는 방법

### 2.1 Future 형식 API

```java
Future<Integer> f(int x);
Future<Integer> g(int x);

--
Future<Integer> y = f(x);
Future<Integer> z = g(x);
System.out.println(y.get() + z.get());
```

위 방식을 두 가지 이유에서 추천 X

- 다른 상황에서는 g에도 future 형식이 필요할 수 있으므로 API 형식을 통일하는게 바람직
- 병렬 하드웨어로 프로그램 실행 속도를 극대화하려면 여러 작은 하지만 합리적인 크기의 태스크를 나누는 것이 좋다.

### 2.2 리액티브 형식 API

- 두번째 대안에서 핵심은 f,g 의 시그니처를 바꿔서 콜백 형식의 프로그래밍을 이용하는 것

  > void f(int x, IntConsumer dealWithResult);

- f 에 추가인수로 콜백(람다)을 전달해서 f의 바디에서는 return 문으로 결과를 반환하는 것이 아니라 결과가 준비되면 이를 람다로 호출하는 태스크를 만드는 것.

```java
static class CallbackStyleExample {
        public static void main(String[] args) {
            int x = 1337;
            Result result = new Result();

            f(x, (int y) -> {
                result.left = y;
                System.out.println(result.left + result.right);
            });

            g(x, (int z) -> {
                result.right = z;
                System.out.println(result.left + result.right);
            });
        }

        private static void g(int x, IntConsumer dealWithResult) {
            dealWithResult.accept(x );
        }


        private static void f(int x, IntConsumer dealWithResult) {
            dealWithResult.accept(x);
        }

        private static class Result {
            int left;
            int right;
        }
    }
```

### 2.3 잠자기(그리고 기타 블로킹 동작)는 해로운 것으로 간주

- `sleep()` 메서드는 스레드는 잠들어도 여전히 시스템 자원을 점유한다.

```java
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class ScheduledExecutorServiceExample {

    /**
     * work1();
     * Thread.sleep(10_000);
     * work2();
     */
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        work1();
        scheduledExecutorService.schedule(ScheduledExecutorServiceExample::work2, 10, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();
    }

    private static void work2() {
        System.out.println("work2");
    }

    private static void work1() {
        System.out.println("work1");
    }
}
```

무슨 차이가 있을까?

- 주석의 경우, 스레드 풀 큐에 추가되며 나중에 차례가 되면 실행. 하지만 코드가 실행되면 워커 스레드를 점유한 상태에서 아무것도 하지 않고 10초를 잔다. 그리고 깨어나서 work2를 실행한 다음 작업을 종료하여 워커 스레드를 해제

- 코드의 경우, work1 실행하고 종료. 그리고 10초뒤에 work2 실행.

- **코드가 더 좋은 이유는?**
  - A가 자는 동안 귀중한 스레드 자원을 점유하는 반면 B는 다른 작업이 실행될 수 있도록 허용한다.
  - 태스크가 실행되면 귀중한 자원을 점유하므로 태스크가 끝나서 자원을 해제하기 전까지 태스크를 계속 실행해야 한다. 태스크를 블록하는 것보다는 다음 작업을 태스크로 제출하고 현재 태스크는 종료하는 것이 바람직하다.
  - 가능하다면 I/O 작업에 이 원칙을 적용하는게 좋다.

### 2.4 현실성 확인

-  새로운 시스템을 설계할 때 시스템을 많은 작은 동시 실행되는 태스크로 설계해서 블록할 수 있는 모든 동작을 비동기 호출로 구현하다면 병렬 하드웨어를 최대한 활용.
- 그러나 '모든 것은 비동기' 라는 설계 원칙을 어겨야 한다.

### 2.5 비동기 API 에서 예외는 어떻게 처리하는가?

- Future 나 리액티브 형식의 비동기 API 에서 호출된 메서드의 실제 바디는 별도의 스레드에서 호출되며 이때 발생하는 어떤 에러는 이미 호출자의 실행 범위와는 관계가 없는 상황
- 예상치 못한 일이 일어나면 예외를 발생시켜 다른 동작이 실행되어야 한다. Future 의 경우, exceptionally() 같은 메서드 제공.



## 3. 박스와 채널 모델

- 동시성 모델을 잘 설계학 개념화하려면 박스와 채널 모델(box-and-channel model) 을 이해하자.

![IMG_4212](https://raw.githubusercontent.com/LenKIM/images/master/2024-06-23/IMG_4212.JPG)

## 4. CompletableFutue와 콤비네이터를 이용한 동시성

```java
class CFComplete {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService ex
                = Executors.newFixedThreadPool(10);

        int x = 1337;
        CompletableFuture<Integer> a = new CompletableFuture<>();
        ex.submit(() -> a.complete(f(x)));
        int b = g(x);

        System.out.println(a.get() + b);
        ex.shutdown();
    }

    private static Integer f(int x) {
        return x;
    }

    private static int g(int x) {
        return x;
    }
}
```

- 위와 같이 할 경우, f(x)의 실행이 끝나지 않는 상황이면 get()을 기다려야 하므로 프로세싱 낭비 발생

- 이럴 때 사용할 수 있는 것이 `CompletableFuture<V> thenCombine(CompletableFuture<U> other, BiFunction<T, U, V) fn)`

  

- ```java
  CompletableFuture<Integer> b = new CompletableFuture<>();
  CompletableFuture<Integer> a = new CompletableFuture<>();
  CompletableFuture<Integer> c = a.thenCombine(b, (a1, b1) -> a1 + b1);
  ex.submit(() -> a.complete(f(x)));
  ex.submit(() -> b.complete(g(x)));
  System.out.println(c.get());
  ex.shutdown();
  ```

- 기존에 문제가 될 수 있었던 get()을 기다리는 문제가 발생하지 않는다.

## 5. 발행-구독 그리고 리액티브 프로그래밍

- Future 와 CompletableFuture은 독립적 실행과 병렬성이라는 정식적 모델에 기반. 연산이 끝나면 get()으로 Future 결과 가져올 수 있다. 따라서 Future 는 **한 번**만 실행해 결과를 제공
  - **구독자**가 구독할 수 있는 **발행자**
  - 이 연결을 구독(subscription)이라 한다.
  - 이 연결을 이용해 메시지(또는 **이벤트**로 알려짐)를 전송한다.



### 5.1 두 플로를 합치는 예제

```java
class SimpleCell {
    private int value = 0;
    private String name;

    public SimpleCell(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        
        SimpleCell c1 = new SimpleCell("C1");
        SimpleCell c2 = new SimpleCell("C2");
        
    }
}
```

c1 또는 c2의 값이 바뀌었을 때 c3가 두 값을 더하도록 어떻게 지정할 수 있을까?

c1과 c2에 이벤트가 발생했을 때 c3를 구독하도록...!

```java
interface Publisher<T> {
  void subscribe(Subscriber<? super T> subscriber);
}

interface Subscriber<T> {
  void onNext(T t);
}
```

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

import static java.util.concurrent.Flow.Subscriber;
import static java.util.concurrent.Flow.Subscription;

class SimpleCell implements Flow.Publisher<Integer>, Flow.Subscriber<Integer> {
    private int value = 0;
    private String name;
    private List<Subscriber> subscriberList = new ArrayList<>();

    public SimpleCell(String name) {
        this.name = name;
    }

    public static void main(String[] args) {

        SimpleCell c1 = new SimpleCell("C1");
        SimpleCell c2 = new SimpleCell("C2");
        SimpleCell c3 = new SimpleCell("C3");

        c1.subscribe(c3);
        c1.onNext(10);
        c2.onNext(20);

    }

    @Override
    public void subscribe(Flow.Subscriber<? super Integer> subscriber) {
        subscriberList.add(subscriber);
    }

    private void notifyAllSubscribers() {
        subscriberList.forEach(subscriber -> subscriber.onNext(value));
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("onSubscribe");
    }


    @Override
    public void onNext(Integer newValue) {
        this.value = newValue;
        System.out.println(this.name + ":" + this.value);
        notifyAllSubscribers();
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("onError");
    }

    @Override
    public void onComplete() {
        System.out.println("onComplete");
    }
}
```



- 기존의 온도계 예제에서 온도계가 매 초마다 온도를 보고했는데 기능이 업그레이드되면서 매밀리초마다 온도계를 보고한다고 가정하자. 우리 프로그램은 이렇게 빠른 속도로 발생하는 이벤트를 아무 문제 없이 처리할 수 있을까? 마찬가지로 모든 SMS 메시지를 폰으로 제공하는 발행자에 가입하는 상황을 생각해보자. 처음에 약간의 SMS 메시지가 있는 새 폰에서는 가입이 잘 동작할 수 있지만 몇년 후에는 매 초마다 수천 개의 메시지가 onNext로 전달된다면 어떤 일이 일어날까? 이런 상황이 pressure(압력)

### 5.2 역압력(Backpressure)

- Subscriber(onNext, onError, onComplete 메서드 포함)를 어떻게 Publisher에게 전달해 발행자가 필요한 메서드를 호출할 수 있는지 살펴봄

- 정보의 흐름 속도를 역압력(흐름 제어)으로 제어 즉 Subscriber에서 Publisher로 정보를 요청해야 할 필요가 있다.Publisher는 여러 Subscriber를 갖고 있으므로 역압역 요청이 한 연결에만 영향을 미쳐야 한다는 것이 문제가 될 수 있다. 그래서 자바9 Flow API 에는 `void onSubscribe(Subscription subscription)` 이것은 Publisher 와 Subscriber 사이에 채널이 연결되면 첫 이벤트로 이 메서드 호출. 

- ```java
  interface Subscription {
  	void cancel();
  	void request(long n);
  }
  ```

## 6. 리액티브 시스템 vs 리액티브 프로그래밍

리액티브 시스템은 런타임 환경이 변화에 대응하도록 전체 아키텍처가 설계된 프로그램. 

- 반응성 / 회복성 / 탄력성이 핵심인데
- 리액티브 프로그래밍 Flow API 는 이것을 활용할 수 있다.
- 네번째이자 마지막 속성 즉 메시지 주도(message-driven)속성 반영
