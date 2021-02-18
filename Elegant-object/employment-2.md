다룰 내용

```markdown
3.5 절대 getter와 setter를 사용하지 마세요  
3.6 부 ctor 밖에서는 new를 사용하지 마세요.    
3.7 인트로스펙션과 캐스팅을 피하세요   
```



`3.5 절대 getter와 setter를 사용하지 마세요  ` 이 글에서 왜 저자는 getter와 setter를 사용하지 말라고 했을까? 읽기전에 생각해보았다. 

객체의 값을 외부에 노출하는 것은 OOP에 위배된다고 생각했다. OOP는 객체간에 메세지를 전달함으로써, 만들어지기 떄문이다. 책에서도 비슷한 이야기를 하긴 하는데, 조금더 구체적으로 설명하는 것처럼 보였다.

```java
class Cash {
  private int dollars;
  
  public int getDollars(){
    return this.dollars;
  }
  
  public void setDollars(int value) {
    this.dollars = value;
  }
}
```

위 코드에서 Cash는 자료구조일까? 객체일까?



**저자는 위 Cash 클래스를 단순한 자료구조라고 말한다.** 내가 앞서 말한 것처럼 dollars를 private로 선언하고, 외부에 노출되는건 함수들 뿐인데 왜 자료구조라고 할까?

이를 이해하기 위해 객체와 자료구조의 차이점을 이해해야 한다.

 둘의 차이점은 무엇이고, 자료구조가 OOP에서 해로운 이유가 뭘까? 



위 클래스에서 dollars를 직접 접근하는 경우에 해당 Cash라는 클래스는 어떤 '개성(personality)'도 지니지 않은 단순한 데이터가방(data bag)라고 표현하고 있습니다. 그러나 만약 코드에서 `print() ` 를 호출한다면?

```java
class Cash {
  private int dollars;
  
  public void print(){
    print(this.dollars)
  }
}
```

이는 객체의 역할을 하고 있다고 말할수 있기 때문에 객체라고 부를수 있다. 어떠한 프로터티가 노출되지 않고, 실제로 어떤 방식으로 동작되는지 알 수 없고, 캡슐화된 어떤 멤버가 이작업에 개입하는 지도 알 수 없을 것이다. 이게 바로 **캡슐화(encapsulation)**이고 OOP가 지향하는 것이다.

그러므로 자료구조와 객체의 차이점은 자료구조는 투명하지만, 객체는 불투명하다. 자료구조는 글래스 박스(glass box)라면, 객체는 블랙 박스(black box)이다. 자료구조는 수동적이지만, 객체는 능동적이다. 

 OOP는 데이터가 객체안에서 캡슐화되어 있고, 객체는 `살아 있다.` 객체들은 서로 연결되고, 어떤 일을 수행해야 할 때는 메세지를 전송해서 작업을 실행한다. 메세지를 통한 코드의 실행을 메서드 호출(method call)이라 부른다.OOP에서는 코드가 데이터를 지배하지 않는다. 대신 필요한 시점에 객체가 자신의 코드를 실행한다.



그러면서, 본격적으로 getter와 setter가 나쁜 이유, 그리고 처음에 보여줬던 클래스가 왜 자료구조인지 설명합니다.



저자는 getter, setter가 Java의 클래스를 자료구조로 변경하기 위해서 도입되었다고 합니다. 그 이유는 C++과 달리 언어차원에서 기능을 지원하지 않고, 수동적인 자료구조를 만들기 위해 필요했다고 합니다.  그럼 클래스에 멤버로 public 으로 하면 되지 않냐 할 수 있겠지만, 그럼 OOP를 제대로 이해하지 못했다고 말할 수 있기 때문에 get,set를 제공하는 것이라고 합니다.

**저자의 이야기 요는 getter와 setter를 사용하면 OOP의 캡슐화 원칙을 손쉽게 위반할 수 있다는 점.** 그리고 겉보기에는 메서드처럼 보이지만, 실제로는 데이터에 직접 접근하고 있다는 불쾌한 현실을 가리고 있을뿐이라는 사실입니다.

그럼 getter, setter 라는 접두사에 대해서 생각해봅시다.

```java
class Cash {
  private final int value;
  public int getDollars(){
    return this.value;
  }
}

---
  
class Cash {
  private final int value;
  public int dollars(){
    return this.value;
  }
}
```



저자는 접두사로 get,set를 사용하는 것이 안티패턴이라 말하고 있습니다. 왜일까? 접두사는 이 객체가 진짜 객체가 아니고, 어떤 존중도 받을 가치가 없는 자료구조라는 사실을 명확하게 전달합니다. 객체는 어떤 대화도 원하지 않고, 그저 우리가 어떤 데이터를 객체 안에 넣어주거나 다시 꺼내주기를 원할 뿐입니다.

책에서 말하길,

`getDollars()` 는 이런 의미입니다. *"데이터 중에 dollars를 찾은 후 반환하세요"* 라는 의미와 크게 다르지 않습니다. `dollars()` 는 "얼마나 많은 달러가 필요한가요?" 라고 묻는 것과 같습니다. `dollars()`는 객체를 데이터의 저장소로 취급하지 않고, 객체를 존중합니다. 사용자는 이 메서드를 통해 얼마나 많은 달러가 포함되어 있는지 알 수 있지만, 이 값이 private 프로퍼티로 저장되어 있다고 가정하지는 않습니다. 내부 구조에 관해 어떤 것도 가정하지 않으며, 결코 이 객체를 자료구조라고 생각하지 않습니다.



`3.6 부 ctor 밖에서는 new를 사용하지 마세요.` 를 사용하지 말라는 말의 의미는 **생성자를 통한 객체 초기화**가 소프트웨어라할 수 있는데, 이 생성자는 오직 부 ctor에서만 사용하라는 의미입니다.

 이는 마치 의존성 주입의 사용해야 하는 이유와 비슷합니다. 원칙에 따라 new 를 타 객체에서 사용하게 되면 강력한 결합을 갖게 되는 문제를 갖게 됩니다.

`3.7 인트로스펙션과 캐스팅을 피하세요` 

여기서 인트로스펙션과 캐스팅은 Java의 instanceOf, Class.cas()를 말합니다. 이를 사용하지 말라고 하는 이유는 **타입에 따라 객체를 차별하기 때문이라고 합니다.**  이는 마치 하나의 똑같은 객체를 좋은 객체와 나쁜 객체를 나누는 행위를 하기 때문입니다.

```java
public <T> int size(Iterable<T> items){
  if (items instanceOf Collection){
    return Collection.class.cast(items).size();
  }
  int size= 0;
  for (T item:items){
    ++size;
  }
  return size
}
```

이런 코드는 자바의 오버로딩을 통해서 분리해야 합니다.

```java
public <T> int size(Iterable<T> items){
  int size= 0;
  for (T item:items){
    ++size;
  }
  return size
}
public <T> int size(Collection<T> items){
  int size= 0;
  for (T item:items){
    ++size;
  }
  return size
}
```

이렇게 말이죠. 

저자는 다시말해, 클라이언트와 객체 사이의 불명확하고, 은폐되고, 암시적인 관계는 유지보수성에 심각한 영향을 끼친다는 사실을 말합니다.