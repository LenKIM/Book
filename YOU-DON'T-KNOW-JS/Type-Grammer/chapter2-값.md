# 2.값



`자바스크립트에 내장된 값 타입과 작동방식을 살펴보고 정확하게 사용할 수 있도록 완전히 이해하자.`



### 배열

```javascript
var a = [1, "2", [3]]

a.length; //3
a[0] === 1; //true
a[2][0] === 3; //true
```



**주의** 구멍난 배열!

```javascript
var a = [];
a[0] = 1'
// 'a[1]' 슬롯을 건너 띄었다.
a[2] = [3];
a[1]; //undefined
a.length; //3	
```

배열 인덱스는 숫자인데, 배열 자체도 하나의 객체여서 키/프로퍼티 문자열을 추가할 수 있다. (하지만 배열 length가 증가하지는 않는다)는 점이 다소 까다롭다.

```javascript
var a = [ ];
a[0] = 1;
a["foobar"] = 2;

a.length; //1
a["foobar"]; //2
a.footbar; // 2
```



### 유사 배열

`indexOf() / concat() / forEach() ` 사용하여 유사 배열을 일반 배열로

```javascript
function foo(){
    var arr = Array.prototype.slice.call(arguments);
    arr.push("bam");
    console.log(arr);
}

foo("bar", "baz"); //
```

ES6에서는 `Array.from()` 활용



### 문자열

자바스크립트 문자열은 실제로 생김새만 비슷할 뿐 문자 배열과 같지 않다.

```javascript
var a = "foo"
var b = ["f", "o", "o"];
```

```javascript
a.length; // 3
b.length; // 3

a.indexOf("o"); // 1
b.indexOf("o"); // 1

var c = a.concat("bar"); // "foobar"
var d = b.concat(["b","a","r"]); // ["f", "o", "o", "b","a","r"];

a === c; //false
b === d; //false
a; // foo
b // f o o 


차이점
a[1] = "0";
b[1] = "0";

a; // "foo"
bl // ["f","o","o"]
```

문자열은 불변 값이지만 배열은 가볍값

한가지 더, 문자열은 불변 값이므로 문자열 메서드는 그 내용을 바로 변경하지 않고 항상 해로운 문자열을 생성한 후 반환한다. 반면에 대부분의 배열 메서드는 그 자리에서 곧바로 원소를 수정한다,.

```javascript
c = a.toUpperCase();
a === c; // false
a; // "foo"
c; // "FOO"

b.push("!");
b; // ["f","O","o", "!"]

a.join; // undefined
a.map; //undefined

var c = Array.prototype.join.call(a, "-");
var d = Array.prototype.map.call(a, function(v){
    return v.toUpperCase() + ".";
}).join("");

c; // "f-o-o"
d; //"F.O.O"


var c = a
.split("")
.reverse()
.join("");
c; //"oof"

```



### 숫자

자바스크립트의 숫자 타입은 number가 유일하며 '정수 Integer', 부동 소수점 숫자를 모두 아우른다.

```javascript
var a = 42.59

a.toFixed(0); // "43"
a.toFixed(1); // "42.6"
a.toFixed(2); // "42.59"
a.toFixed(3); // "42.590"
a.toFixed(4); // "42.5900"
```

```javascript
var a = 42.59

a.toPrecision(0); // "4e+1"
a.toPrecision(1); // "43"
a.toPrecision(2); // "42.6"
a.toPrecision(3); // "42.59"
a.toPrecision(5); // "42.590"
a.toPrecision(6); // "42.5900"
a.toPrecision(7); // "42.59000"
```



### 작은 소수 값

```javascript
0.1 + 0.2 === 0.3; // false 
```

다른 언어는 모두 IEEE 754표준을 준수하지만, 많은 이들의 예상과는 달리 자바스크립트는 그렇지 않다.

수식만 보면 true이지만, false이다.

간단히 말해, 이진 부동 소수점으로 나타낸 0.1과 0.2는 원래의 숫자와 일치하지 않는다.

정확히는 0.3000000000000004에 가깝지만, 같은 것은 아니다.



**그럼 어떻게?**

가장 일반적으로는 미세한  '반올림 오차'를 허용 공차(Tolerance)로 처리하는 방법이 있다.

**머신 입실론**이라고 하는데, 자바스크립트 숫자의 머신 입실론은 2^-52 이다.

ES6부터는 이 값이 Number.EPSILON으로 미리 정의되어 있으므로 필요시 사용하면 되고, ES6이전 브라우저는 다음과 같이 폴리필을 대신 사용한다.

```javascript
if(!Number.EPSILON){
    Number.EPSILON = Math.pow(2, -52);
}
```

Number.EPSILON으로 두 숫자의 (반올림 허용 오차 이내의) '동등함'을 비교할 수 있다.

```javascript
function numbersCloseEnoughToEqual(n1, n2){
    return Math.abs(n1 - n2) < Number.EPSILON;
}

var a = 0.1 + 0.2;
var b = 0.3;

numbersCloseEnoughToEqual(a,b); // true
numbersCloseEnoughToEqual(0.00000001,0.00000002); // false
```

부동 소수점숫자의 최댓값은 대략 1.798e+ 308이고 Number.MAX_VALUE로 정의하며, 최솟값은 5e-324로 음수는 아니지만 거의 0에 가까운 숫자고 Number.MIN_VALUE 로 정의한다.



### 안전한 정수 범위

숫자를 표현하는 방식이 이렇다 보니, 정수는 Number.MAX_VALUE 보다 휠씬 작은 수준의 안전 값의 범위가 정해져 있다.

`Number.MAX_SAFE_INTEGER` 최솟값은 `Number.MIN_SAFE_INTEGER = -9007199254740991`

