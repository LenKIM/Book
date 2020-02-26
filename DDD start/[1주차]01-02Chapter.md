**CHAPTER 1 도메인 모델 시작**
도메인… ………………………………………………………………………… 1
도메인 모델… …………………………………………………………………… 3
도메인 모델 패턴………………………………………………………………… 6
도메인 모델 도출……………………………………………………………… 10
엔티티와 밸류………………………………………………………………… 17
엔티티……………………………………………………………………………… 18
엔티티의 식별자 생성…………………………………………………………… 20
밸류 타입… ……………………………………………………………………… 22
엔티티 식별자와 밸류 타입……………………………………………………… 29
도메인 모델에 set 메서드 넣지 않기…………………………………………… 30
도메인 용어…………………………………………………………………… 34

**CHAPTER 2 아키텍처 개요**
네 개의 영역…………………………………………………………………… 37
계층 구조 아키텍처…………………………………………………………… 40
DIP……………………………………………………………………………… 44
DIP 주의사항……………………………………………………………………… 50
DIP와 아키텍처…………………………………………………………………… 52
도메인 영역의 주요 구성요소… …………………………………………… 54
엔티티와 밸류… ………………………………………………………………… 55
애그리거트………………………………………………………………………… 58
리포지터리………………………………………………………………………… 61
요청 처리 흐름………………………………………………………………… 65
인프라스트럭처 개요… ……………………………………………………… 66
모듈 구성… …………………………………………………………………… 68



# 목표

- 도메인 모델 이해하기
  - 엔티티와 벨류 차이점 이해하기
  - 도메인 용어 이해하기
- 아키텍처 이해하기
  - 네 개의 영역
  - DIP 이란?
  - 계층 구조 아키텍처



# 01. Domain

- 온라인 서점 소프트웨어는 온라인으로 책을 판매하는 데 필요한 상품조회, 구매, 결제, 배송 추적 등의 기능을 제공해야 한다. 이때 '온라인 서점'은 소프트웨어로 해결하고자 하는 문제 영역, 즉 **도메인(domain)**에 해당된다.

![image-20190728140928012](/Users/lenkim/Library/Application Support/typora-user-images/image-20190728140928012.png)

- 특정 도메인을 위한 소프트웨어라고 해서 도메인이 제공해야 할 모든 기능을 구현하는 것은 아니다. 많은 온라인 쇼핑몰이 자체적으로 배송 시스템을 구축하기보다는 외부 배송 업체의 시스템을 사용하고 배송추적에 필요한 기능만 일부 연동한다.   
  **: 즉, 상황에 따라 도메인을 어떻게 구성할지 달라진다.**



## Domain Model

도메인 모델에는 다양한 정의가 존재하는데 기본적으로 **도메인 모델**은 특정 도메인을 개념적으로 표현한 것이다.

