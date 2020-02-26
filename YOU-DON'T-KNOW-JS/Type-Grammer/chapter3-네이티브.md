#네이티브


- String()
- Number()
- Boolean()
- Array()
- Object()
- Function()
- RegExp()
- Date()
- Error()
- Symbol()



대표적인 자료형

하지만 자바와 달리 String()이 String이 아니고 Object 이다.



### 내부 [[ Class ]] 

typeof가 'object'인 값(배열 등)에는 [[class]]라는 내부 프로퍼티가 추가로 붙는다. 이 프로퍼티는 직접 접근할 수 없고 Object.prototype.toString()라는 메서드에 값을 넣어 호출합니다.



### 래퍼 박싱

```javascript
var a = "abc";
a.length; // 3
a.toUpperCase(); // "ABC"
```



#### 객체 래퍼의 함정

```javascript
var a = new Boolean(false);

if(!a){
    console.log("Oops"); // 실행되지 않는다.
}
```

false를 객체 래퍼로 감쌌지만 문제는 객체가 'truthy'란 점. 그래서 예상과는 달리, 안에 들어있는 false 값과 반대의 결과.



#### 결과 => 객체 래퍼로 직접 박싱하는 건 권하고 싶지 않다.



### 언박싱

객체 래퍼의 원시 값은 valueOf()메서드로 추출

```javascript
var a = new String("abc");
var b = new Number(42);
var c = new Boolean(true);

a.valueOf(); 
b.valueOf();
c.valueOf();
```

암시적인 언박싱이 일어남.



### 네이티브, 나는 생성자.

```javascript
var a = new Array(1,2,3);
a; // [1, 2, 3]

var b = [1, 2, 3];
b; // [1, 2, 3]
```

```javascript
var a = new Array(3);
var b = [undefined, undefined, undefined ];
var c = [];
c.length = 3;
3
a;
(3) [empty × 3]
b;
(3) [undefined, undefined, undefined]
c;
(3) [empty × 3]
```

```javascript
a.join("-");
"--"
a.map(function(v,i){return i;});
(3) [empty × 3]
b.map(function(v,i){return i;});
(3) [0, 1, 2]
```



### Object(), Function() and RegExp()

```javascript
var c = new Object();
undefined
c.foo = "bar"
"bar"
c;
{foo: "bar"}
var d = {foo: "bar"};
undefined
d;
{foo: "bar"}
var e = new Function("a", "return a * 2;");
undefined
var f = function(a) {return a * 2;}
undefined
function g(a) {return a * 2;}
undefined
var h = new RegExp("^a*b+", "g");
undefined
var i = /^a*b+/g;
undefined
```



### Symbol()

ES6에 처음 나온 시 값.

```javascript
var mysym = Symbol("my own symbol");
undefined
mysym
Symbol(my own symbol)
mysym.toString();
"Symbol(my own symbol)"
typeof mysym;
"symbol"
var a = { };
undefined
a[mysym] = "foobar";
"foobar"
Object.getOwnPropertySymbols(a);
[Symbol(my own symbol)]
```



### 네이티브 프로토타입

내장 네이티브 생성자는 각자의 .prototype객체를 가진다.

```javascript
var a = "abc";
a.indexOf("c");
a.toUpperCase();
a.trim();
```

```javascript
typeof Function.prototype;
Function.prototype();

RegExp.prototype.toString(); // 빈 regex
"abc".match(RegExp.prototype); // [""]
```



### Wrap up

자바스크립트는 원시 값을 감싸는 객체 래퍼, 즉 네이티브(String, Number, Boolean) 제공

 객체 래퍼는 타입별로 쓸만한 기능이 구현되어 있어 편리하게 사용할 수 있다.

"abc" 같은 단순 스칼라 원시 값이 있을 때, 이 값의 length 프로퍼티나 String.prototype에 정의된 메서드를 호출하면 자바스크릅티는 자동으로 원시값을 '박싱'하여 필요한 프로퍼티와 메서드를 쓸 수 있게 도와준다.