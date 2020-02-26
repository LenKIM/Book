## 자동타입변환 = 강제적 타입변환 = 타입 강제변화



### 값 변환

```javascript
var a = 42;
var b = a + ""; // 암시적 강제변환
var c = String(a); // 명시적 강제변환
```

**변환을 어떻게 할 것인가?**

##### '명시적' : '암시적' = '명백한' : '숨겨진 부수 효과' 용어상으로는 이러한 대응 관계가 성립

### 추상 연산

##### ToString

'문자열이 아닌 값 => 문자열' 변환 작업은 ToString 추상 연산 로직이 담당

```javascript
// 1.07 에 1000 을 7번
var a = 1.07 * 1000 * 1000* 1000* 1000* 1000* 1000
a.toString(); // 1.07e21
```

##### JSON 문자열화

ToString은 JSON.stringify() 유틸리티를 사용하여 어떤 값을 JSON문자열로 직렬화화는 문제와도 연관된다.

```javascript
JSON.stringify(42);
JSON.stringify("42");
JSON.stringify(null);
JSON.stringify(true);
```

JSON 안전값은 모두 JSON.stringify()로 문자열화 가능

그러나 환형 참조 객체는 불가.

### ToNumber

'숫자 아닌 값 -> 수식 연산이 가능한 숫자'

true -> 1

false -> 0

undefuned ->NaN

null -> 0

```javascript
var a = {
    valueOf: function(){
        return "42";
    }
};
undefined
var b = {
    toString: function(){
        return "42";
    }
};
undefined
var c = [4,2];
undefined
c.toString = function(){
    return this.join("");
};
ƒ (){
    return this.join("");
}
Number (a);
Number (b);
Number (c);
Number ("");
Number ([]);
Number (["abc"]);

```

### ToBoolean

#### Falsy 값

true/false 가 아닌 값에 불리언에 상단한 값으로 강제변환했을 때 어떻게 작동하띾?

 둘 중 하나

1. 불리언으로 강제변환하면 false가 되는 값
2. 1번을 제외한 나머지



'불리언으로'강제변환 시 모든 가능한 경우의 수가 나열되어 있다.

명세가 정의한 falsy값은 다음과 같다.

- undefined
- null
- false
- +0 -0 NaN
- ""

모두가 falsy한 값. 불리언으로 강제변환하면 false

#### truthy 값

그 외 전부 다.



### 명시적 강제변환

##### 문자열 < > 숫자

`String()` `Number()` 함수 이용.

```javascript
var a = 42;
var b = String(a);
var c = "3.14";
var d = Number(c);
b; //"42"
d; // 3.14
```



```javascript
var a = 42;
var b = a.toString();

var c = "3.14";
var d = +c;

b; //"42"
d; // 3.14
```

#### 날짜 < > 숫자

```javascript
var d= new Date("Mon, 18 Aug 2014 08:53:06 CDT");
+d; // 1408xxxxx

var timestamp = +ne Date();
```

#### 이상한 나라의 틸트(~)

~ 연산자는 먼저 32비트 숫자로 '강제변환'한 후 NOT 연산을 한다.(각 비트를 거꾸로 뒤집는다.)

!이 불리언 값으로 강제변환하는 것뿐만 아니라 비트를 거꾸로 뒤집는 것과 아주 비슷하다.



```javascript
var a = "Hello World";
~a.indexOf("lo"); // -4 <=truthy;
if(~a.indexOf("lo")){
    //찾았다.
}
~a.indexOf("ol"); // 0
!~a.indexOf("ol"); // true
if(!~a.indexOf("ol")){
    //못 찾음.
}
```



비트 잘라내기

~용도로 ToInt32 '강제 변환'을 적용한 후 각 비트를 거꾸로 한다.



### 명시적 강제변환: 숫자 형태의 문자열 파싱

```javascript
var a= "42";
var b = "42px";

Number(a);
parseInt(a);

Number(b); //NaN
parseInt(b); // 42
```



### 명시적 강제변환 : -> 불리언

```javascript
var a = "0";
var b = [];
var c = {};

var d = "",
var e = 0;
var f = null;
var g

Boolean(a);// true
Boolean(b);// true
Boolean(c);// true

Boolean(d);// false
Boolean(e);// false
Boolean(f);// false
Boolean(g);// false
```



