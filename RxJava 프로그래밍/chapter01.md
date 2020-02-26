 **함수평 프로그래밍과 객체지향 프로그래밍의 차이?**
 예를 들어
 `연말 매출액 = 1월매출 + 2월매출 + 3월매출 +.... 12월 매출`  
 존재 할때, 명령형 프로그래밍 방식은 변경이 발생했다는 통지를 받아서 연말 매출액에 새로 계산하는 당겨오는 (pull)방식이지만,  
 리액티프 프로그래밍은 데이터 소스가 변경된 데이터를 밀어주는 (Push)방식입니다. 일종의 옵서버 패턴.

 ```
 dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.12'
    compile group: 'io.reactivex.rxjava2', name: 'rxjava', version: '2.1.1'
}
 ```

위와 같은 서드파트 라이브러리.

```java

import io.reactivex.Observable;

public class FirstExample {

    public void emit() {
        Observable.just("Hello", "RxJava2")
                .subscribe(System.out::println);
    }

    public static void main(String[] args) {
        FirstExample demo = new FirstExample();
        demo.emit();
    }
}

```


#### Observable 클래스

Observable 클래스는 데이터의 변화가 발생하는 데이터 소스. 앞서 나왔던 연간 매출액 예에서는 개별적인 매출액 데이터에 해당.  

#### just() 함수
Observable 클래스의 just()함수는 가장 간단한 Observable 선언 방식.

#### subscribe() 함수
subscribe()함수는 Observable을 구독합니다. Observable은 subscribe 함수를 호출해야 비로소 변화환 데이터를 구독자에게 발행합니다. (just()함수만 호출하면 데이터를 발행하지 않습니다.) 이 부분은 옵서버 패턴과 동일하다고 생각하면 됩니다. 반드시 데이터를 수신할 구독자가 subscribe()함수를 호출해야 Observable에서 데이터가 발행.

#### System.out::println

자바8의 메서드 레퍼런스 활용.

#### emit()
우리가 만든 메소드
