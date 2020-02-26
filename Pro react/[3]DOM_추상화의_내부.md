## 리액트의 이벤트
 리액트는 합성 이벤트 시스템을 구현해 리액트 애플리케이션과 인터페이스에 일관성과 고성능을 보장한다.

 일관성을 위해 이 이벤트 시스템은 이벤트가 여러 다른 브라우저와 플랫폼에서 동일한 속성을 갖도록 이벤트를 정규화한다.

 또한 고성능을 위해 자동으로 이벤트를 위임한다. 리액트가 이벤트 핸들러를 노드 자체에 연결하는 것은 아니다. 실제로는 단 하나의 이벤트 리스너가 문서 루트에 연결되며, 이벤트가 발생하면 리액트가 이를 적절한 컴포넌트 요소로 매핑한다. 또한 리액트는 이벤트 리스너가 언마운트될 때 자동으로 이를 제거한다.

### DOM 이벤트 리스너

HTML은 태그 특성을 위한 간단하고 이해하기 쉬운 이벤트 처리 API를 제공한다. 이 API의 문제는 바람직하지 않은 부수효과가 많기 때문이다. 이 API는 전역 스코프를 오염시키며, 큰 HTML파일의 컨텍스트 안에서 추적하기 어렵고, 성능이 떨어지며, 메모리 누수의 원인이 될 수 있다.

JSX는 HTML의 이벤트 처리 API와 비슷하게 사용하기 쉽고 손쉽게 이해할 수 있는 API를 이용하지만 HTML이벤트 처리 API와 달리 바람직하지 않은 부수 효과는 제거했다. 콜백 함수는 컴포넌트 스코프이며 이벤트 위임과 자동 관리 언마운팅을 이용해 영리하게 작업을 처리한다. 그런데 원래 HTML 구현과는 몃 가지 사소한 차이점이 있다는 데 주의해야 한다. 리액트의 속성에는 카멜표기법이 적용된다. 또한 모든 브라우저와 장치에서 일관성을 유지하기 위해 여러 브라우저와 버전에 포함된 모든 변형의 하위 집합을 구현한다.

이전 칸반앱에서 에로우펀션을 활용해 onClick 이벤트 핸들러 안에 다음과 같은 인라인 함수를 추가.

```javascript
onClick={            
  () => this.setState({showDetails: !this.state.showDetails})
                }
```

```javascript
render(){
        let cardDetails;
        if(this.state.showDetails){
            cardDetails = (
                <div className="card__details">
                    {this.props.description}
                    <CheckList cardId={this.props.id}
                               tasks = {this.props.tasks}/>
                </div>
            );
        };
        return (
            <div className="card">
                <div className="card__title" onClick={
                    () => this.setState({showDetails: !this.state.showDetails})
                }
                >{this.props.title}</div>
                {cardDetails}
            </div>
        );
    }
}
```

###### 이놈을 아래와 같이 바꿀수 있다.

```javascript

toggleDetails(){
       this.setState({showDetails: !this.state.showDetails})
   }

   render(){
       let cardDetails;
       if(this.state.showDetails){
           cardDetails = (
               <div className="card__details">
                   {this.props.description}
                   <CheckList cardId={this.props.id}
                              tasks = {this.props.tasks}/>
               </div>
           );
       };
       return (
           <div className="card">
               <div className="card__title" onClick={
                   this.toggleDetails.bind(this)
               }>{this.props.title}</div>
               {cardDetails}
           </div>
       );
   }
}

```

### JSX 자세히 살펴보기
JSX는 자바스크립트 코드 안에 선언적인 XML 스타일의 구문을 작성할 수 있게 해주는 리액트의 선택적 자바스크립트 구문 확장이다.

리액트의 JSX는 웹 프로젝트에 대해서는 HTML과 비슷한 XML 태그 집합을 제공하지만, 다른 XML태그 집합을 이용해 사용자 인터페이스를 작성하는 사용 사례(예:리액트와 SVG, 리액트 캔버스, 리액트 네이티브)

트랜스파일(브라우저나 서버가 코드를 해석할 수 있도록 일반 자바스크립트로 변환)을 거치면 XML은 리액트 라이브러리에 대한 함수 호출로 변환된다.
```HTML
<h1>Hello World</h1> => React.createElement("h1",null,"Hello World")
```

**JSX의 장점**
- XML은 특성을 이용한 요소 트리로 UI를 표현하는 데 아주 적절하다.
- 애플리케이션의 구조를 시각화하기 쉬우며 더 간결하다.
- 일반 자바스크립트이므로 언어의 의미를 변형시키지 않는다.

