다룰 내용

```markdown
2.7 문서를 작성하는 대신 테스트를 만드세요    
2.8 모의 객체(Mock) 대신 페이크 객체(Fake)를 사용하세요  
2.9 인터페이스를 짧게 유지하고 스마트(smart)를 사용하세요
```



`2.7 문서를 작성하는 대신 테스트를 만드세요` 에서 저자가 말하는 것은 코드를 문서화한다는 것은 코드를 깔끔하게 하는 것에 더 많은 우선순위를 둬야한다고 말하고 있습니다. 

 예를 들어 코드에서 메서드, 클래스 명, 클래스 설계 등이 엉망일 경우 분명 문서화가 필요할 것이기 때문인데- 그렇게 때문에 더욱 코드를 깔끔하게 해야 한다고 말하고 있습니다. 아마도 이 부분이 저자는 유지보수성에 대해서 생각했기 때문이라고 생각합니다.

 그럼 `깔끔하게 만든다`  라는 말의 의미는 단위테스트를 포함한다는 의미가 됩니다.

 그러면서 단위테스트에 대한 생각을 저자가 간략히 말하는데, 단위 테스트도 클래스의 일부로 취급해야 되고, 이는 곧 더 깔끔하게 만들수 있는 여지를 만들어 준다고 이야기합니다. 즉, 단위테스트를 잘 관리하는 것이 문서를 만드는 것보다 중요하다고 말하고 있습니다.

이 부분을 읽으면서 들었던 생각은 테스트 주도 설계의 장점과 같은 맥락을 이어간다고 생각합니다. 그러나, 우리가 논의해봐야 하는 부분은 현실에서는 여전히 우리는 타협하고 있다고 생각합니다. 유지보수하고 있는 서비스의 코드에 문서화를 한다고 했을 때, 가장 흔하게 저지르는 죄악은 `코드위에 주석 달기`입니다. 클린 코드 책이나, 여러 서적에서 코드로서 잘표현되면 주석은 필요없다고 합니다. 그러나, 예를 들어 내일 아침 제가 다른 사람의 코드를 리뷰할때 주석을 발견했습니다. 이 부분을 보고 제가 감히 지우라고 말할 수 있을까요? 코드를 리뷰할 때는 정중하고, 그리고 모범답안을 함께 제시해야 상대방이 기분 나쁘지 않을 텐데, 이 부분을 코드리뷰하면서 해결할 수 있는 문제일까?

 강합적인 규칙으로서 주석은 "어떻게(How)"가 아니라 "왜(Why)" 일 때만 존재해야 한다는 룰이 있어도. 사실 지키기 어려운데, 과연 저자가 말한 대로 단위테스트를 만들어 낼수 있을 까 싶습니다.



`2.8 모의 객체(Mock) 대신 페이크 객체(Fake)를 사용하세요` 이 부분이 아직도 조금 이해가 되지 않는 부분입니다. 이 부분은 책을 좀 더 읽고 다시 작성하기.





`2.9 인터페이스를 짧게 유지하고 스마트(smart)를 사용하세요` 이 부분이 조금 재미있었는데, 이 부분은 코드로서 단계단계 설명해보겠습니다.

```java
interface ExChange {
  float rate(String target);
  float rate(String source, String target);
}
```

위 ExChange 인터페이스에서 2개의 메서드가 존재하는데, 이는 ExChange 가 너무 많은 요구를 하고 있다고 합니다. 그러므로, ExChange는 좋지 않은 인터페이스라고 합니다.

 이는 단일 책임 원칙을 위반하는 클래스를 만들도록 부추기기 때문에 조심해야 합니다. ExChange는 오버로딩으로 2가지 메소드를 가지고 있지만, 두 메소드가 하는 역할은 철저히 독립될 수 있을 만큼의 역할을 지니고 있기 때문에 분리해야 합니다.



그럼 어떻게 분리를 해야될까요?

책에서는 Smart라는 것을 사용하라고 합니다. Smart 라는 객체에 대해서도 아래와같이 코드로서 표현합니다.

```java
interface Exchange{
  float rate(String source, String target);
  final class Smart {
    private final Exchange origin;
    public float toUsd(String source) {
      return this.source.rate(source, "USD");
    }
  }
}
```

Smart 클래스는 많은 메서드를 포함할 수 있으며, Exchange 인터페이스와 Smart 클래스는 서로 분리되는 역할을 얻을 수 있습니다.

```java
float rate = new Exchange.Smart(new NYSE().toUsd("EUR")
```

와 같이 사용할 수 있습니다.

또한, 더 많은 메서드가 필요하다면

```java
interface Exchange{
  float rate(String source, String target);
  final class Smart {
    private final Exchange origin;
    public float toUsd(String source) {
      return this.source.rate(source, "USD");
    }
    
    public float eurToUsd(){
      return this.toUsd("EUR");
    }
  }
}
```



결과적으로 "스마트" 클래스의 크기는 점점 더 커지겠지만, Exchange인터페이스는 작고, 높은 응집도를 유지할 수 있습니다.

```java
float rate = new Exchange.Smart(new NYSE()).eurToUsd()
```

또는 데코레이터로도 사용될 수 있다.

```java
interface Exchange{
  float rate(String source, String target);
  final class Fast implements Exchange {
    
    private final Exchange origin;
    
    public float rate(String source, String target) {
      ...
    }
    
    public float eurToUsd(){
      return this.origin.toUsd("EUR");
    }
  }
}
```

그렇게 되면 '스마트' 클래스가 객체에 새로운 메서드를 추가하는데 비해 데코레이터는 이미 존재하는 메서드를 좀 더 강력하게 만든다는 점이 있습니다.