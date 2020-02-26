# RxJava 프로그래밍: 리액티브 프로그래밍 기초부터 안드로이드까지 한 번에

---

```
Chapter 1 리액티브 프로그래밍 소개  
__1.1 리액티브 프로그래밍  
____1.1.1 자바 언어와 리액티브 프로그래밍  
____1.1.2 리액티브 프로그래밍 개념 잡기  
__1.2 RxJava를 만들게 된 이유  
__1.3 RxJava 처음 시작하기  
____1.3.1 io.reactivex  
____1.3.2 Observable 클래스  
____1.3.3 just() 함수  
____1.3.4 subscribe() 함수
____1.3.5 System.out::println
____1.3.6 emit() 메서드
__1.4 RxJava를 어떻게 공부할 것인가
__1.5 마블 다이어그램 보는 법
__1.6 마치며

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

Chapter 3 리액티브 연산자 입문
__3.1 map() 함수
__3.2 flatMap() 함수
____3.2.1 구구단 만들기
__3.3 filter() 함수
__3.4 reduce() 함수
____3.4.1 데이터 쿼리하기
__3.5 마치며

Chapter 4 리액티브 연산자의 활용
__4.1 생성 연산자
____4.1.1 interval() 함수
____4.1.2 timer() 함수
____4.1.3 range() 함수
____4.1.4 intervalRange() 함수
____4.1.5 defer() 함수
____4.1.6 repeat() 함수
__4.2 변환 연산자
____4.2.1 concatMap() 함수
____4.2.2 switchMap() 함수
____4.2.3 groupBy() 함수
____4.2.4 scan() 함수
__4.3 결합 연산자
____4.3.1 zip() 함수
____4.3.2 combineLatest() 함수
____4.3.3 merge() 함수
____4.3.4 concat() 함수
__4.4 조건 연산자
____4.4.1 amb() 함수
____4.4.2 takeUntil() 함수
____4.4.3 skipUntil() 함수
____4.4.4 all() 함수
__4.5 수학 및 기타 연산자
____4.5.1 수학 함수
____4.5.2 delay() 함수
____4.5.3 timeInterval() 함수
__4.6 마치며

Chapter 5 스케줄러
__5.1 스케줄러 개념 배우기
__5.2 스케줄러의 종류
____5.2.1 뉴 스레드 스케줄러
____5.2.2 계산 스케줄러
____5.2.3 IO 스케줄러
____5.2.4 트램펄린 스케줄러
____5.2.5 싱글 스레드 스케줄러
____5.2.6 Executor 변환 스케줄러
__5.3 스케줄러를 활용하여 콜백 지옥 벗어나기
__5.4 observeOn() 함수의 활용
__5.5 마치며

Chapter 6 안드로이드의 RxJava 활용
__6.1 RxAndroid 소개
____6.1.1 리액티브 라이브러리와 API
____6.1.2 안드로이드 스튜디오 환경 설정
__6.2 RxAndroid 기본
____6.2.1 Hello world 예제
____6.2.2 제어 흐름
____6.2.3 RxLifecyle 라이브러리
____6.2.4 UI 이벤트 처리
__6.3 RxAndroid 활용
____6.3.1 리액티브 RecyclerView
____6.3.2 안드로이드 스레드를 대체하는 RxAndroid
____6.3.3 REST API를 활용한 네트워크 프로그래밍
__6.4 메모리 누수
____6.4.1 해결책 1: Disposable 인터페이스를 이용하여 명시적으로 자원 해제
____6.4.2 해결책 2: RxLifecycle 라이브러리 이용
____6.4.3 해결책 3: CompositeDisposable 클래스 이용
__6.5 마치며

Chapter 7 디버깅과 예외 처리
__7.1 디버깅
____7.1.1 doOnNext(), doOnComplete(), doOnError() 함수
____7.1.2 doOnEach() 함수
____7.1.3 doOnSubscribe(), doOnDispose(), 기타 함수
__7.2 예외 처리
____7.2.1 onErrorReturn() 함수
____7.2.2 onErrorResumeNext() 함수
____7.2.3 retry() 함수
____7.2.4 retryUntil() 함수
____7.2.5 retryWhen() 함수
__7.3 흐름 제어
____7.3.1 sample() 함수
____7.3.2 buffer() 함수
____7.3.3 throttleFirst()와 throttleLast() 함수
____7.3.4 window() 함수
____7.3.5 debounce() 함수
__7.4 마치며

Chapter 8 테스팅과 Flowable
__8.1 JUnit 5 활용
__8.2 TestObserver 클래스
__8.3 비동기 코드 테스트
__8.4 Flowable 클래스
____8.4.1 Observable과 Flowable의 선택 기준
____8.4.2 Flowable을 활용한 배압 이슈 대응
__8.5 마치며
```
