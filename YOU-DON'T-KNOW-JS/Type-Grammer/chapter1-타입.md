# 1. 타입


`자바스크립트에서 타입이라는 말의 의미는 무엇일까?`

### 내장 타입

- null
- undefined
- boolean
- number
- string
- object
- symbol



값 타입은 typeof 연산자로 알 수 있고, 해당 연산자의 리턴값은 언제나 String이다.

```javascript
typeof undefined === "undefuned" // true
typeof true === "boolean" // true
typeof 42 === "number" // true
typeof "42" === "string" // true
typeof {life: 42} === "object" // true

typeof Symbol() === "Symbol" // true	
```

여기서 예외가 존재하는데 바로 Null

```javascript
type null === "object"; //true
```

명확하게 null을 이해하기 위해서는 `falsy` 하다고 이해해야만 한다.

정확히는

```javascript
var a = null;
(!a && typoof a === "object"); // true
```

또다른 예외는 바로 function

```javascript
typeof function a() {/* ... */} === "function"; //true
```

function이 최상위 레벨의 내장 타입처럼 보이지만 명세를 읽어보면 실제로는 object의 하위타입이다. 구체적으로 설명하면 함수는 '호출 가능한 객체' 라고 명시.



### 값은 타입을 가진다.

```javascript
var a = 42
typeof a; //"number"

a = true;
typeof a; // "boolean"

typeof typeof 42; // "string"
```

정확히 typeof는 "이 변수의 타입은 무엇이니?" 라는 질문보다는 , 이 변수에 들어있는 값의 타입은 무엇이니가 더 적합하다.



### 값이 없는 vs 선언되지 않은

값이 없는 변수의 값은 undefined이며, typeof결과는 "undefined"이다

```javascript
var a;
typeof a; // "undefined"

var b = 42;
var c;

//그러고 나서
b = c;
typeof b; // "undefined"
typeof C; // "undefined"

var a;
typeof a;
typeof b;
```

선언되지 않은 변수도 typeof하면 "undefined"로 나온다. b는 분명 선언조차 하지 않은 변수인데 typeof b를 해도 브라우저는 오류처리를 하지 않는다. 바로 이것이 typeof 만의 독특한 안전 가드다.



### 선언되지 않은 변수

typeof 안전 가드는 전역 변수를 사용하지 않을 때에는 유용한데, 일부 개발자들은 이런 설계 방식이 그다지 바람직하지 않다고 말한다. 이를테면 다른 개발자가 여러분이 작성한 유틸리티 함수를 자신의 모듈/프로그램에 카피앤페이스트하여 사용하는데, 가져다 쓰는 프로그램에 유틸리티의 특정 변숫값이 정의되어 있는지 체크해야 하는 상황을 가져해 보자.

```javascript
function doSomethingCool(){
    var helper =
    	(typeof FeatureXYZ !== "undefined")?
    	FeatureXYZ :
    	function(){
            
    	};
    	var val = helper();
    	//...
}
```



```javascript
//IIFE(즉시 호출 함수 표현식)
(function(){
    function FeatureXYZ(){/*... 나의 XYZ 기능 ...*/ }
    
    // 'doSOmethingCool()'를 포함
    function doSomethingCool(){
        var helper = 
            (typeof FeatureXYZ !== "undefined") ?
                FeatureXYZ :
                function(){/*... 나의 XYZ 기능 ...*/ }
            var val = helper();
            //...
            }
        doSomethingCool();
})();
```

또는 의존성 주입 설계 패턴에서는
```javascript
function doSomethingCool(FeatureXYZ){
    var helper = FeatureXYZ ||
      function() {/*... 나의 XYZ 기능 ...*/};
    var val = helper();
    //...
    }
    
```
 
### WRAP UP

자바스크립트에는 7가지 내장 타입이 있으며, typeof 연산자로 타입명을 알아낸다.

변수는 타입이 없지만 값은 타입이 있고, 타입은 값의 내재된 특성을 정의한다.

"undefined"와 "undeclared"가 대충 같다고 보는 개발자들이 많은데, 자바 스크립트 엔진은 둘은 전혀 다르게 취급한다. undefined는 선언된 변수에 할당할 수 있는 값이지만, undeclared는 변수 자체가 선언된 적이 없음을 나타낸다.

불행히도 자바스크립트는 이 두 용어를 대충 섞어 버려, 에러메시지 뿐만아니라, typeof 반환 값도 모두 "undefined"로 뭉뚱그린다.

그래도  에러를 내지 않는 typeof 안전 가드 덕분에 선언되지 않은 변수에 사용하면 제법 쓸만하다.
