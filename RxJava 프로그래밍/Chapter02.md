```
Chapter 2 Observable 처음 만들기
__2.1 Observable 클래스
____2.1.1 just() 함수
____2.1.2 subscribe() 함수와 Disposable 객체
____2.1.3 create() 함수
____2.1.4 fromArray() 함수
____2.1.5 fromIterable() 함수
____2.1.6 fromCallable() 함수
____2.1.7 fromFuture() 함수
____2.1.8 fromPubilsher() 함수
__2.2 Single 클래스
____2.2.1 just() 함수
____2.2.2 Observable에서 Single 클래스 사용
____2.2.3 Single 클래스의 올바른 사용 방법
__2.3 Maybe 클래스
__2.4 뜨거운 Observable
__2.5 Subject 클래스
____2.5.1 AsyncSubject 클래스
____2.5.2 BehaviorSubject 클래스
____2.5.3 PublishSubject 클래스
____2.5.4 ReplaySubject 클래스
__2.6 ConnectableObservable 클래스
__2.7 마치며
```

Observable클래스란 무엇일까??

이는 세분화하여 **Observable, Maybe, Flowable** 클래스로 구분되 사용.

Observable은 옵서버 패턴을 구현. 옵서버 패턴은 객체의 상태 변화를 관찰하는 관찰자 목록을 객체에 등록합니다. 그리고 상태 변화가 있을 때마다 메서드를 호출하여 객체가 직접 목록의 각 옵서버에게 변화를 알려줍니다. 라이프 사이클은 존재하지 않으며 보통 단일 함수를 통해 변화만 알립니다.  

`Observed라는 단어가 관찰을 통해서 얻은 결과를 의미한다면 Observable은 현재는 관찰되지 않았지만 이론을 통해서 앞으로 관찰할 가능성을 의미한다.`  

Observable은 세 가지의 알림을 구독자에게 전달.  
- onNext :  Observable이 데이터의 발행을 알림.
- onComplete : 모든 데이터의 발행을 완료했음을 알림. 해당 이벤트는 단 한번만 발생하며, 발생한 후에는 더 이상 onNext이벤트가 발생해선 안된다.
- onError : Observable에서 어떤 이유로 에러가 발생했음을 알립니다. onError 이벤트가 발생하면 이후에 onNext및 onComplete 이벤트가 발생하지 않습니다. 즉, Observable의 실행을 종료합니다.


##### 1. just() 함수
##### 2. subscribe() 함수와 Disposable 객체
 **RxJAVA는 내가 동작시키기 원하는 것을 사전에 정의해둔 다음 실제 그것이 실행되는 시점을 조절할 수 있습니다.** 이때 사용하는 것이 subscribe()함수. Observable은 just()등의 팩토리 함수로 데이터 흐름을 정의한 후 subscribe()함수를 호출해야 실제로 데이터를 발행합니다.

  ```Java
@SchedulerSupport(SchedulerSupport.NONE)
  public final Disposable subscribe() {
      return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer());
  }

@CheckReturnValue
 @SchedulerSupport(SchedulerSupport.NONE)
 public final Disposable subscribe(Consumer<? super T> onNext) {
     return subscribe(onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer());
 }

@CheckReturnValue
 @SchedulerSupport(SchedulerSupport.NONE)
 public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
     return subscribe(onNext, onError, Functions.EMPTY_ACTION, Functions.emptyConsumer());
 }

 @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
            Action onComplete) {
        return subscribe(onNext, onError, onComplete, Functions.emptyConsumer());
    }

    @CheckReturnValue
       @SchedulerSupport(SchedulerSupport.NONE)
       public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
               Action onComplete, Consumer<? super Disposable> onSubscribe) {
           ObjectHelper.requireNonNull(onNext, "onNext is null");
           ObjectHelper.requireNonNull(onError, "onError is null");
           ObjectHelper.requireNonNull(onComplete, "onComplete is null");
           ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");

           LambdaObserver<T> ls = new LambdaObserver<T>(onNext, onError, onComplete, onSubscribe);

           subscribe(ls);

           return ls;
       }

       @SchedulerSupport(SchedulerSupport.NONE)
       @Override
       public final void subscribe(Observer<? super T> observer) {
           ObjectHelper.requireNonNull(observer, "observer is null");
           try {
               observer = RxJavaPlugins.onSubscribe(this, observer);

               ObjectHelper.requireNonNull(observer, "Plugin returned null Observer");

               subscribeActual(observer);
           } catch (NullPointerException e) { // NOPMD
               throw e;
           } catch (Throwable e) {
               Exceptions.throwIfFatal(e);
               // can't call onError because no way to know if a Disposable has been set or not
               // can't call onSubscribe because the call might have set a Subscription already
               RxJavaPlugins.onError(e);

               NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
               npe.initCause(e);
               throw npe;
           }
 }
  ```

  - 인자가 없는 subscribe()함수는 onNext와 onComplete이벤트를 무시하고 onError이벤트가 발생했을 때만 OnerrorNotImplementedException 을 던짐
  - 인자가 1개 있는 오버로딩은 OnNext 이벤트를 처리한다.
  - 인자가 2개인 함수는 onNext와 onError이벤트를 처리
  - 인자가 3개인 함수는 onNext, onError, onComplete 이벤트를 모두 처리

