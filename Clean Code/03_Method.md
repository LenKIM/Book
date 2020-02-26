## 3장 함수
```
Index
작게 만들어라!
__ 블록과 들여쓰기
한 가지만 해라!
__ 함수 내 섹션
함수 당 추상화 수준은 하나로!
__ 위에서 아래로 코드 읽기: 내려가기 규칙
Switch 문
서술적인 이름을 사용하라!
함수 인수
__ 많이 쓰는 단항 형식
__ 플래그 인수
__ 이항 함수
__ 삼항 함수
__ 인수 객체
__ 인수 목록
__ 동사와 키워드
부수 효과를 일으키지 마라!
__ 출력 인수
명령과 조회를 분리하라!
오류 코드보다 예외를 사용하라!
__ Try/Catch 블록 뽑아내기
__ 오류 처리도 한 가지 작업이다.
__ Error.java 의존성 자석
반복하지 마라!
구조적 프로그래밍
함수를 어떻게 짜죠?
```
#### Intro
---
의도를 분명히 표현하는 함수를 어떻게 구현할 수 있을까? 함수에 어떤 속성을 부여해야 처음 읽는 사람이 프로그램 내부를 직관적으로 파악할 수 있을까?

#### 작게 만들어라
---

***함수를 만드는 첫째 규칙은 '작게'다.***
***함수를 만드는 둘째 규칙은 '더 작게'다.***

```java
public static String renderPageWithSetupsAndTeardowns( PageData pageData, boolean isSuite) throws Exception {
	boolean isTestPage = pageData.hasAttribute("Test");
	if (isTestPage) {
		WikiPage testPage = pageData.getWikiPage();
		StringBuffer newPageContent = new StringBuffer();
		includeSetupPages(testPage, newPageContent, isSuite);
		newPageContent.append(pageData.getContent());
		includeTeardownPages(testPage, newPageContent, isSuite);
		pageData.setContent(newPageContent.toString());
	}
	return pageData.getHtml();
}
```
위 코드를 다음과 같이 줄일 수 있다.

```java
public static String renderPageWithSetupsAndTeardowns{
  PageData pageData, boolean isSuite) throws Exception {
    if(isTestPage(pageData))
    includeSetupAndTeardownPages(pageData, isSuite);
  return pageData.getHtml();
  }
}
```
 다음과 같이 줄일 수 있다.  

**위와 같이 함수를 줄여서 생기는 부작용 또는 장점은 무엇인가?**  

#### 블록과 들여쓰기
---
if 문 / else 문/ while 문 등에 들어가는 블록은 한 줄이어야 한다는 의미이다. 대개 거기서 함수를 호출하겠지?
그러면 바깥을 감싸는 함수가 작아질 뿐 아니라, 블록 안에서 호출되는 함수 이름을 적절히 짓는다면, 코드를 이해하기 쉬워진다.  

 키 포인트는 함수를 읽고 이해하기 쉬워야 한다라는 것!!  
어느 정도 중첩 구조는 허용할 지라도 함수는 절대 커지면 안된다!  

#### 한가지만 해라
---

***함수는 한 가지를 해야 한다. 그 한 가지를 잘 해야 한다. 그 한 가지만을 해야 한다.***

 무슨 말 일까? 함수가 하는 기능은 한 가지만 수행해야 한다?  

반은 맞고 반은 틀리다.  

여기서 만약 renderPageWithSetupsAndTeardowns() 하는 함수가 있다.  
지정된 함수 이름 아래에서 추상화 수준이 하나인 단계만 수행한다면 그 함수는 한 가지 작업만 한다.  

이 말의 뜻은 즉, 함수의 이름이 길더라도 함수의 의미가 추상화된 내용을 제대로 한 가지를 뜻하고 있다면 적절하다라고 표현한다.  

#### 함수 당 추상화 수준은 하나로!
---

추상화 수준이란 무엇일까?

추상화 수준이 높다, 낮다 라는것은 함수명의 가독성이 얼마나 좋은가를 기준으로 판별할 수 있다고 판단한다(+주관적인생각)  

