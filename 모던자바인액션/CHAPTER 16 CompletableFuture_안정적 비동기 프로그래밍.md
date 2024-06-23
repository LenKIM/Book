# 16. CompletableFuture: 안정적 비동기 프로그래밍

>  배울 내용
>
> 1. 비동기 작업을 만들고 결과 얻기
> 2. 비블록 동작으로 생산성 높이기
> 3. 비동기 API 설계와 구현
> 4. 동기 API를 비동기적으로 소비하기
> 5. 두 개 이상의 비동기 연산을 파이프라인으로 만들고 합치기
> 6. 비동기 작업 완료에 대응



## 1. Future의 단순 활용

**future로 오래 걸리는 작업을 비동기적으로 실행하기**

```java
 @Test
    void name() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Double> future = executor.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return doSomeLongComputation();
            }

            private Double doSomeLongComputation() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
                System.out.println("doSomeLongComputation");
                return 100.0;

            }
        });
        doSomeThingElse();
        try {
            future.get(6, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void doSomeThingElse() {
        System.out.println("doSomeThingElse");
    }
```

- 위 시나리오에 어떤 문제가 있을까? 오래 걸리는 작업이 영원히 끝나지 않으면 어떻게 될까? 작업이 끝나지 않는 문제가 있을 수 있어 get 메서드를 오버로드해서 우리 스레드에 대기할 최대 타임아웃 시간을 설정하는게 좋다.



### 1.1 Future 제한

- '오래걸리는 A라는 계산이 끝나면 그 결과를 다른 오래 걸리는 B로 전달하시오. 그리고 B의 결과가 나오면 다른 질의의 결과와 B의 결과를 조합하시오' 와 같은 요구사항을 쉽게 구현할 수 있어야 한다. Future 로 이와 같은 동작을 구현하는 것은 쉽지 않다.
  - 두 개의 비동기 계산 결과를 하나로 합친다. 두 가지 계산 결과는 서로 독립적일 수 있으며 또는 두 번째 결과에 첫번째 결과에 의존하는 상황일 수 있다.
  - Future 집합이 실행하는 모든 태스크의 완료를 기다린다
  - Future 집합에서 가장 빨리 완료되는 태스크를 기다렸다가 결과를 얻는다.
  - 프로그램적으로 Future를 완료시킨다(즉, 비동기 동작에 수동으로 결과 제공)
  - Future 완료 동작에 반응한다.(즉, 결과를 기다리면서 블록하지 않고 결과가 준비되었다는 알림을 받은 다음에 Future 의 결과로 원하는 추가 동작을 수행할 수 있음)





### 1.2 CompletableFuture 로 비동기 애플리케이션 만들기

- 고객에게 비동기 API를 제공하는 방법을 배운다
- 동기 API를 사용해야 할 때 코드를 비블록으로 만드는 방법을 배운다. 두 개의 비동기 동작을 파이프라인으로 만드는 방법과 두 개의 동작 결과를 하나의 비동기 계산으로 합치는 방법을 본다.
- 비동기 동작의 완료에 대응하는 방법을 배운다.



## 16.2 비동기 API 구현

```java
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class asyncTest {

    public static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  double getprice(String product) {
        return calculatePrice(product);
    }

    private double calculatePrice(String product) {
        delay();
        return Math.random() * product.charAt(0) + product.charAt(1);
    }

    @Test
    void xxxxx() throws ExecutionException, InterruptedException {
        long l = System.nanoTime();
        Future<Double> apple = getPriceAsync("apple");
        System.out.println((System.nanoTime() - l) / 1_000_000);
        System.out.println("Invocation returned after " + (System.nanoTime() - l) / 1_000_000 + " msecs");

        doSomethingElse();
        Double v = apple.get();
        System.out.println("Price is " + v);
        System.out.println((System.nanoTime() - l) / 1_000_000);
        System.out.println("Price returned after " + (System.nanoTime() - l) / 1_000_000 + " msecs");
    }

    private void doSomethingElse() {
        System.out.println("doSomethingElse");
    }

    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            double price = calculatePrice(product);
            futurePrice.complete(price);
        }).start();
        return futurePrice;
    }
}

```

future 가 즉시 반환되는 것을 확인할 수 있다.

```
0
Invocation returned after 1 msecs
doSomethingElse
Price is 188.50094885144267
1011
Price returned after 1011 msecs
```

## 2.2 에러 처리 방법

가격을 계산하는 동안 에러가 발생하면 어떻게 될까? 예외가 발생하면 해당 스레드에만 영향을 미친다. 즉, 에러가 발생하면 가격 계산은 계속 진행되며 일의 순서가 꼬인다. 결과적으로 클라이언틑 get 메서드가 반환될때까지 영원히 기다리게 될 수도 있다.

```java
public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception ex) {
                futurePrice.completeExceptionally(ex);
            }
        }).start();
        return futurePrice;
    }
```

**팩토리 메서드 supplyAsync로 CompletableFuture 만들기**

```java
CompletableFuture.supplyAsync(() -> calculatePrice(product))
                .thenAccept(System.out::println);
```

## 16.3 비블록 코드 만들기

모든 상점에 순차적으로 정보를 요청하는 경우..

getPrice(product) / getPrice(product) / getPrice(product)...



## 16.4 비동기 작업 파이프라인 만들기

 우리와 계약을 맺은 모든 상점이 하나의 할인 서비스를 사용하기로 했다고 가정하자. 할인 서비스에서는 서로 다른 할인율을 제공하는 다섯 가지 코드 제공

`Discount.Code`

```java
public class Discount {
  public enum Code {
    NONE(0), SILVER(5), GOLD(10), PLATIUM(15), DIAMOND(20);
    
    private final int percentage;
    
    Code(int percentage) {
      this.percentage = percentage;
    }
  }
  // 
}
```