앞 함수 원형은 모두 Disposable 인터페이스의 객체를 리턴합니다.

```java
Observable<String> source = Observable.just("RED", "GREEN", "YELLOW");
Disposable d= source.subscribe(
        v -> System.out.println("onNext(): value : " + v),
        err -> System.out.println("onError() : err :" + err.getMessage()),
        () -> System.out.println("onComplete()")
);

System.out.println("isDisposed() : " + d.isDisposed());
```

```
결과
onNext(): value : RED
onNext(): value : GREEN
onNext(): value : YELLOW
onComplete()
isDisposed() : true
```
##### 3. create()
 해당 함수는 onNext(), onComplete, onError 같은 알림을 개발자가 직접 호출해야 한다.

 ```java
 Observable<Integer> source = Observable.create(
                (ObservableEmitter<Integer> emitter) -> {
                    emitter.onNext(100);
                    emitter.onNext(200);
                    emitter.onNext(300);
                    emitter.onComplete();
                }
        );
        source.subscribe(System.out::println);
 ```

 사용자가 직접 호출이란 뜻이 이런거.  
 `source.subscribe(System.out::println);`
 얘는 `source.subscribe(data -> System.out.println("Result : " + data));` 를 줄인것

##### 4. fromArray()
 배열에서 int[]배열은 Integer[]로 변환해야 합니다. 다양한 방법 중 자바 8의 IntStream.of(intArray).boxed().toArray(Integer[]::new);

##### 5. fromIterable()

Iterable<E> 인터페이스를 구현하는 대표적인 클래스는 ArrayList(List 인터페이스), ArrayBlockingQueue(BlockingQueue 인터페이스), HashSet(Set 인터페이스), LinkedList, Stack, TreeSet, Vector 등이 있습니다.

```java
List<String> names = new ArrayList<>();
        names.add("Jerry");
        names.add("Jerry2");
        names.add("Jerry3");

        Observable<String> source2 = Observable.fromIterable(names);
        source2.subscribe(System.out::println);
```

##### 6. fromCallable()
 기존 자바에서 제공하는 비동기 클래스나 인터페이스의 연동을 할 때 사용

 자바 5에서 추가된 동시성 API Callable인터페이스.
 ```java
 Callable<String> callable = () -> {
            Thread.sleep(1000);
            return "Hello Callable";
        };

        Observable<String> source = Observable.fromCallable(callable);
        source.subscribe(System.out::println);
 ```
##### 7. fromFuture()

 위와 동일하게 비동기 계산의 결과를 구할 때 사용되지만, 보통 Executor 인터페이스를 구현한 클래스에 Callable 객체에서 구현한 계산 결과가 나올 때까지 블로킹 됩니다.

 ```java
 Future<String> future = Executors.newSingleThreadExecutor().submit(() -> {
            Thread.sleep(1000);
            return "Hello Future";
        });

        Observable<String> source = Observable.fromFuture(future);
        source.subscribe(System.out::println);
 ```

##### 8. fromPubilsher()

Observable에서 제공하는 fromXXX() 계열 함수의 마지막.
Pubilsher는 자바 9의 표준 Flow API의 일부.

```java
Publisher<String> publisher = (Subscriber<? super String> s) -> {
            s.onNext("Hello Obserable.fromPublisher()");
            s.onComplete();
        };

        Observable<String> source = Observable.fromPublisher(publisher);
        source.subscribe(System.out::println);
```

### Single 클래스

오직 1개의 데이터만 발생하도록 한정하는 것을 말합니다. Observable클래스는 데이터를 무한하게 발행 할 수 있지만 싱글은 오직 1개. 보통 결과가 유일한 서버 API를 호출할 떄 유용하게 사용할 수 있다.