`getHtml()은 추상화 수준이 높은 편`
`String pagePathName = PathParser.render(pagepath)은 중간 수준의 추상화`
`.append("\n")는 추상화 수준이 낮은 편`

 한 함수 내에 추상화 수준을 섞으면 코드를 읽는 사람이 헷갈린다. 특정 표본이 근본 개념인지 아니면 세부사항인지 구분하기 어려운 탓이다. 근본 개념과 세부사항이 뒤섞기 시작하면, 깨어진 창문처럼 함수에 세부사항을 점점 더 추가한다.  

 - 위에서 아래로 코드 읽기: ***내려가기*** 규칙
 코드는 위에서 아래로 이야기처럼 읽여야 좋다! 한 함수 다음에는 추상화 수준이 한 단계 낮은 함수가 온다.  
 즉, 위에서 아래로 프로그램을 읽으면 함수 추상화수준이 한 단계씩 낮아진다. 이것을 내려가기 규칙

 이를 지키기 어렵지만 굉장히 중요한 규칙이다.
 ***중요한 것은 '한 가지만'하는 함수****


#### Switch 문
----

**Switch문 대신 활용하는 방법을 예를 통해 파악하기?**

Switch문은 작게 만들기 어렵다. 단일 블록이나 함수를 선호. Switch문을 완전히 피할 수는 없지만 저자는 저차원 클래스에 숨기고 절대로 반복하지 않는 방법으로 활용한다. 더하기, 다형성을 이용한다.

```java
public Money calculatePay(Employee e) throws InvalidEmployeeType {
  switch (e.type) {
    case COMMISSIONED :
      return calculateCommissionedPay(e);
    case HOURLY :
      return calculateHourlyPay(e);
    case SALARIED:
      return calculateSalariedPay(e);
    default:
    throw new InvalidEmployeeType(e.type);
  }
}
```

위 코드는 잘 짜여진 코드 일까? 아니다.
일단 첫번째, 함수가 길다.
두번째, '한 가지'작업만 수행하지 않는다.
세번째, 'SRP(Single Responsibility Principle)'을 위반한다. 코드를 변경할 이유가 여럿!
넷째, 'OCP'위반 새 직원 유형을 추가할 때마다 코드를 변경하기 때문에.

위의 문제를 해결해보면?

```java
public abstract class Employee{
  public abstract boolean isPayday();
  public abstract Money calculatePay();
  public abstract void deliverPay(Money pay);
}
-----------------------------------------------------------
public interface EmployeeFactory {
  public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType;
}

-----------------------------------------------------------

public class EmployeeFactoryImpi implements EmployeeFactory {

  public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType {
    switch (r.type) {
      case COMMISSIONED:
      return new CommissionedEmployee(r);
      case HOURLY:
      return new HourlyEmployee(r);
      case SALARIED :
      return new  SalariedEmployee(r);
      default:
      throw new InvalidEmployeeType(r.type);
    }
  }
}
```

 이렇게 상속관계로 숨긴 후에는 절대로 다른 코드에 노출하지 않는다.  
#### 서술적인 이름을 사용하라!
---
 ***좋은 이름이 주는 가치는 아무리 강조해도 지나치지 않다.***
 - 코드를 읽으면서 짐작했던 기능을 각 루틴이 그대로 수행한다면 깨끗한 코드라 불러도 되겠다. 한가지만 하는 작은 함수에 좋은 이름을 붙인다면 이런 원칙을 달성함에 있어 이미 절반은 성공했다. 함수가 작고 단순할 수록 서술적인 이름을 고르기도 쉬워진다. 시간이 오래걸려도 괜찮고, 이름이 길어도 괜찬으니 일관성있는 이름을 부여해라!  

#### 함수 인자
----
함수에서 이상적인 인자 개수는 0개  
그 다음은 1개, 그 다음은 2개.  

인수를 활용하면 가독성이 극도로 줄어든다. StringBuffer의 경우만 봐도 볼때마다 읽는 사람은 그 코드를 이해해야한다.  

테스트 관점에서 인수활용은 더 어렵게 만든다.

최선은 입력 인수가 없는 경우이며, 차선은 입력 인수가 1개뿐인 경우다. SetupTeardownIncluder.render(pageData)는 이해하기 아주 쉽다. pageData 객채 내용을 랜더링 하겠다른 뜻!

- 활용
인수를 1개를 넘기는 이유는 크게 두 가지.
첫번째는 인수에 질문을 던지는 경우이다. boolean fileExists("MyFile")이 좋은 예다.  
다른 하나는 인수를 뭔가로 변환해 결과를 반환하는 경우.

