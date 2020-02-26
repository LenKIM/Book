# 디폴트 메서드

- 디폴트 메서드란 무엇인가?
- 진화하는 API가 호환성을 유지하는 방법
- 디폴트 메서드의 활용 패턴
- 해결 규칙



자바8에서는 기본 구현을 포함하는 인터페이스를 정의하는 두 가지 방법을 제공한다. 

첫 번째는 인터페이스 내부에 **정적 메서드(static mathod)**를 사용하는 것

두 번째는 인터페이스의 기본 구현을 제공할 수 있도록 **디폴트 메서드** 라는 기능을 사용하는 것



즉, 자바8 에서는 메서드 구현을 포함하는 인터페이스를 정의할 수 있다. 결과적으로 기존 인터페이스를 구현하는 클래스는 자동으로 인터페이스에 추가된 새로운 메소드의 디폴트 메서드를 상속받게 된다. 이렇게 하면 기존의 코드 구현을 바꾸도록 강요하지 않으면서도 인터페이스를 바꿀 수 있다.



결국 인터페이스가 아니라 추상 클래스 아닌가? 인터페이스와 추상 클래스는 같은 점이 많아 졌지만 여전히 다른 점도 있다. 

 디폴트 메서드를 사용하는 이유는 뭘까? 디폴트 메서드는 주로 라이브러리 설계자들이 사용한다.

![image-20190823225406532](/Users/lenkim/Library/Application Support/typora-user-images/image-20190823225406532.png)



디폴트 메서드가 없던 시절에는 인터페이스에 메서드를 추가하면서 여러 문제가 발생했다. 인터페이스에 새로 추가된 메서드를 구현하도록 인터페이스를 구현하는 기존 클래스를 고쳐야 했기 때문이다. 본인이 직접 인터페이스와 이를 구현하는 클래스를 관리할 수 있는 상황이라면 이 문제를 어렵지 않게 해결할 수 있지만 인터페이스를 대중에 공개했을 때는 상황이 다르다. 그래서 디폴트 메서드가 탄생한 것이다.

**디폴트 메서드** 를 이용하면 인터페이스의 기본 구현을 그대로 상속하므로 인터페이스에 자유롭게 새로운 메서드를 추가할 수 있게 된다.



*이제 디폴트 메서드를 사용하게 되는 이유를 직접 코드를 짜면서 찾아보자.*



맨 처음 Resizeable의 인터페이스 초기 버전은 다음과 같았다.

```java
public interface Resizable extends Drawable{
    public int getWidth();
    public int getHeight();
    public void setWidth(int width);
    public void setHeight(int height);
    public void setAbsoluteSize(int width, int height);
    //TODO: uncomment, read the README for instructions
    //public void setRelativeSize(int widthFactor, int heightFactor);
}
```



해당 인터페이스를 활용해서 `Ellipse` `Util` 등의 함수를 구현 함.

```java
public class Ellipse implements Resizable {
    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

   ....
}
```

```java
import java.util.List;

public class Utils{
    public static void paint(List<Resizable> l){
        l.forEach(r -> { r.setAbsoluteSize(42, 42); });
    }

}

```



이런 상태에서 Resizable에 새로운 기능이 추가되었다.

`public void setRelativeSize(int widthFactor, int heightFactor);`

그럼 모든 코드를 수정해야 되는 문제가 발생한다. 그러나 **바이너리 호환성(인터페이스에 추가되도 추가된 함수를 호출만 하지않으면 오케이 하다.)**은 유지된다.

공개된 API를 고치면 기존 버전과의 호환성 문제가 발생한다. 이런 이유 때문에 공식 자바 컬렉션 API같은 기존의 API는 고치기 어렵다. 물론 API를 바꿀 수 있는 몃 가지 대안이 있지만 완벽한 해결책은 될 수 없다. 예를 들어 자신만의 API를 별도로 만든 다음에 예전 버전과 새로운 버전을 직접 관리하는 방법도 있다.



## 디폴트 메서드란 무엇인가?

위 문제를 해결하기 위해 디폴트 메서드 즉

```java
default void setRelativeSize(int wFactor, int hFactor){
  setAbsoluteSize(getWidth() / wFactor, getHegiht() / hFactor);
}
```

와 같이 한다면 위 코드에 어떤 수정도 없이 문제를 해결 할 수 있게 된다.