```java
        //1. 기존 Obserable에서 Single 객체로 변환하기
        Observable<String> source = Observable.just("Hello Single");
        Single.fromObservable(source)
                .subscribe(System.out::println);

        //2. single()함수를 호출해 Single 객체 생성하기
        Observable.just("Hello Single")
                .single("default item")
                .subscribe(System.out::println);

        //3. first()함수를 호출해 Single 객체 생성하기.
        String[] colors = {"RED", "Blue", "Gold"};
        Observable.fromArray(colors)
                .first("default value")
                .subscribe(System.out::println);

        //4. empty Observable에서 Single 객체 생성하기.
        Observable.empty()
                .single("default value")
                .subscribe(System.out::println);

        //5. take()함수에서 Single 객체 생성
        Observable.just("하하", "하하2")
                .take(1)
                .single("default order")
                .subscribe(System.out::println);
```

### Maybe 클래스

maybe 클래스는 처음 도입된 Observable의 또 다른 특수 형태.

Single 클래스는 1개 완료, Maybe 클래스는 0혹은 1개완료 할 수도 있습니다.



# #

###### 뜨거운 Obserable

Observable에는 뜨거운 것과 차가운 것이 있습니다.

**차가운 Observable**은 마치 냉장고에 들어있는 냉동식품과 같다. Observable을 선언하고 just(), fromIterable() 함수를 호출해도 옵서버가 subscribe()함수를 호출하여 구독하지 않으면 데이터를 발행하지 않습니다. **다른 말로 게으른 접근법.**

**뜨거운 Observable**은 구독자가 존재 여부와 관계없이 데이터를 발행하는 Observable입니다. 따라서 여러 구독자를 고려 할 수 있다. 단, 구독자로서는 Observable에서 발행하는 데이터를 처음부터 모두 수신할 것으로 보장할 수 없다. 즉, 차가운 Observable은 구독하면 준비된 데이터를 처음부터 발행합니다. 하지만 뜨거운 Observable은 구독한 시점부터 Observable에서 발행한 값을 받습니다.



차가운 Observable의 예는 웹 요청, 데이터베이스 쿼리와 파일 읽기 등입니다. 보통 내가 원하는 URL이나 데이터를 지정하면 그때부터 서버나 데이터베이스 서버에 요청을 보내고 결과를 받아옵니다. 지금까지 우리가 다룬 Observable은 모두 차가운 Observable입니다. 앞으로도 별도의 언급이 없으면 차가운 Observable이라고 생각하면 됩니다.


뜨거운 Observable의 예는 마우스 이벤트, 키보드 이벤트, 시스템 이벤트, 센서 데이터와 주식 가격등이 있습니다.
뜨거운 Observable에는 주의 할 점이 있습니다. 바로 배압(back pressure)을 고려.

배압은 Observable에서 데이터를 발행하는 속도와 구독자가 처리하는 속도의 차이가 클 때 발생합니다. Flowable이라는 특화 클래스에서 배압을 처리합니다.

차가운 Observable을 뜨거운 Observable 객체로 변환하는 방법은 Subject 객체를만들거나 ConnectableObservable 클래스를 활용하는 것!

#### 2.5 Subject 클래스
 Subject클래스는 차가운 Observable을 뜨거운 Observable로 바꿔준다고 소개했습니다. Subject 클래스의 특성은 **Observable의 속성과 구독자의 속성이 모두 있다는 점입니다.** Observable 처럼 데이터를 발행할 수도 있고 구독자처럼 발행된 데이터를 바로 처리할 수도 있습니다.

 - **AsyncSubject**
 Observable에서 발행한 마지막 데이터를 얻어올 수 있는 Subject클래스. 완료되기 전 마지막 데이터에만 관심이 있으며 이전 데이터는 무시.

![ ](https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/S.AsyncSubject.png)

1. 처음 구독자가 subscribe() 함수 호출
2. 이후에 1, 2 발행된 후 두번째 구독자가 subscribe()함수를 호출
3. 마지막으로 3이 발행되고 데이터 발행을 완료.

```java
AsyncSubject<String> subject = AsyncSubject.create();
        subject.subscribe(data -> System.out.println("Subscriber #1 => " + data));
        subject.onNext("1");
        subject.onNext("3");
        subject.subscribe(data -> System.out.println("Subscriber #2 => " + data));
        subject.onNext("5");
        subject.onComplete();
```
Subscriber #1 => 5  
Subscriber #2 => 5  

 - **BehaviorSubject**
