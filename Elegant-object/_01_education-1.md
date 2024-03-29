다룰 내용

```markdown
2.1 가능하면 적게 캡슐화하세요      
2.2 최소한 뭔가는 캡슐화하세요      
2.3 항상 인터페이스를 사용하세요      
2.4 메서드 이름을 신중하게 선택하세요    
2.5 퍼블릭 상수(public constant)를 사용하지 마세요      
2.6 불변 객체로 만드세요  
```

 `2.1 가능하면 적게 캡슐화하세요` 에서 저자는 클린코드에서 나왔던 내용과 비슷하게, 객체 안에 객체는 오직 4개 이하로 존재 해야 한다고 강하게 말하고 있습니다. 만약 4개 이상이 존재해야 한다 라면, 그 객체를 리팩토링 해야 한다고 말하고 있습니다.

 어느 정도 공감되어지는 이유는 사내 서비스를 유지보수 하다보면, 객체 하나가 10개이상의 시그니처를 받아야 하는 클래스가 존재한다. 이럴 때 나는 극도의 거부감이 발생한다. 그 이유를 생각해보면, 첫번째로 나는 그 시그니처의 순서를 모두 외우지 못한다. 또한 객체가 가진 시그니처가 존재하는 이유에 대해서 생각해볼 겨를이 없었던 것 같다. 당장 그 시그니처에 맞게 채워야겠다. 라는 생각이 더 강하게 들었다.



```java
/**
     * 검색된 페이지를 csv로 응답한다.
     *
     * @param appletId
     * @param page
     * @param size
     * @param sort
     * @param direction
     * @param wq
     * @return
     */
    CsvResponse getDocsByCsv(long appletId, int page, int size, String sort, String direction, String wq);

```



코드 하나를 가져와봤다. `appletId` 와, `page`,`size`, `sort` , `direction` 와 무슨 관계가 있을까? 아무런 관계가 없다. 그렇기 때문에 위에서 말한 `2.1 가능하면 적게 캡슐화하세요.` / `2.2 최소한 뭔가를 캡슐화하세요` 라는 말에 대해서 더욱 공감되는 내용이다.

이 두가지에 대해서는 반박의 여부가 없을 정도로 합리적인 납득이 가능했다.



`2.3 항상 인터페이스를 사용하세요` 라는 말에 대해서는 `항상` 이라는 말에 대해서는 납득하기 어렵다. `간결한 결합`을 만든다는 것은 유지보수에 있어서 절대적이라는 사실에 대해서는 공감하지만, 실제로 서비스를 개발할 때, 우리는 `인터페이스` 를 먼저 만들고 개발하는가?

