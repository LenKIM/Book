# null 대신 Optioanl

- null 래퍼런스의 문제점과 null을 멀리해야 하는 이유
- null 대신 Optioanl: null로부터 안전한 도메인 모델 재구현하기.
- Optioanl 활용: null 확인 코드 제거하기.
- Optional에 저장된 값을 확인하는 방법
- 값이 없을 수 있는 상황을 고려하는 프로그래밍



*null은  `구현하기 쉬웠기 때문에` 존재했던 것이지, 의도했던 것은 아니다.*



## 00. 값이 없는 상황을 어떻게 처리할까?

```java
public String getCarInsuranceName(Person person){
  if(persion != null) {
    Car car = person.getCar();
    if(car != null) {
      Insurance insurance = car.getInsurance();
      if(insurance != null){
        return insurance.getName()
      }
    }
  }
  return "Unknown"
}
```

이렇게 코딩하고 있는가?

아니면...

```java
public String getCarInsuranceName(Person person){
  if(person == null){
    return "Unknown";
  }
  
  Car car = person.getCar();
  if(car == null){
    return "Unknown";
  }
  Insurance insurance = car.getInsurance();
  if(insurance == null){
    return "Unkwon";
  }
  return insurance.getName();
}
```



## Null 때문에 발생하는 문제

- 에러의 근원이다.
- 코드를 어지럽힌다.
- 아무 의미가 없다,
- 자바 철학에 위배된다.
- 형식 시스템에 구멍



## Optional 클래스

`java.util.Optional<T>` 

: 선택형 값을 캡슐화하는 클래스.

**Optioanl.empty 는 Optioanl 의 특별한 싱글턴 인스턴스를 반환하는 정적 팩토리 메소드.**



**null 레퍼런스와 Optional.empty() 는 서로 무엇이 다를까?**

null을 참조하려 하면 NullPointerException이 발생하지만 Optioanl.empty()는 Optioanl 객체이므로 이를 다양한 방식으로 활용할 수 있다.

Optioanl을 이용하면 값이 없는 상황이 우리 데이터에 문제가 있는 것인지 아니면 알고리즘의 버그인지 명확하게 구분할 수 있다. 모든 null 레퍼런스를 Optioanl로 대치하는 것은 바람직하지 않다. `Optioanl` 의 역할은 더 이해하기 쉬운 API를 설계하도록 돕는다.

*즉, 메서드의 시그너처만 보고도 선택형 값인지 여부를 구별할 수 있다.*



## Map으로 Optional의 값을 추출하고 변환

보통 객체의 정보를 추출할 때는 Optioanl을 사용할 때가 값다.

```java
String name = null;
if(insurance != null){
  name = insurance.getName();
}
```

이런 유형의 패턴에 사용할 수 있도록 Optional은 map 메서드 지원

```java
Optional<Insurance> optInsurance = Optioanl.ofNullable(insurance);
Optional<String> name = optInsurance.map(Insurance::getName);
```



## flatMap 으로 Optioanl 객체 연결

```java
Optional<Person> optPerson = Optioanl.of(person);
Optional<String> name = optPerson.map(Person::getCar)
  															 .map(Car::getInsurance)
  															 .map(Insurance::getName);
```

*와 같이 할 수 있지 않을까 생각할 수 있지만, 해당 코드는 컴파일되지 않는다.*

왜 그럴까? 위 map을 여러번 사용하는 것은 마치 `Optioanl<Optional<Car>>` 형식의 객체이다.

getInsurance는 또 다른 Optioanl 객체를 반환하므로 getInsurance 메서드를 지원하지 않는다.



이런 문제는 어떻게 해결 할수 있을까?

**Flatmap** 메서드로

전달된 함수는 각각의 정사각형을 두 개의 삼각형을 포함하는 스트림으로 변환한다. 즉, map을 적용한 결과로 세 개의 스트림을 포함하는 하나의 스트림이 생성된다.



위 컴파일 되지 않는 코드는 어떻게 해결하면 될까?

```java
pulbic String getCarInsuranceName(Optional<Person> person){
  return person.flatMap(Person::getCar)
    					 .flatMap(Car::getInsurance)
    					 .map(Insurance::getName)
    					 .orElse("Unknown");
}
```

flatmap메서드로 전달된 함수는 각각의 정사각형을 두 개의 삼각형을 포함하는 스트림으로 변환한다. 즉, map을 적용한 결과로 세 개의 스트림(각각 두 개의 삼각형을 가지고 있는)을 포함하는 하나의 스트림이 생성된다. 하지만, flatmap 덕분에 이차원 스트림이 여섯 개의 삼각형을 포함하는 일차원스트림으로 바뀐다.



## 디폴트 액션과 Optional 언랩

- **get()** 은 값을 읽는 가장 간단한 메서드면서 동시에 가장 안전하지 않는 메서드다. 메서드 get은 래핑된 값이 있으면 해당 값을 반환하고 값이 없으면 NoSuchElementException을 발생.
- **orElse(T otehr)** 메서드를 이용하면 Optional이 값을 포함하지 않을 때 디폴트 값을 제공
- **orElseGet(Supplier<? extends T> other)** 는 orElse 메서드에 대응하는 게으른 버전의 메서드다. Optioanl에 값이 없을 때만 Supplier가 실행되기 때문이다. 디폴트 메서드를 만드는 데 시간이 걸리거나 Optional이 비어있을 때만 디폴트 값을 생성하고 싶다면 orElseGet 사용

- **orElseThrow(Supplier<? extends X> exceptionSupplier)** 는 Optioanl이 비어 있을 때 예외를 ㅂ라생시킴.
- **ifPresent(Consumer<? super T> consumer)** 을 이용하면 값이 존재할 때 인수로 넘겨준 동작을 실행할 수 있다.



# 활용 예시

## 잠재적으로 null이 될 수 있는 대상을 Optional로 감싸기.

```java
Object value = map.get("key");
Optioanl<Object> value = Optional.ofNullable(map.get("key"))
```



## 그 외에는 우리가 더 Optioanl을 잘 쓰기 위한 노력을 하면서 채워보자.















































