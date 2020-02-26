# 문법



## 문(Statement)과 표현식(Expression)

**문장(Sentance)은 생각을 표현하는 단어들의 완전한 조형물. 문장은 하나 이상의  어구(Pharse)로 구성되며, 각 어구는 구두점이나 접속사로 연결할 수 있고 어구는 더 작은 어구로 나눌 수있다. 어떤 어구는 불완전하여 그 자체로 완성된 문장을 형성할 수 없지만 스스로의 힘만으로 완성되는 어구도 있다.**

*문은 문장*

*표현식은 어구*

*연산자는 구두점/접속사에 해당된다.*



### 문의 완료값

```javascript
var b;

if(true){
    b = 4 + 38;
}
```

결과는 42



그러나

```javascript
var a, b;

a = if(true){
    b = 4 + 38;
}
```

안됨.


왜냐하면 문의 완료값이 존재하기 때문에!



### 표현식의 부수 효과

```javascript
var a = 42;
var b = a++;
```

a = 43 // b = 42

**a++에는 부수효과가 존재한다!**



**해결**은 문을 나열하는 (Statement-Series) 콤마 연산자, 를 사용하면 다수의 개별 표현식을 하나의 문으로 연결

```javascript
var a = 42, b;
b = (a++, a);
```



### 콘테스트 규칙

자바스크립트 문법 규칙 중에서 같은 구문이지만 어디에서 어떤 식으로 사용하느냐에 따라 서로 다른 의미를 가지는 경우가 있다.

#### 중괄호

 { }

1. 객체리터럴

```javascript
var a = {
    foo: bar()
};
```



2. 레이블

```javascript
{
    foo:bar()
}
```



3. 블록

```javascript
[] + {}; // "[Object object]"
{} + []; // 0
```

윗 줄에서 엔진은 + 연산자 표현식의 {}를 실제 값으로 해석한다.

[]는 " "로 강제변환되고 {}도 문자열 "[Object Object]"로 강제 변환

아랫 줄도 마찬가지로.



4. 객체 분해 ( 디스트럭쳐링 ! )
5. else if와 선택적 블록

```
if(a){
    //...
} else if(b){
    //...
} else if(c){
    //....
}
```

원랜 else if같은 건없다.

```javascript
if(a){
    //...
} else {
	if(b){
    //...
} else {
    if(c){
    //....
}
```

이렇게 됨!!!



### 연산자 우선순위

```javascript
var a = 42;
vat b = "foo";

a && b; // "foo";
a || b; // 42
```



하지만 연산자가 2개, 피연산자가 3개일 경우는?

```javascript
var a = 42;
var b = "foo";
var c = [1,2,3];

a && b || c; //foo
a || b && c; //42
```



요점은 &&가 || 보다 연산우선순위가 더 높다.



### 단락 평가

&&,|| 연산자는 좌측 피연산자의 평가 결과만으로 전체 결과가 이미 결정될 경우 우측 피연산자의 평가를 건너뛴다. 그래서 단락이란 말이 유래된 것이다. 

예를 들어 a && b에서 a가 falsy면 b는 쳐다보지도 않는다. &&연산 결과가 이미 false로 굳어진 마당에 애써 b를 조사할 필요가 없다. 마찬가지로 a || b에서 a가 truthy면, 이미 전체 결과값은 true로 확정되므로 b는 관심을 둘 이유가 없다.

```javascript
function doSomething(opts){
    if(opts && opts.cool){
        // ...
    }
}
```



### 세미콜론 자동 삽입(ASI)



### SWITCH

switch문에서 알아야할 점이 하나 있다!

```javascript
switch (a){
    case 2:
	    break
    case 2:
	    break
        default:
        
}
```

여기서 내가 모르는 부분이 뭐냐면

바로 switch 표현식과 case 표현식 간의 매치 과정은 === 알고리즘과 똑같다.

그러나 강제변환이 일어나는 동등 비교를 이용하고 싶다면 switch문에 꼼수를 좀 부려야 한다.

```javascript
switch (a){
    case a == 2:
	    break
    case a == 2:
	    break
        default:       
}
```