- 플래그 함수
 요즘은 IDE가 좋아져서 해당이 안될 수도 있지만, render(boolean isSuite)를 renderForSuite()와 renderForSuiteTest()라는 함수로 나눠야 적절.
- 이항 함수
- 삼항 함수
 왠만하면 쓰지 않는 편이 좋다. 이유는 이렇게 작성하다보면 무시하는 코드가 발생하고 그 무시하는 코드는 위험을 발생 시킨다! 그러므로 사용하지 않는 편이 좋다. 그러나 Point(int x, int y)이런거는 당연히 해야한다.

 이항 삼항의 함수의 경우 인수 객체로 생산자를 만들어 오버 로딩을 할 때 활용하는 것이 적절하다.

- 인수 목록
 ... 의 의미란? 가변 인수를 넣어줄 수 있다.
- 동사와 키워드
 함수의 의도나 인수의 순서와 의도를 제대로 표현하려면 좋은 함수 이름이 필수다. 단항 함수는 함수와 인수가 동사/명사 쌍을 이뤄야 한다. 예를 들어 write(name)은 누구나 곧바로 이해한다. 즉, 함수 이름에 ***키워드*** 를 추가하는 형식이다. 즉, 함수 이름에 인수 이름을 넣는다.

#### 부수 효과를 일으키지 말자.
함수안에 특정 함수가 부수적인 효과를 일으킬 가능성있는 코드에 주의하라!
정... 불안하면 함수에 이름작성할 때 같이 작성하자.
#### 명령과 조회를 분리하라!

***1.함수는 뭔가를 수행하거나 뭔가를 답하거나 둘 중 하나만 해야한다.***
***2.객체 상태를 변경하거나 아니면 객체 정보를 반환하거나 둘 중 하나다..***

`public boolean set(String attr, String value);`

이 함수는 이름이 attr인 속성을 찾아 값을 value로 설정한 후 성공하면 true를 반환하고 실패하면 false를 반환

근데 이런 코드가 있다. if(set("username", "unclebob"))...

위 조건을 위반했다.

```java
if(attribiteExitsts("userName")){
  setAttribute("username", "unclebob");
} ...
```

이런식으로!

### 오류 코드보다 예외로 사용하라!
----

```java
if(deletePage(page) == E_OK){
  if(registry.deleteReference(page.name) == E_OK){
    if(configKeys.deleteKey(page.name.makeKey()) == E_OK){
      logger.log("page deleted");
    } else {
      logger.log("configKey not deleted");
    }
  } else {
    logger.log("deleteReference from registry failed");
  }
} else {
  logger.log("dekete failed");
  return E_ERROR;
}
```
을 다음과 같이 수정  

```java
try{
  deletePage(page);
  registry.deleteReference(page.name);
  configKeys.deleteKey(page.name.makeKey());
} Catch (Exception e){
  logger.log(e.getMessage());
}
```

### 반복하지 마라!
---
 끊임없는 코드리뷰를 통해 중복을 제거하라.  
### 구조적 프로그래밍
---
### 함수는 어떻게 짜야될까?

 소프트웨어를 짜는 행위는 여느 글짓기와 비슷하다. 일단 생각을 기록한 후 읽기 좋게 다듬고, 읽힐 때까지 말을 다듬고 문장을 고치고 문단을 정리한다.  

 함수도 이와 유사하다. 생각을 기록하고, 좋게 다듬고, 이름을 바꾸고, 중복을 제거한다. 메서드를 줄이고 순서를 바꾼다. 때로는 전체 클래스를 쪼개기도 한다. 이 와중에도 코드는 항상 단위 테스트를 통과한다.  

### 결론
모든 시스템은 프로그래머가 설계한 도메인 특화 언어로 만들어 진다. 함수는 그 언어에서 동사이며, 클래스는 명사다. 요구사항 문서에 나오는 명사와 동사를 클래스와 함수 후보로 고려한다는 끔찍한 옛 규칙으로 역행하자는 이야기가 아니다.
 이번 장에는 함수를 잘 만드는 기교를 소개했다. ***함수도 이야기로 풀어가는데 있다는 사실을 명심하자.***