(구독자가) 구독을 하면 가장 최근 값 혹은 기본값을 넘겨주는 클래스입니다. 예를 들어 온도 센서에서 값을 받아온다면 가장 최근의 온도 값을 받아오는 동작을 구현 할 수 있습니다. 또한 온도를 처음 얻을 때는 초깃값(예를 들면 0)을 반환하기도 합니다.  

![](https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/S.BehaviorSubject.png)

```java
BehaviorSubject<String> subject = BehaviorSubject.createDefault("6");
subject.subscribe(data -> System.out.println("Subscriber #1 => " + data));
subject.onNext("1");
subject.onNext("3");
subject.subscribe(data -> System.out.println("Subscriber #2 => " + data));
subject.onNext("5");
subject.onComplete();
```

Subscriber #1 => 6  
Subscriber #1 => 1  
Subscriber #1 => 3  
Subscriber #2 => 3  
Subscriber #1 => 5  
Subscriber #2 => 5  

 - **PublishSubject**
 가장 평범한 Subject 클래스입니다. 구독자가 subscribe()함수를 호출하면 값을 발행하기 시작합니다.AsyncSubject클래스 처럼 마지막 값만 발행하거나 BehaviorSubject클래스처럼 발행한 값이 없을 때 기본값을 대신 발행하지도 않습니다. 오직 해당 시간에 발생한 데이터를 그대로 구독자에게 전달받습니다.

![](https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/S.PublishSubject.png)

```java
PublishSubject<String> subject = PublishSubject.create();
        subject.subscribe(data -> System.out.println("Subscriber #1 => " + data));
        subject.onNext("1");
        subject.onNext("3");
        subject.subscribe(data -> System.out.println("Subscriber #2 => " + data));
        subject.onNext("5");
        subject.onComplete();
```

Subscriber #1 => 1  
Subscriber #1 => 3  
Subscriber #1 => 5  
Subscriber #2 => 5  

 - **ReplaySubject**
 가장 특이하고 사용할 때 주의해야 하는 클래스. Subject클래스의 목적은 뜨거운 Observable을 활용하는 것인데 차가운 Observable처럼 동작하기 때문이다. ReplaySubject클래스는 구독자가 새로 생기면 항상 데이터의 처음부터 끝까지 발행하는 것을 보장해줍니다. 마지 테이프로 전체 내용을 녹음 해두었다가 새로운 사람이 들어오면 정해진 음악을 들려주는 것과 가습니다. 그러므로 모든 데이터 내용을 저장해두는 과정 중 메모리 누수가 발생할 가능성을 염두에 두고 사용할 때 주의해야 합니다.

![ ](https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/S.ReplaySubject.png)

```Java
ReplaySubject<String> subject = ReplaySubject.create();
        subject.subscribe(data -> System.out.println("Subscriber #1 => " + data));
        subject.onNext("1");
        subject.onNext("3");
        subject.subscribe(data -> System.out.println("Subscriber #2 => " + data));
        subject.onNext("5");
        subject.onComplete();
```

Subscriber #1 => 1  
Subscriber #1 => 3  
Subscriber #2 => 1  
Subscriber #2 => 3  
Subscriber #1 => 5  
Subscriber #2 => 5  

#### ConnectableObservable

 Subject클래스처럼 차가운 Observable을 뜨거운 Observable로 변환합니다. Observable을 여러 구독자에게 공유할 수 있으므로 원 데이터 하나를 여러 구독자에게 동시에 전달할 때 사용합니다. 특이한 점은 subscribe()함수를 호출해도 아무 동작이 일어나지 않는다는 점. 새로 추가된 connect() 함수는 호출한 시점부터 subscribe()함수를 호출한 구독자에게 데이터를 발행하기 때문입니다.

 ConnectableObservable 객체를 생성하려면 먼저 Observable에 publish()함수를 호출해야합니다. 이 함수는 여러 구독자에게 데이터를 발행하기 위해 connect()함수를 호출하기 전까지 데이터 발행을 유예하는 역할.

![ ](http://reactivex.io/documentation/operators/images/publishConnect.png)

```Java
String[] dt = {"1", "3", "5"};
        Observable<String> balls = Observable.interval(100L, TimeUnit.MILLISECONDS)
                .map(Long::intValue)
                .map(i -> dt[i])
                .take(dt.length);

        ConnectableObservable<String> source = balls.publish();
        source.subscribe(tester);
        source.subscribe(data -> System.out.println("Subscriber #1 => " + data));
        source.subscribe(data -> System.out.println("Subscriber #2 => " + data));
        source.connect();
        Thread.sleep(250);

        source.subscribe(data -> System.out.println("Subscriber #3 => " + data));
        Thread.sleep(100);
```