![Image result for ëë©ì¸ ëª¨ë¸](http://ww1.sinaimg.cn/large/006tNc79gy1g5fhpi8qt4g309e07nmx7.gif)

- 객체를 이용한 도메인 모델 - 도메인을 이해하려면 도메인이 제공하는 기능과 도메인의 주요 데이터 구성을 파악해야 하는데, 이런 면에서 기능과 데이터를 함께 보여주는 객체 모델은 도메인을 모델링하기에 적합하다.

- 관계가 중요한 도메인이라면 그래프를 이용해서 도메인을 모델링할 수 있다.



***즉, 도메인 모델은 기본적으로 도메인 자체를 이해하기 위한 개념 모델. 개념 모델을 이용해서 바로 코드를 작성할 수 있는 것은 아니기에 구현 기술에 맞게 구현 모델이 필요하다.***



*Notes*

>  하위 도메인과 모델
>
> 도메인은 다수의 하위 도메인으로 구성된다. 각 하위 도메인이 다루는 영역은 서로 다르기 때문에 같은 용어라도 하위 도메인마다 의미가 달라질 수 있다. 예를 들어, 카탈로그 도메인의 상품이 상품 가격, 상세 내용을 담고 있는 정보를 의미한다면 배송 도메인의 상품을 고객에게 실제 배송되는 물리적인 상품을 의미.
>
> 도메인에 따라 용어의 의미가 결정되므로, 여러 하위 도메인을 하나의 다이어그램에 모델링하면 안 된다. 카탈로그와 배송 도메인 모델을 구분하지 않고 하나의 다이어그램에 함께 표시.
>
> 모델의 각 구성요소는 특정 도메인을 한정할 때 비로서 의미가 완전해지기 때문에, 각 하위 도메인마다 별도로 모델을 만들어야 한다. 이는 카탈로그 하위 도메인 모델과 하위 도메인 모델을 따로 만들어야 한다나는 것을 뜻한다.



## 도메인 모델 패턴

![](http://ww4.sinaimg.cn/mw1024/006tNc79gy1g5fid2lab8j30i60yagmd.jpg)



| 계층                        | 설명                                                         |
| --------------------------- | ------------------------------------------------------------ |
| 사용자 인터페이스 또는 표현 | 사용자의 요청을 처리하고 사용자에게 정보를 보여준다. 여기서 사용자는 소프트웨어를 사용하는 사람뿐만 아니라 외부시스템도 사용자가 될 수 있다. |
| 응용(Application)           | 사용자가 요청한 기능을 실행한다. 업무 로직을 직접 구현하지 않으며 도메인 계층을 조합해서 기능을 실행한다. |
| 도메인                      | 시스템이 제공할 도메인의 규칙을 구현한다.                    |
| 인스라스트럭처              | 데이터베이스나 메시징 시스템과 같은 외부 시스템과의 연동을 처리한다. |

```java

public class Order {
    @EmbeddedId
    private OrderNo number;

   ...

    protected Order() {
    }

    public Order(OrderNo number, Orderer orderer, List<OrderLine> orderLines,
                 ShippingInfo shippingInfo, OrderState state) {
        setNumber(number);
        setOrderer(orderer);
        setOrderLines(orderLines);
        setShippingInfo(shippingInfo);
        this.state = state;
        this.orderDate = new Date();
        Events.raise(new OrderPlacedEvent(number.getNumber(), orderer, orderLines, orderDate));
    }

...

    private void setOrderLines(List<OrderLine> orderLines) {
        verifyAtLeastOneOrMoreOrderLines(orderLines);
        this.orderLines = orderLines;
        calculateTotalAmounts();
    }

    private void verifyAtLeastOneOrMoreOrderLines(List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            throw new IllegalArgumentException("no OrderLine");
        }
    }

    private void calculateTotalAmounts() {
        this.totalAmounts = new Money(orderLines.stream()
                .mapToInt(x -> x.getAmounts().getValue()).sum());
    }
```

**도메인 모델 패턴**이라는 것은 한 도메인 안에서 필요한 기능을 구현해야 한다.

- 핵심 규칙을 구현한 코드는 도메인 모델에만 위치하기 떄문에 규칙이 바뀌거나 규칙을 확장해야 할 때 다른 코드에 영향을 덜 주고 변경 내역을 모델에 반영할 수 있게 된다.



## 도메인 모델 도출

도메인 모델 도출을 위해서는 기본이 되는 작업은 모델을 구성하는 핵심 구성요소. 규칙, 기능을 찾는 것.

이 과정은 요구사항에서 출발한다.

 그리고 그 요구사항을 코드에 반영하면서 각 도메인에서 필요한 기능을 구현한다.



## Entity & Value

도출한 모델은 크게 엔티티와 벨류로 구분.

> Value 타입은 우리말로 하면 값 타입으로 표현할 수 있지만 "값"이란 단어를 여러 의미로 사용할 수 있기 때문에 이 책에서는 value를 지칭할 떄 "밸류"를 사용한다.

### 엔티티

- 식별자를 갖는다는 것.
- 식별자는 엔티티 객체마다 고유해서 각 엔티티는 서로 다른 식별자를 갖는다. 
- 예를 들면 `Order` 라는 도메인은 이라면, `orderNumber` 이라는 엔티티를 갖는다.
- 엔티티의 식별자는 바뀌지 않고 고유하기 때문에 두 엔티티 객체의 식별자가 같으면 두 엔티티는 같다고 판단 할 수 있다.

### 벨류

ShippingInfo 클래스의 receiverName필드와 receiverPhoneNumber 필드는 서로 다른 두 데이터를 담고 있지만 두 필드는 개념적으로 받는 사람을 의미한다.
 **즉, 두 필드는 실제로 한 개념을 표현하고 있다.**

```java
public class ShippingInfo {
	private String receiverName;
	private String receiverPhoneNumber;
	
	private String shippingAddress1;
	private String shippingAddress2;
	private String shippingZipcode;
}

/// 변경 후
public class ShippingInfo {
    @Embedded
    @AttributeOverrides({
      @AttributeOverride(name = "zipCode", column = @Column(name = "shipping_zip_code")),
      @AttributeOverride(name = "address1", column = @Column(name = "shipping_addr1")),
      @AttributeOverride(name = "address2", column = @Column(name = "shipping_addr2"))
    })
    private Address address;
    @Column(name = "shipping_message")
    private String message;
    @Embedded
    private Receiver receiver;
  ....
    
@Embeddable
public class Address {
    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;
  
...
```



## 엔티티 식별자와 밸류 타입

### 도메인 모델에 set 메서드 넣지 않기.

도메인 모델에서 get/set 메서드를 무조건 추가하는 것은 좋지 않은 버릇.

```java
public class Order {
    @EmbeddedId
    private OrderNo number;

    @Version
    private long version;

    @Embedded
    private Orderer orderer;

    @ElementCollection
    @CollectionTable(name = "order_line", joinColumns = @JoinColumn(name = "order_number"))
    @OrderColumn(name = "line_idx")
    private List<OrderLine> orderLines;

    @Column(name = "total_amounts")
    private Money totalAmounts;

    @Embedded
    private ShippingInfo shippingInfo;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    protected Order() {
    }

    public Order(OrderNo number, Orderer orderer, List<OrderLine> orderLines,
                 ShippingInfo shippingInfo, OrderState state) {
        setNumber(number);
        setOrderer(orderer);
        setOrderLines(orderLines);
        setShippingInfo(shippingInfo);
        this.state = state;
        this.orderDate = new Date();
        Events.raise(new OrderPlacedEvent(number.getNumber(), orderer, orderLines, orderDate));
    }

    private void setNumber(OrderNo number) {
        if (number == null) throw new IllegalArgumentException("no number");
        this.number = number;
    }

    private void setOrderer(Orderer orderer) {
        if (orderer == null) throw new IllegalArgumentException("no orderer");
        this.orderer = orderer;
    }

    private void setOrderLines(List<OrderLine> orderLines) {
        verifyAtLeastOneOrMoreOrderLines(orderLines);
        this.orderLines = orderLines;
        calculateTotalAmounts();
    }

    private void verifyAtLeastOneOrMoreOrderLines(List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            throw new IllegalArgumentException("no OrderLine");
        }
    }

    private void calculateTotalAmounts() {
        this.totalAmounts = new Money(orderLines.stream()
                .mapToInt(x -> x.getAmounts().getValue()).sum());
    }

    private void setShippingInfo(ShippingInfo shippingInfo) {
        if (shippingInfo == null) throw new IllegalArgumentException("no shipping info");
        this.shippingInfo = shippingInfo;
    }
```


위 코드의 set메서드는 앞서 set메서드와 중요한 차이점이 있는데 그것은 바로 접근 범위가 private이라는 점. 이 코드에서 set 메서드는 클래스 내부에서 데이터를 변경할 목적으로 사용된다. private이기 때문에 외부에서 데이터를 변경할 목적으로 set메서드를 사용할 수 없다.



### 도메인 용어

코드를 작성할 때 도메인에서 사용하는 용어는 매우 중요하다.

*어떤 클래스 이름, 함수명 이름*은 그 도메인과 관계되어 결정되는 것이 중요하다.



그러나,  한국 개발자의 경우 영어이기 때문에 불리한점은 있지만, 알맞은 영어 단어를 찾기 위한 노력을 게을리해서는 안된다.



도메인패턴의 단점은 무엇일까?

----

# 아키텍처

아키텍처를 설계할 때 출현하는 전형적인 영역은

'표현' - '응용' - '도메인' - '인프라스트럭처'

- 네 영역 중 표현 영역 또는 UI영역은 사용자의 요청을 받아 응용 영역에 전달하고 응용 영역의 처리 결과를 다시 사용자에게 보여주는 역할.

![layer](http://ww4.sinaimg.cn/large/006tNc79gy1g5fk2zj4l0j30j00890us.jpg)

아키텍처를 설계할 때 전형적인 영역이 '표현, '응용', '도메인', '인프라스트럭처' 네 개의 영역이다.

- **표현 영역:** 사용자의 요청을 받아 응용 영역에 전달하고 처리 결과를 다시 사용자에게 보여주는 역할
- **응용 영역:** 시스템이 사용자에게 제공해야 할 기능을 구현.
- **도메인 영역**: 도메인 모델을 구현.
- **인프라스트럭처 영역**: 구현 기술을 다룸. RDBMS 연동을 처리하고, 메시징 큐에 메시지를 전송하거나 수신하는 기능을 구현 하는 등. 논리적인 개념을 표현하기보다는 실제 구현을 다름.



## 계층 구조 아키텍처



표현 영역과 응용 영역은 도메인 영역을 사용하고, 도메인 영역은 인프라스트럭처 영억을 사용하므로 계층 구조를 적용하기에 적당해 보인다. 도메인의 복잡도에 따라 응용과 도메인을 분리하기도 하고 한 게층으로 합치기도 하지만 전체적인 아키텍처는 다음과 같읕 구조를 가진다.

![Image result for DDD ë¤ ê°ì ìì­](http://ww3.sinaimg.cn/large/006tNc79gy1g5fk86wjt8j305l06yjr6.jpg)



- 상위 계층에서 하위 계층으로의 의존만 존재하고 하위 계층은 상위 계층에 의존하지 않는다. 예를 들어, 표현 계층은 응용 계층에 의존하고 응용 계층이 도메인 계층에 의존하지만, 반대로 인프라스트럭처 계층이 도메인에 의존하거나 도메인이 응용 계층에 의존하지는 않는다.

![No Image](http://ww3.sinaimg.cn/large/006tNc79gy1g5fk9wgslwj30dx07zt9i.jpg)



- 응용 영역과 도메인 영역은 DB나 외부 시스템 연동을 위해 인프라스트럭처의 기능을 사용하므로 이런 계층 구조를 사용하는 것은 직관적으로 이해하기 쉽다. 하지만, 짚고 넘어가야 할 것이 있는데 바로 표현, 응용, 도메인 계층이 상세한 구현 기술을 다루는 인프라스트럭처 계층에 종속된다는 점이 있다.

  이런 의존을 예제와 함께 살펴보면,

  ```java
  public class CalculateDiscountService {
      
      private DroolsRuleEngine ruleEngine;
  
      public CalculateDiscountService(DroolsRuleEngine ruleEngine) {
          this.ruleEngine = ruleEngine;
      }
      
      public Money calculateDiscount(OrderLine orderLines, String customerId){
          Customer customer = findCustomer(customerId);
          
          MutableMoney money = new MutableMoney(0); //Drools에 특화된 코드 - 연산결과를 받기 위해 추가된 타입
          List<?> facts = Arrays.asList(customer, money); // Drools에 특화된 코드 - 룰에 필요한 데이터(지식)
          ((List) facts).addAll(orderLines);
          ruleEngine.evalute("discountCalculation", facts); // Drools에 특화된 코드 - Drools의 세션 이름
          return money.toImmutableMoney();
      }
  }
  ```

이런 코드는  2가지 어려움을 갖는데, `테스트 어려움` 과 `기능 확장의 어려움` 

그러므로 이런 문제를 해결하기 위해서 `DIP` 를 적용한다.



![image-20190728161058153](http://ww3.sinaimg.cn/large/006tNc79gy1g5fkm4y53lj30wi0aj0tw.jpg)

하나의 클래스가 저수준의 모듈들을 나눠서 사용해야 한다. 그런데 앞서 말한 두 가지 문제가 발생한다.



DIP는 이 문제를 해결하기 위해 저수준 모듈이 고수준 모듈에 의존하도록 바꾼다.

고수준 모듈을 구현하려면 저수준 모듈을 사용해야 하는데, 반대로 저수준 모듈이 고수준 모듈에 의존하도록 하려면 어떻게 해야 할까? **비밀은 추상화한 인터페이스에 있다.**



```java
public interface RuleDiscounter {
    public Money applyRules(Customer customer, List<OrderLine> orderLines);
}
public class CalculateDiscountService {

    private RuleDiscounter ruleDiscounter;

    public CalculateDiscountService(RuleDiscounter ruleDiscounter) {
        this.ruleDiscounter = ruleDiscounter;
    }

    public Money calculateDiscount(OrderLine orderLines, String customerId){
        Customer customer = findCustomer(customerId);

        return ruleDiscounter.applyRules(customer, orderLines);
    }
}
```

더이상 `CalculateDiscountService` 는 Drools에 의존하는 코드를 포함하고 있지 않다.다만 `RoleDiscounter` 가 룰을 적용한다는거만 알 뿐이다.

```java
public class DroolsRuleDiscounter implements RuleDiscounter {
    private KieContainer kieContainer;

    public DroolsRuleDiscounter(KieContainer kieContainer) {
        KieServices ks = KieServices.Factory.get();
        this.kieContainer = ks.getKieClasspathContainer();
    }

    @Override
    public Money applyRules(Customer customer, List<OrderLine> orderLines) {
        return ... Code emit;
    }
}
```

![dip](http://ww3.sinaimg.cn/large/006tNc79gy1g5fkxe1rxij30gv06pmz1.jpg)



이렇게 의존 관계를 역전하는 것을 DIP를 적용하는 것이다. 위 그림과 같이 저수준 모듈이 고수준 모듈에 의존하게 된다. 고수준 모듈이 저수준모듈을 사용하려면 고수준 모듈이 저수준 모듈에 의존해야 하는데, 반대로 저수준 모듈이 고수준 모듈에 의존한다고 해서 이를 **DIP(Dependency Inversion Principle, 의존 역전 원칙)** 이라고 부른다.



그렇다고해서, 

![No Image](http://ww1.sinaimg.cn/large/006tNc79gy1g5fl0l7ml8j30cm06e74w.jpg)

이런식으로 모듈을 나눈다면 잘못된 DIP 적용이 된것이다. 왜냐?

도메인이 영역은 구현기술을 다루는 인프라스트럭처 영역에 의존하고 있기 떄문에,

하늘색 테두리가 도메인영역에 들어가고, 파란색은 인프라영역에 들어가야지 맞는 DIP 적용예이다.



## DIP와 아키텍처

인프라스트럭처 영역은 구현 기술을 다루는 저수준 모듈이고 응용 영역과 도메인 영역은 고수준 모듈이다. 인프라스트럭처 계층의 가장 하단에 위치하는 계층형 구조와 달리 아키첵터에 DIP를 적용하면 다음과 같은 인프라 영역이 응용영역과 도메인 영역에 의존하는 구조가 된다.



![No Image](http://ww1.sinaimg.cn/large/006tNc79gy1g5fl687sxpj30e209f3zy.jpg)

인프라에 위치한 클래스나 도메인이나 응용 영역에 정의한 인터페이스를 상속받아 구현하는 구조가 되므로 도메인과 응용 영역에 대한 영향을 주지 않거나 최소화하면서 구현 기술을 변경하는 것이 가능.



위 그림에서 

만약, notifier를 변경해야 한다면, OrderService의 코드를 변경할 필요가 없다.

만약, Repo를 JPA로 변경하다면 OrderService의 코드 또한 변경할 필요가 없다.

 

![image-20190728163418742](http://ww3.sinaimg.cn/large/006tNc79gy1g5flaf4onej30e409ljt7.jpg)



이런식으로 변경하면 된다.



## 도메인 영역의 주요 구성요소로는...

| 요소            | 설명                                                         |
| --------------- | ------------------------------------------------------------ |
| 엔티티(ENTITIY) | 고유의 식별자를 갖는 객체로 자신의 라이프사이클을 갖는다.    |
| VALUE           | 고유의 식별자를 갖지 않는 객체로 주로 개념적으로 하나의 도메인 객체의 속성을 표현할 때 사용 |
| AGGREGATE       | 애그리거트는 관련된 엔티티와 밸류 객체를 개념적으로 하나로 묶음 |
| REPOSITORY      | 도메인 모델의 영속성을 처리.                                 |
| DOMAIN SERVICE  | 특정 엔티티에 속하지 않은 도메인 로직을 제공.                |

*저자왈*

*신입 시절에 처음 도메인 모델을 만들 때 DB테이블의 엔티티와 도메인 모델의 엔티티를 구분하지 못해 거의 동일하게 만들고 했다. 도메인 모델의 엔티티와 DB모델의 엔티티를(거의) 같은 것으로 생각해서 그랬는데, 경력을 더할수록 도메인 모델에 대한 이해가 쌓이면서 실제 도메인 모델의 엔티티와 DB관계형 모델의 엔티티는 같은 것이 아님을 알게 되었다.*

**1. 두 모델의 가장 큰 차이점은 도메인 모델의 엔티티는 데이터와 함께 도메인 기능을 함께 제공한다는 점.**

**2. 도메인 모델의 엔티티는 두 개 이상의 데이터가 개념적으로 하나인 경우 벨류 타입을 이용해서 표현할 수 있다는 것.**



### Entity와 Value

- 도메인 모델의 Entity는 단순히 데이터를 담고 있는 데이터 구조라기보다는 데이터와 함께 기능을 제공하는 객체이다.
- Value는 `immutable로 구현하는 것으로 권장`한다.

### Aggregate

- 도메인이 커질수록 Entity와 Value가 많아지고 복잡해진다.
- 이런 문제를 해결하기 위해서 애그리거트가 발생.
- 도메인 모델에서 전체 구조를 이해하는데 도움이 되는 것이 바로 애그리거트이다.
- 관련 객체를 하나로 묶은 객체이다.
  - 주문 : 주문자, 배송지 정보, 주문 목록, 총 결제 금액
- 내부 구현을 숨겨서 Aggregate 단위로 구현을 캡슐화할 수 있도록 돕는다.

### Repository

- Aggregate 단위로 도메인 객체를 저장하고 조회하는 기능을 정의한다.
  - 저장하는 메소드
  - 루트 식별자로 Aggregate를 조회하는 Method

![image-20190728165558402](http://ww4.sinaimg.cn/large/006tNc79gy1g5flwzegb0j30u0141e82.jpg)

### 요청 처리 흐름

- @Transaction
- 기능 구현에 필요한 도메인 객체를 Repository에서 가져와 실행하거나 신규 도메인 객체를 생성해서 Repository에 저장한다.

![image-20190728165733618](http://ww1.sinaimg.cn/large/006tNc79gy1g5flymkyaqj31370u01ky.jpg)

### Infrasturcture


표현 영역, 응용 영역, 도메인 영역을 지원한다.도메인 객체의 영속성 처리, 트랜잭션, SMTP클라, REST 클라 등 다른 영역에서 필요로 하는 프레임워크, 구현 기술, 보조 기능을 지원.

- 무조건 Infrasturcture의존을 없애는 것은 좋은 것이 아니다.
  - @Transaction가 하나의 예제가 될 수 있다
- DIP의 장점을 해치지 않는 범위에서 응용 영역과 도메인 영역에서 구현 기술에 대한 의존을 가져가는 것이 현명하다.
- 의존을 완전히 갖지 않도록 시도하는 것은 구현을 더 복잡하고 어렵게 만들 수 있다.
  

### 모듈 구성

- 한 패키지에 가능하면 10개 미만으로 타입 개수를 유지하려고 노력한다.
- 이 개수가 넘어가면 모듈을 분리하는 시도를 해본다.





*질문*

1. DDD를 잘 실천할줄 아는 사람은 어떤 사람일까?
2. 도메인 모델링을 해본 경험담?
3. 특정 도메인을 개념적으로 표현한 것이 도메인 모델.
   : 즉, 도메인 모델은 기본적으로 도메인 자체를 이해하기 위한 개념 모델. 개념 모델을 이용해서 바로 코드를 작성할 수 있는 것은 아니기에 구현 기술에 맞게 구현 모델이 필요하다.
4. 모델과 도메인의 차이는?
5. 표현 / 응용 / 도메인 / 인프라 을 어떤 방법론으로 접근해서 코딩하는가?
6. 도메인 모델 패턴 이란? 한 도메인 안에서 필요한 기능을 모두 구현해야 하는 것.
7. Entity / Value 를 설명할 수 있는가?
8. 좋은 이름을 짓기 위한 나만의 노력?
9. 도메인패턴의 단점은 무엇일까?
10. 도메인에 넣어야 하는 기능과 서비스에 넣어야 하는 기능을 어떻게 나누는가?
11. 도메인 모델 패턴말고 다른 패턴은 무엇?