**JSX의 특징**
- 태그 특성은 카멜표기법으로 작성한다.
```HTML
<input type = "text" maxlength="30"/>

=> React는

return <input type="text" maxLength="30">
대문자 기입
```
- 모든 요소는 짝이 맞아야 한다.
```HTML
```
- 특성 이름은 DOM API 기반이다.
```HTML
class 와 className
```

**JSX의 특이점**

JSX에는 다루기 까다로운 측면이 있다. 이번 절에서는 JSX로 컴포넌트를 작성할 때 경험할 수 있는 일반적인 문제에 대처하는 간단한 기법과 팁, 전략에 대해 알아보쟈

*단일 루트 노드*
 리액트 컴포넌트는 단일 루트 노드만 렌더링 할 수 있다. 이러한 제한이 있는 이유를 알아보기 위해 다음 render 함수의 return문을 살펴보자.

 ```javascript
  return(
    <h1>Hello World</h1>
  )

  // 이는 다음과 같이 변화된다.
  return React.createElement("h1", null, "Hello World");

  // 그러나 다음과 코드는 유효하지 않다.
  return(
    <h1>Hello World</h1>
    <h2>Hello World</h2>
  )
 ```

 정확히 말하면 이것은 JSX의 제한이 아니라 자바스크립트의 특징이다. return문은 단일 값만 반환할 수 있지만, 이 코드는 두 개의 문을 반환하려고 한다. 해결책은 아주 간단한데, 일반 자바스크립트와 마찬가지로 모든 반환값을 루트 객체 하나에 래핑하면 된다. 예를 들어 다음과 같이 작성할 수 있다.

```javascript
return(
  <div>
    <h1>Hello World</h1>
    <h2>Hello World</h2>
  </div>
)

// 위 코드는 다음과 같이 변환한다면,
return React.createElement("div", null,
React.createElement("h1", null, "Hello World"),
React.createElement("h2", null, "World"),
)

```
즉, 단일 값을 반환하는 유효한 자바스크립트 코드다.

*조건 절*

if문은 JSX와는 잘 어울리지 않지만 이는 JSX의 제한이 아니라 JSX가 사실은 일반 자바스크립트이기 때문이다. 이해하는 데 도움이 되도록 JSX가 일반 자바스크립트로 변환되는 방법을 다시 확인해보자.

다음과 같은 JSX가 있다고 가정해보자.

```javascript
return(
  <div className="salutation">Hello JSX</div>
)
```

 그런데 다음과 같이 JSX중간에 IF절을 넣었다고 가정해 보자.
 ```javascript
 <div className={if(condition) {"salutation"}}>
 Hello JSX
 </div>
 ```

 이렇게 되면 에러가 발생한다. 어떻게 해결하겠는가??

 JSX안에 if문을 사용할 수 없지만 삼항식을 이용하고 조건에 따라 변수에 값을 할당하는 방법과 같은 해결책이 있다.

 리액트는 null과 정의되지 않은 값을 인식하며 JSX에서 이스케이프 처리할 경우 아무것도 출력하지 않는다.

 <해결 방안>
 1. 삼항식 이용
 ```javascript
 render(){
   return(
     <div className={condition ? "salutation" : ""}>
     Hello JSX
     </div>
    )
 }
 ```
 삼항식은 조건에 따라 전체 노드를 렌더링하는 경우에도 잘 동작한다.

```
<div>
 {condition ? <span>Hello JSX</span> : null}
Hello JSX
</div>
```
 2. 조건을 밖으로 이동
 삼항식으로 문제를 해결할 수 없을 때는 조건절을 JSX안쪽에서 바깥쪽으로 옮기는 방법이 있다.
```javascript
  render(){
    return(
      <div className={condition ? "salutation" : ""}>
      Hello JSX
      </div>
     )
  }
```

이 놈을.
```javascript
let className;
if(condition){
  className="salutation";
}
render(){
  return(
    <div className={condition}>
    Hello JSX
    </div>
   )
}
```

리액트는 정의되지 않은 값을 처리하는 방법을 이해하며, 조건이 false일 경우 div 태그 안에 클래스특성을 생성하지 않는다.

#### 칸반 앱: 카드가 열려있는지 여부 확인
```javascript
return (
         <div className="card">
             <div style={sideColor}/>
             <div className={
                 this.state.showDetails?"card__title card__title--is-open" : "card__title"
             } onClick={
                 this.toggleDetails.bind(this)
             }>{this.props.title}</div>
             {cardDetails}
         </div>
     );

    //  =>CSS
     .card__title:before {
       display: inline-block;
       width: 1em;
       content: '▸';
     }

     .card__title--is-open:before {
       content: '▾';
     }
```