미래에 변경될 수 있는 요구사항을 예측할 수 있는 사람이라면 `2.3 항상 인터페이스를 사용하세요` 공감하겠지만, 나에게는 조금 버거운 내용이라 판단된다. 책에서는 인터페이스를 `계약(contract)` 라고 표현한다. 이 말에 대해서도 공감한다. 하지만, 계약이라는 것도 2개 이상의 객체가 존재할 때 `계약`이라는게 존재한다고 생각한다. 그렇기 때문에 `항상` 이라는 말은 지키기 어렵다. 그 외 생각해볼만 것으로 `계약의 의한 설계` 가 있다. 어떤 메소드에 대해서 사전, 사후 불변식을 지키며 설계하는 방법에 대해서 말한다. 관련된 내용으로 조영호님의 오브젝트 책 맨 뒤에 나오기도 하고, 내가 세미나 했던 내용도 있다. 참고해보면 좋을 것 같다. ([참고자료](https://www.slideshare.net/JoenggyuLenKim/design-by-contract-226703670))



`2.4 메서드 이름을 신중하게 선택하세요.`  이 부분에 대한 글을 읽으면서, 뭔가 논란이 많을 것이다 라는 생각을 가졌다. 이 절에서는 명명법에 대해서 설명하기 보다는 어떤 하나의 룰을 제시한다. 결과부터 설명하면, 빌더(Builder)의 이름은 **명사** 로, 조정자(manipulator)의 이름은 **동사**로 짓는데, 이 때 *빌더는 어떤 반환값을 가지며, 조정자는 반환값을 가지지 않는 것*을 원칙으로 합니다. 그 외 예외가 존재하는데, Bolean을 반환하는 값에 대해서 형용사로 작성해야 한다. 예를 들어 isEmpty() 라면 이는 empty()라 코드로 명시하고, 읽을 때는 `is empty()` 라고 말하는 것이다.

그럼 이제 빌더와 조정자가 무엇인지 얘기해보겠다.

책에서 말하길 빌더(builder)는 뭔가를 만들고 새로운 객체를 반환하는 메서드를 가리킨다. 그리고 빌더는 늘 명사로 존재해야 한다.

예를 들어

```java
int pow(int base, int power);
float speed();
Employee employee(int id);
String parsedCell(int x, int y);
```

에서 `parsedCell` 예시만 살펴보면 형용사+명사 = 명사. 형용사 parsed 가 들어가므로써 Cell 이라는 객체가 풍성해졌다. 그리고 이를 뜻하는 바가 무엇인지 명확해졌다. 그 메서드를 통해 무엇이 반환될 것인가에 대해서 명확해졌다.



이번에는 조정자에 대해서 생각해보자. 조정자(manipulator)는 객체로 추상화한 실세계 엔티티를 수정하는 메소드를 말한다. 이번에도 예를 들면

```java
void save(String contents);
void put(String key, Float value);
void remove(Employee emp);
void quicklyPrint(int id);
```



동사로서 동작됨을 이해할 수 있는데, 이 때 반환값은 존재하지 않는다. 

저자는 이러한 원칙을 지키기 위해 노력한다고 한다. 이는 CQRS와 연관되있어 보인다.

`2.4 메서드 이름을 신중하게 선택하세요.`  절에 대한 이야기를 조금 더 해보자. 저자가 말하고자 하는 원칙에 대해서 백번 양보한다고 가정해보자. 사실 JPA의 `Entity.save()` or` HashMap.put()` 그럼 이것들은 잘못된 걸까? 저자의 원칙에 따르면 어긋난 부분이다. 

조금 더 저자의 생각을 이해해보고 싶은데, 어느정도 납득가능하지만 이미 만들어진 자료구조, 프레임워크에서는 잘못된 걸까? 유지보수성을 어렵게 만드는 요소가 되는걸까?

 어떤 메서드인가에 따라 빌더냐?, 조정자이냐? 라는 관점을 바라보는건 좋은데, 이렇게 무 자르듯이 지켜야되는 부분인지는 잘 모르곘다.



`2.5 퍼블릭 상수(Public Contant)를 사용하지 마세요` 부분에서는 `public static String MAX_VALUE = Integer.MAX_VALUE`  이런식으로 public으로 두지 말라는 말인데, 그 이유는 public으로 노출된 객체를 사용한다는 건 응집도를 낮추고, 결합도를 높이는 행위로 이어진다. 

중복코드가 싫어서 `Public` 으로 상수로 두었다는 말은 지금 당장은 납득가능한 이야기이지만, 유지보수성으로는 좋은 코드는 아니라고 생각한다. 그러므로, 상수가 사용되는 객체 내에서 `private static String MAX_VALUE` 가 되야하는거 아닐까? 이렇게 되면 응집도는 높이고, 결합도는 낮쳐지기 때문이다.

 그렇다면 추후에 중복으로 사용되는 상수가 발생하면 어떻게 해야될까? 중복으로 사용해야되는 순간이 온다면, `Constants.XXX` 가 아니라 의미가 있는 클래스의 static 상수로 놓아야 합니다. 요는 객체는 자신이 사용하는 상수에 대해서 알고 있어야 하며, `Contants.XXX`와 같이 클래스를 만들어 활용하게 되면 점점 더 코드가 오염될 수 있다.



이번 글의 마지막으로 `2.6 불변 객체로 만드세요` 에 대해서는 막연하게 알고만 있었던 불변 객체의 장점을 적시에 관통하는 내용으로 가득차서 용어만 간단히 정리하고자 한다.

- 식별적 가변성이 없다.
- 실패 원자성을 보장해준다.
- 시간적 결합을 제거할 수 있다.
- 부수효과 제거
- NULL 참조 없애기
- 스레드 안전성
- 단순성