When? 데이터베이스에서 64비트 ID를 처리할 때가 대부분



### 정수인지 확인

```
Number.isInteger(42);
Number.isInteger(42.000);
Number.isInteger(42.3);

Poly.fill
if(!Number.isInteger){
    Number.isInteger = function(num){
        return typeof num == "number" && num % 1 == 0;
    };
}
```

### 32비트(부호있는) 정수

`a | 0` 과 같이 쓰면 '숫자 값 -> 32비트 부호 있는 정수 로 강제변환.

 즉, 정수의 안전 범위가 대락 53비트에 이르지만, 32비트 숫자에만 가능한 연산을 만들기 위해 `a | 0' 와 같은 방법을 쓴다고 합니다!



### 특수 값

타입별로 자바스크립트 개발자들이 조심해서 사용해야 할 특수한 값



#### 값 아닌 값

Undefined 타입의 값은 undefined밖에 없다. null 타입도 값은 null뿐이다. 그래서 이 둘은 타임과 값이 항상 같다.

Undefined와 null의 의미를 어떻게 '정의'하여 쓰든지, null은 식별자가 아닌 특별한 키워드이므로 null이라는 변수에 뭔가 할당할 수는 없다. 



#### void 연산자

```javascript
function doSomething(){
    //참고: 'APP.ready'는 이 어플에서 제공한 값
    if(!APP.ready){
        //나중에 다시 해보자!
        return void setTimeout(doSomething, 100);
    }
    var result;
    return result;
}

if(doSomething()){
    //다음 작업 바로 실행
}
```

setTimeout()함수는 숫자 값을 반환하는데, void를 쓰면 if문에서 긍정 오류가 일어나지 않게 할 수 있다.

##### void 연산자는 (어떤 표현식으로부터) 값이 존재하는 곳에서 그 값이 undefined가 되어야 좋을 경우에만 사용하자!!!!

#### 특수 문자

`NaN`

```javascript
var a = 2 /"foo"; //NaN
typeof a === "number"; //true
```

**NaN은 글자 그대로 '숫자 아님' 보다는 '유효하지 않는 숫자' / '실패한 숫자' / '몹쓸 숫자'**

```javascript
var a = 2
undefined
typeof a === "number"
true
var a = NaN
undefined
typeof a === "number"
true
typeof a === NaN
false
```

정확하게 NaN을 판별하려면 isNaN() 함수를 활용!

ES6 부터는 Number.isNaN() 등장!

```javascript
if(!Number.isNaN){
    Number.isNaN = function(n){
        return (
        typeof n === "number" &&
        window.isNaN(n)
        );
    };
}

var a = 2 
var b = 'foo'
Number.isNaN(a); // true
Number.isNaN(b); // false
```

### 무한대

```javascript
var a = 1 / 0;
```

```javascript
var a = 1 / 0; //Infinity
var a = -1 / 0; // -Infinity
```

+Infinity 와  -Infinity가 존재함.

### 영(0)

```javascript
var a = 0 / -3; // -0
var b = 0 * -3; // -0
```

왜 -0 이 존재?

값의 크기로 어떤 정보와 그 값의 부호로 또 다른 정보를 동시에 나타내야 하는 어플이 존재하기 때문에????

+0, -0 개념이 없다면 어떤 변숫값이 0에 도달하여 부호가 바뀌는 순간, 그 직전까지 이 변수의 이동방향은 무엇인지 알 수가 없으므로 부호가 다른 두 0은 유용하다. 즉, 잠재적인 정보 소실을 방지하기 위해 0의 부호를 보존한 셈.

```javascript
function isNegZero(n){
    n = Number(n);
    return (n === 0)&& (1 / n === -Infinity);
}

isNegZero(-0); //true
isNegZero(0 / -3); //true
isNegZero(0); //false
```



### 값 vs 레퍼런스

자바스크립트는 포인터라는 개념 자체가 없고 참조하는 방법도 조금 다르다. 우선 어떤 변수가 다른 변수를 참조할 수 없다. 그냥 안된다.

더구나 자바스크립트에는 값 또는 레퍼런스의 할당 및 전달을 제어하는 구문 암시(Syntactic Hint)가 전혀 없다. 대신, 값의 타입만으로 값-복사, 레퍼런스-복사 둘 중 한쪽이 결정된다.

```javascript
var a = 2;
var b = a;
b++;
a; //2
b; //3

var c = [1,2,3];
var d = c;
d.push(4);
c; // [1,2,3,4]
d; // [1,2,3,4]
```

null, undefined, string, number, boolean 그리고 ES6의 symbol 같은 단순 값(스칼라 원시 값)은 언제나 값-복사 방식으로 할당/전달된다.

객체나 함수 등 합성 값은 할당/전달시 반드시 레퍼런스 사본을 생성한다.

### 정리

자바스크립트 배열은 모든 타입의 값들을 숫자로 인덱싱한 집합이다. 문자열은 일종의 '유사배열'이지만, 나름 특성이 있기 떄문에 배열로 다루고자 할 때에는 조심하는 것이 좋다. 자바스크립트 숫자는 '정수'와  '부동 소수점 숫자'모두 포함한다.

원시타입에는 몃몃 특수 값이 있다.

null 타입은 null이란 값 하나뿐이고,마찬가지로 undefined타입도 값은 undefined 분이다.

undefined는 할당 된 값이 없다면 모든 변수/프로퍼티의 디폴트 값이다. void 연산자는 어떤 값이라도 undefined로 만들어 버린다.

숫자에는 naN, +Infinity, -Infinity, -0 와 같은 특수 값이 있다.