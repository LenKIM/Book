---
title: Money 객체 만들기
created: '2021-02-01T01:59:09.636Z'
modified: '2021-02-01T03:54:10.615Z'
---

# Money 객체 만들기

$5 + 10CHF = $10 (환율이 2:1)
**$5 x 2 = $10**

---
~~- 달라 클래스가 없음~~
~~- 생성자가 없음~~
~~- times(int) 메서드 없음~~
~~- amount 필드 없음~~

---

amount를 private으로 만들기
Dollar 부작용?
Money 반올림?

---

타락한 객체

1. 테스트를 작성, 마음속에 있는 오퍼레이션이 코드에 어떤 식으로 나타나길 원하는지 생각하기. 이야기를 써내려가는 것이다. 원하는 인터페이스를 개발하라. 올바른 답을 얻기 위해 필요한 이야기의 모든 요소를 포함시켜라.

2. 실행 가능하게 만든다. 다른 무엇보다도 중요한 것은 빨리 초록 막대를 보는 것. 깔끔하고 단순한 해법이 명백히 보인다면 그것을 입력하라. 만약 깔끔하고 단순한 해법이 있지만 구현하는 데 몇분정도 걸릴 것 같으면 일단 적어 놓은 뒤에 원래 문제(초록 만대를 보는 것)로 돌아오자. 미적인 문제에 대한 이러한 전환은 몇몇 숙련된 소프트웨어 공학자들에게 어려운 일이다. 그들은 오로지 좋은 공학적 규칙들을 따르는 방법만 알 뿐, 초록 막대가 모든 죄를 사해준다. 하지만 아주 잠시 동안만이다.

3. 올바르게 만든다. 이제 시스템이 작동하므로 직전에 저질렀던 죄악을 수습하자. 좁고 올곧은 소프트웨어 정의의 길로 되돌아와서 중복을 제거하고 초록 막대로 되돌리자.

---

amount를 private으로 만들기
~~Dollar 부작용?~~
Money 반올림?

최대한 빨리 초록 닥대를 보기 위해 취할 수 있는 전략으로 아래 3가지 중 2가지.

1. 가짜로 구현하기 - 상수를 반환하게 만들고 진짜 코드를 얻을 때까지 단계적으로 상수를 변수로 바꾸어 간다.
2. 명백한 구현 사용 - 실제 구현을 입력

위와 같은 생각은 설계 논의로 이어질 수 있다. 우선 시스템이 이런 식으로 동작해야 하는지 저런 식으로 동작해야 하는지 논의할 수 있수 이따. 일단 올바른 행위에 대해 결정을 내린 후에 긓 애위를 얻어낼 수 있는 최상의 방법에 대해 이야기할 수 있다.
---

amount를 private으로 만들기
~~Dollar 부작용?~~
Money 반올림?
equals()
hashcode()

여기서 삼각측량이란 개념이 나온다.

필요 도구 1
```java
@Test
    void equality() {
        assertEquals(new Dollar(5), new Dollar(5));
    }
```

필요 도구 2
```java
@Test
    void equality() {
        assertEquals(new Dollar(5), new Dollar(5));
        assertFalse(new Dollar(5).equals(new Dollar(6)) );
    }
```

여기서 말하는 삼각 측량이란 개념은 True 테스트 케이스와 False 테스트를 작성 후, 맞추기 위한 구현을 하는 것을 말합니다.

---

amount를 private으로 만들기
~~Dollar 부작용?~~
Money 반올림?
~~equals()~~
~~hashcode()~~
5CHF * 2 = 10CHF

테스트를 리팩토링헀기 때문에 Franc 를 만드는 것이 단순화 된다.

급하게 생각하지 말고, 기분을 전환하는 숨을 쉬며 천천히 접근하기.
1. 테스트 작성
2. 컴파일되게 하기.
3. 실패하는지 확인하기 위해 실행.
4. 실행하게 만듦.
5. 중복 제거.

각 단계에는 서로 다른 목적. 다른 스타일의 해법, 다른 미적 시각을 필요로 한다. 그러면 새 기능이 포함되더라도 잘 알고 있는 상태에 이를 수 있다. 거기에 도달하기 위해서라면 어떤 죄든 저지를 수 있다. 그동안 만큼은 속도가 설계보다 더 높은 패이기 때문이다.

 적절한 시기에 적절한 설계를. 돌아가게 만들고, 올바르게 만들어라.

```java
package money;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DollarTest {

    @Test
    void multiplication() {
        Dollar five = new Dollar(5);
        assertEquals(five.times(2), new Dollar(10));
        assertEquals(five.times(3), new Dollar(15));
    }

    @Test
    void equality() {
        assertEquals(new Dollar(5), new Dollar(5));
        assertFalse(new Dollar(5).equals(new Dollar(6)) );
    }

    @Test
    void francMultiplication() {
        Franc five = new Franc(5);
        assertEquals(five.times(2), new Franc(10));
        assertEquals(five.times(3), new Franc(15));
    }
}
```

Franc 가 만들어지면서 코드가 중복됐다. 다음 테스트를 작성하기 전에 이것들을 제거해야 한다. equals()을 일반화하는 것부터 시작하자.

- 큰 테스트를 공략할 수 없다. 그래서 진전을 나타낼 수 있는 자그마한 테스트를 만들었다.
- 뻔뻔스럽게도 중복을 만들고 조금 고쳐서 테스트를 작성
- 설상가상으로 모델 코드까지 도메인으로 복사하고 수정해서 테스트 통과
- 중복이 사라지기 전에는 집에 가지 않겠다고 약속했다.

---

amount를 private으로 만들기
~~Dollar 부작용?~~
Money 반올림?
~~equals()~~
~~hashcode()~~
~~5CHF * 2 = 10CHF~~
hashCode()
Equal null
Equal object
Dollar/Franc 중복
~~공용 equals~~
공용 times
**Franc과 Dollar 비교하기**

---


amount를 private으로 만들기
~~Dollar 부작용?~~
Money 반올림?
~~equals()~~
~~hashcode()~~
~~5CHF * 2 = 10CHF~~
hashCode()
Equal null
Equal object
Dollar/Franc 중복
~~공용 equals~~
공용 times
~~Franc과 Dollar 비교하기~~

```java
package money;

abstract class Money {
    protected int amount;

    public Money(int i) {
        this.amount = i;
    }

    public static Dollar dollar(int i) {
        return new Dollar(i);
    }

    public static Franc franc(int i) {
        return new Franc(i);
    }

    @Override
    public boolean equals(Object obj) {
        Money dollar = (Money) obj;
        return amount == dollar.amount && getClass().equals(dollar.getClass());
    }

    abstract Money times(int i);
}
```
---
12장. 더하기



- 큰 테스트를 작은 테스트로 줄여서 발전을 나타낼 수 있도록
- 우리에게 필요한 계산에 대한 가능한 메타포들을 신중히 생각
- 새 메타포에 기반하여 기존의 테스트 재작성
- 테스트를 빠르게 컴파일
- 그리고 테스트를 실행
- 진짜 구현을 만들기 위해 필요한 리팩토링을 약간의 전율과 함께 기대했다.