\+ 단항 연산자가 값을 강제변환하는 것처럼 ! 부정 단항 연산자도 값을 불리언으로 명시적으로 강제변환한다. 문제는 그 과정에서 truty, falsy까지 뒤 바뀐다는 점

그래서 일반적으로 자바스크립트 개발 시 불리언값으로 명시적인 강제변환을 할 땐 !!이중부정 연산자를 사용. 두 번째 ! 이 패리티를 다시 원상 복구 

```javascript
var a = "0";
var b = [];
var c = {};

var d = "",
var e = 0;
var f = null;
var g

!!a;// true
!!b;// true
!!c;// true

!!d;// false
!!e;// false
!!f;// false
!!g;// false
```





### 암시적 변환

부수 효과가 명확하지 않게 숨겨진 형태로 일어나는 타입변환

그러나 명시적 변환과 더불어 암시적 변환도 알아야 한당.



### 암시적 강제 변환 : 불리언 -> 숫자

```javascript
function onlyOne(){
    var sum = 0;
    for(var i = 0l i < argu.length; i++){
        //falsy값은 건너 뛴다.
        //0으로 취급하는 셈이다. 그러나 NaN은  피해야 한다.
        if(argu[i]){
            sum += argu[i];
        }
    }
    return sum == 1;
}
```

```javascript
function onlyOne(){
    var sum = 0;
    for(var i=0; i < argument.length; i++){
        sum += Number(!!arguments[i]);
    }
    return sum === 1;
    
}
```

### 암시적 강제변환 : -> 불리언

1. if () 문의 조건식
2. for ( ; ;) 에서 두 번째 조건 표현식
3. while () 및 do.. while() 루프의 조건 표현식
4. ? : 삼항 연산시 첫 번째 조건 표현식
5. || 및 && 의 좌측 피연산자



### &&와 || 연산자

```javascript
a || b;
// 대략 다음과 같다.
a ? a : b;

a && b;
a? b : a
```

```
function foo(a,b){
    a = a || "hello";
    b = b || "world";
    console.log(a + " " + b);
}

foo(); //Hello World
```





### 느슨한/엄격한 동등 비교

== 느슨함

=== 엄격함

"동등함의 비교 시 ==는 강제변환을 허용하지만, ===는 강제변환을 허용하지 않는다."

#### 비교 성능

타입이 다른 두 값의 동등 비교에서 성능은 중요한 포인트가 아니다. **다만, 비교 과정에서 강제변환의 개입 여부.**

강제변환이 필요하다면 느슨한 동등 연산자(==)를, 필요하지 않다면 엄격한 동등 연산자(===)를 사용하자.



### * -> 불리언

```
var a = "42";

//나빠(실패헌다!):
if( a== true){
    // ...
}

//이것도 나쁨(실패)
if(a === true){
    //...
}

//그럴듯하군(암시적으로 동작)
if(a){
    //...
}

//휠씬 좋아 (명시적으로 작동)
if(!!a){
    //...
}

if(Boolean(a)){
    //...
}

```



### null -> undefined

nul과 undefined를 느슨한 동등 비교하면 서로에게 타입을 맞춘다.

죽, Null과 undefined는 느슨한 동등 비교 시 상호 간의 임시적인 강제변환이 일어나므로 비교 관점에서 구분이 되지 않는 값으로 취급되는 것이다.

```javascript
var a = null;
var b;

a== b //true
a == null; // true
b == null; // true

```

**'null <> undefined' 강제변환은 안전하고 예측 가능하며, 어떤 다른 값도 비교 결과 긍정 오류을 할 가능성이 없다. null과 undefined을 구분되지 않는 값들로, 결국 동일한 값으로 취급하는 강제변환은 권장하고 싶다.**



`if(a === undefined || a === null) 이나 if( a== null) 같은 것!!!`



### 말도 안되는... 불편한 진실.

```javascript
[] == ![] //true
2 == [2] //true
"" == [null] // true
0 == "\n" //true

"0" == false; // true
false == 0; // true
false == ""; //true
false == []; //true
"" == 0; //true
"" == []; //true
0 == []; //true
```