#### 공백
HTML의 경우 브라우저는 일반적으로 여러 행의 요소 간에 공백을 출력한다. 반면 리액트의 JSX는 분명한 지시가 있을 때만 공백을 출력한다. 예를 들어, 다음 JSX는 행 사이에 출력하지 않는다.

명시적으로 공백을 삽입하려면 빈 문자열{""}을 포함하는 식을 이용한다.

#### JSX의 주석

JSX는 HTML이 아니므로 HTML주석을 지원하지 않는다. 단,

이렇게 사용가능하다.
```javascript
let content = (
  <Nav>
    {/* 자식 주석이므로 {}로 감싼다.*/}
    <Person
    /* 다중
    행
    주석 */
    name={window.isLoggedIn ? window.name : ''} //행 끝 주석
    />
    </Nav>
);
```
#### 동적 HTML 렌더링
리액트에는 XSS 공격방지 기능이 기본적으로 내장돼 있다. 즉,HTML태그를 동적으로 생성하고 JSX에 추가하는 작업을 기본적으로 금지하낟. 이 기본 설정은 보안을 위해서는 바람직하지만 HTML을 동적으로 생성해야 하는 경우도 있다. 데이터를 마크다운 포맷으로 인터페이스를 렌더링하는 경우를 예를 들어보자

일단 마크드(Marked)라는 것을 설치하고
마크다운을 HTML로 변환한다.

```javascript
// CARD.js
render() {
    let cardDetails;
    if (this.state.showDetails) {
      cardDetails = (
        <div className="card__details">
          <span dangerouslySetInnerHTML={{__html:marked(this.props.description)}} />
          <CheckList cardId={this.props.id} tasks={this.props.tasks} />
        </div>
      );
    }

```

#### 인라인 스타일링
JSX로 리액트 컴포넌트를 작성하는 것은 같은 파일 안에 UI정의와 상호작용을 경합하는 것.
앞에서 설명한 것 처럼 관심사를 분리하려면 각 관심사에 대해 잘 캡슐화되고, 독립적이며, 재사용 가능한 컴포넌트를 이용해야 한다. 그런데 사용자 인터페이스의 경우에는 콘텐츠와 상호작용 외에도 스타일을 고려해야 한다.

리액트는 자바스크립트를 이용한 인라인 스타일일을 지원하는데,
장점으로
- 셀럭터 없이 스타일의 범위지정 기능
- 특정성충돌이 예방
- 소스 순서에 관계없음

#### 폼 처리

리액트에서는 컴포넌트의 상태가 변경될 때마다 컴포넌트를 다시 렌더링해야 하므로 컴포넌트의 내부 상태를 최소환으로 유지한다. 리액트가 컴포넌트를 다시 렌더링하는 이유는 자바스크립트 코드상의 컴포넌트 상태를 정확하게 나타내고 인터페이스의 동기화를 유지하기 위해서다.

따라서 사용자가 상호작용하면 상태가 변경되는 <Input><TextArea><option> 과 같은 폼 컴포넌트는 HTML과 다르게 이용된다.

리액트는 폼을 컴포넌트로서 처리하는 두 가지 방식(제어, 비제어 컴포넌트)을 지원하며, 앱의 특성이나 개인 선호에 따라 맞는 방식을 선택할 수 있다.

##### 제어 컴포넌트

값이나 확인되는 속성을 가지는 폼 컴포넌트를 제어 컴포넌트,
제어 컴포넌트의 요소 안에서 렌더링되는 값은 항상 속성의 값을 반영한다. 기본적으로 사용자는 이를 변경할 수 없다.

즉, 태스크의 체크박스를 클릭해도 체크박스는 바뀌지않는다, 이 체크박스는 CardList배열에 하드코딩한 값을 반영하며 배열 자체를 수정해야 체크박스도 변경된다.

만약 변경 불가능한 값을 렌더링하고 있다면, 이를 해결하기 위해서는 어떻게 해야 될까?

이 값을 변경할 수 있게 하려면 이를 컴포넌트 상태로서 처리해야 한다.

```javascript
class Search extends Component {
  constructor() {
    super();
    this.state={
      searchTerm = "React"
    };
  }

  render(){
    return(
      <div>
      Search Term;
      <input type:"search" value = {this.state.searchTerm} />
      </div>
    )
  }
}
```

이렇게 하고 최종적으로 사용자가 값을 업데이트할 수 있도록 하려면 onChange이벤트를 이용하면된다.

```javascript

class Search extends Component {
  constructor() {
    super();
    this.state={
      searchTerm = "React"
    };
  }

  handleChange(event){
    this.setState({searchTerm: event.target.value});
  }

  render(){
    return(
      <div>
      Search Term;
      <input type:"search" value = {this.state.searchTerm}
      onChange=this.handleChange.bind(this) />
      </div>
    )
  }
}

```