*인터페이스가 구현을 가질 수 있고 클래스는 여러 인터페이스를 동시에 구현할 수 있으므로 결국 자바도 다중 상속을 지원하는걸까?*  *인터페이스를 구현하는 클래스가 디폴트 메서드를 오버라이드한다면 어떻게 될까?* 

조금 더 학습을 해보자!



> 추상 클래스와 자바 8의 인터페이스
>
> 추상 클래스와 인터페이스는 뭐가 다를까? 둘 다 추상 메서드와 바디를 포함하는 메서드를 정의할 수 있다.
>
> 첫째, 클래스는 하나의 추상 메서드만 상속받을 수 있지만 인터페이스를 여러 개 구현할 수 있다.
>
> 둘째, 추상 클래스는 인스턴스 변수(필드)로 공통 상태를 가질 수 있다. 하지만 인터페이스는 인스턴스 변수를 가질 수 없다



## 디폴트 메서드 활용패턴

***우리가 만드는 인터페이스에도 디폴트 메서드를 추가할 수 있다.***

### - 선택형 메서드  

Iterator는 hasNext와 next뿐 아니라 remove메서드도 정의한다. 사용자들이 remove 기능을 잘 사용하지 않으므로 자바 8 이전에는 remove기능을 무시했다. 결과적으로 Iterator를 구현하는 많은 클래스에서는 remove에 빈구 현을 제공했다. 

```java
interface Iterator<T> {
  boolean hasNext();
  T next();
  default void remove(){
    throw new UnsupportedOperationException();
  }
}
```



### - 동작 다중 상속

 디폴트 메서드를 이용하면 기존에는 불가능했던 **동작 다중 상속 기능**도 구현할 수 있다.

자바에서 클래스는 한 개의 다른 클래스만 상속할 수 있지만 인터페이스는 여러 개 구현할 수 있다.

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
```

자바8 에서는 인터페이스가 구현을 포함할 수 있으므로 클래스는 여러 인터페이스에서 동작(구현 코드)을 상속받을 수 있다. 다중 동작 상속이 어떤 장점을 제공할 수 있을까? 중복되지 않는 최소한의 인터페이스를 유지한다면 우리 코드에서 동작을 쉽게 재사용하고 조합할 수 있다.



> **Tips**
>
> 옳지 못한 상속
>
> 상속으로 코드 재사용 문제를 모두 해결할 수는 X
>
> 예를 들어 한 개의 메서드를 재사용하려면 100개의 메서드와 필드가 정의되어 있는 클래스를 상속받는 것은 좋은 생각이 아님. 이럴 때는 Delegation, 즉, 멤버 변수를 이용해서 클래스에서 필요한 메서드를 직접 호출하는 메서드를 작성하는 것이 좋다.
>
> 디폴트 메서드에도 이 규칙을 적용할 수 있음. 필요한 기능만 포함하도록 인터페이스를 최소한으로 유지한다면 필요한 기능만 선택할 수 있으므로 쉽게 기능을 조힙할 수 있다.





## 요약

- 자바 8의 인터페이스는 구현 코드를 포함하는 디폴트 메서드, 정적 메서드를 정의할 수 있다.
- 디폴트 메서드의 정의는 default 키워드로 시작하며 일반 클래스 메서드처럼 바디를 갖는다.
- 공개된 인터페이스에 추상 메서드를 추가하면 소스 호환성이 깨진다.
- 디폴트 메서드 덕분에 라이브러리 설계자가 API를 바꿔도 기존 버전과 호환성을 유지할 수 있다.
- 선택형 메서드와 동작 다중 상속에도 다폴트 메서드를 사용할 수 있다.
- 클래스가 같은 시그너처를 갖는 여러 디폴트 메서드를 상속하면서 생기는 충돌 문제를 해결하는 규칙이 있다.
- 클래스나 슈퍼클래스에 정의된 메서드가 다른 디폴트 메서드 정의보다 우선한다. 이 외에 상황에서는 서브인터페이스에서 제공하는 디폴트 메서드가 선택된다.
- 두 메서드의 시그너처가 같고, 상속관계로도 충돌 문제를 해결할 수 없을 때는 디폴트 메서드를 사용하는 클래스에서 메서드를 오버라이드해서 어떤 디폴트 메서드를 호출할지 명시적으로 결정해야 한다.