단순히 폼을 다루기 위한 방법으로는 복잡해 보이더라도, 다음과 같은 장점이 있다.
- 리액트가 컴포넌트를 다루는 방법을 준수한다. 상태가 인터페이스 바깥의 자바스크립트 코드에서 완전히 관리된다.
- 이 패턴은 사용자 상호작용에 반응하거나 유효성을 검사하는 인터페이스를 구현하는 데 유리하다. 예를 들어 다음과 같이 하면 손쉽게 사용자 입력을 50자로 제한 할 수 있다.
this.setState({searchTerm: event.target.value});this.setState({searchTerm: event.target.value});

this.setState({searchTerm: event.target.value.substr(0,50)});

##### 비제어 컴포넌트

제어 컴포넌트는 리액트의 원칙을 준수하며 그에 따른 혜택을 누린다. 반면 비제어 컴포넌트는 리액트에서 다른 대부분의 컴포넌트가 구성되는 방법과는 다른 안티패턴이지만 때로는 사용자 입력필드를 관리할 필요가 없는 경우가 있다.

특히 큰 폼에서는 사용자가 필드를 입력하게 한 후 입력이 모두 끝나면 필요한 처리를 모두 할 수 있다. 값을 제공하지 않는 모든 입력 컴포넌트가 비제어 컴포넌트이며, 렌더링되는 요소의 값은 사용자의 입력에 의해 결정된다.

```html
return(
  <form>
  <div className="formGroup">
  Name:<input name="name" type="text"/>
  </div>
  <form>
  <div className="formGroup">
  Email:<input name="email" type="mail"/>
  </div>
  <button type="submit">Submit</button>
  </form>

)
```

이 예제는 빈 값으로 시작하는 입력 필드 두 개를 렌더링하는데, 사용자가 입력을 시작하는 즉시 렌더링된 요소에 그 내용이 반영된다..

다음과 같이 onSubmit을 이용해 비제어 컴포넌트 폼을 처리하는 것이 가능하다.

```javascript
 handleSubmit(event){
   console.log("Submitted values are: ",
 event.target.name.value,
 event.target.email.value);
 event.preventDefault();
 }

render(){
   return(
     <form onSubmit={this.handleSubmit}>
     <div className="formGroup">
     Name:<input name="name" type="text"/>
     </div>
     <form>
     <div className="formGroup">
     Email:<input name="email" type="mail"/>
     </div>
     <button type="submit">Submit</button>
     </form>
   )
 }
```


#### 가상DOM의 작동 방식

지금까지 살펴본 것처럼 리액트 설계의 핵심적인 측면 중 하는 업데이트가 수행될 때마다 모든 것을 다시 렌더링하는 것처럼 API가 구성됐다는 점이다. DOM조작은 여러가지 이유로 속도가 느리므로 리액트는 성능을 개선하기 위해 가상 DOM을 구현한다. 리액트는 애플리케이션의 상태가 바뀔 때마다 실제 DOM을 업데이트하는 대신 원하는 DOM상태와 비슷한 가상트리를 생성한다. 그런 다음 전체 DOM모드를 다시 생성하지 않고도 실제 DOM을 가상 DOM과 같이 만드는 방법을 알아낸다.

가장 DOM트리와 실제 DOM트리를 동일하게 만드는 데 필요한 최소 변경 횟수를 알아내는 프로세스를 조정이라고하며, 일반적으로 이 작업은 아주 복잡하고 실행 비용이 높다. 이러한 조정 작업은 여러 차례에 걸쳐 반복과 최적화를 거친 후에도 매우 까다롭고 시간을 많이 보시한다. 리액트는 이 작업을 조금이라도 수월하게 하고 휠씬 빠르고 실용적인 알고리즘을 적용하기 위해 일반적인 애플리케이션의 작동 방법에 대해 몃 가지 사항을 가정한다. 이러한 가정에는 다음과 같은 것이 있다.

#### 키

Key 속성은 고유하고 상수인 어떤 값이라도 포함할 수 있다. 카드의 데이터에는 각 카드의 ID가 포함돼 있으므로 이를 List컴포넌트에서 key속성 이용해보자.

#### ref

이 부분은 다음 참고자료를 참고해주었으면 좋겠다.
[ref  참고 자료](https://github.com/LenKIM/TIL_Today_I_Learned/blob/master/5.React/8.ref:DOM%EC%97%90%20%EC%9D%B4%EB%A6%84%EC%9D%84%20%EB%8B%AC%EC%95%84%EC%A3%BC%EC%9E%90.md)
