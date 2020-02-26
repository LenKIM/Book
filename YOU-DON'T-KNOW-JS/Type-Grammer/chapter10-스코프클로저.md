# 스코프 클로저

### 깨달음

클로저? 클로저는 자바스크립트의 모든 곳에 존재한다. 그저 인식하고 받아들이면됨.

클로저는 렉시컬 스코프에 의존해 코드를 작성한 결과로 그냥 발생한다. 이용하려고 굳이 의도적으로 클로저를 생성할 필요가 없다. 모든 코드에서 클로저는 생성되고 사용된다. 그러므로 여기서 적절히 클로저의 전반을 파악하면 클로저를 목적에 따라 확인하고, 받아들이고, 이용할 수 있다.



### 핵심

클로저는 함수가 속한 렉시컬 스코프를 기억하여 함수가 렉시컬 스코프 밖에서 실행될 때에도 이 스코프에 접근할 수 있게 하는 기능을 뜻한다.

크도를 보면서 앞의 정의가 설명한 바를 살펴보자.

```javascript
function foo(){
    var a = 2;
    function bar(){
        console.log(a)
    }
    bar();
}

foo();
```

bar()는 foo() 스코프에 대한 클로저를 가진다. 달리말하면 bar( )는 foo( ) 스코프에 닫힌다.

이런 방식으로 정의된 클로저는 바로 알아보기 힘들고 앞의 코드에서 클로저가 작동하는 방식을 볼 수도 없다. 렉시컬 스코프는 분명하게 볼 수 있지만, 클로저는 여전히 코드 뒤에 숨겨진 불가사의한 음모의 그림자로 남아있다.

```javascript
function foo(){
    var a = 2;
    function bar(){
        console.log(a);
    }
    return bar;
}
var baz = foo();
baz(); // 2 
```

함수 bar( )는 foo () 의 렉시컬 스코프에 접근할 수 있고, bar(  ) 함수 자체를 값으로 넘긴다. 이 코드는 bar를 참조하는 함수 객체 자체를 반환한다.

**foo( )를 실행하여 반환한 값 (bar () 함수)을 baz라 불리는 변수에 대입하고 실제로는 baz( )함수를 호출했다. 이는 당연하게도 그저 다른 확인자 참조로 내부 함수인 bar( )를 호출한 것이었다. bar ( )는 의심할 여지없이 실행됐다. 그러나 이 경우에 함수 bar는 함수가 선언된 렉시컬 스코프 밖에서 실행됐다.**

일반적으로 foo( )가 실행된 후에는 foo ( )의 내부 스코프가 사라졌다고 생각할 것이다. 이것은 엔진이 가비지 콜렉터를 고용해 더는 사용하지 않는 메모리를 해제시킨다는 사실을 알기 때문이다. 더는 foo ( )의 내용을 사용하지 않는 상황이라면 사라졌다고 보는 게 자연스럽다.

그러나 클로저의 '마법'이 이를 내버려두지 않는다. 사실 foo의 내부 스코프는 여전히 '사용 중'이므로 해제되지 않는다. 그럼 누가 그 스코프를 사용 중인가? 바로 bar( )자신이다. 선언된 위치 덕에 bar( )는 foo( )스코프에 대한 렉시컬 스코프 클로저를 가지고, foo( )는 bar( )가 나중에 참조할 수 있도록 스코프를 살려둔다. 즉, bar( )는 여전히 해당 스코프에 대한 참조를 가지는데, 그 참조를 바로 클로저라고 부른다.

foo( )선언이 끝나고 수 밀리 초 후 변수 baz를 호출(bar라 명명했던 내부 함수를 호출)할 때, 해당 함수는 원래 코드의 렉시컬 스코프에 접근할 수 있고 예상한 것처럼 이는 함수가 변수 a에 접근할 수 있다는 의미다.

함수는 원래 코드의 렉시컬 스코프에서 완전히 벗어나 호출됐다. 클로저는 호출된 함수가 원래 선언된 렉시컬 스코프에 계속해서 접근할 수 있도록 허용한다. 물론, 어떤 방식이든 함수를 값으로 넘겨 다른 위치에서 호출하는 행위는 모두 클로저가 작용한 예이다.

```javascript
function foo(){
    var a =2;
    function baz(){
        console.log(a);
    }
    bar(baz);
}

function bar(fn){
    fn();
}
```

코드에서 함수 baz를 bar에 넘기고, 이제 fn이라 명명된 함수를 호출했다. 이때 foo()의 내부 스코프에 대한 fn의 클로저는 변수 a에 접근할 때 확인할 수 있다. 이런 함수 넘기기는 간접적인 방식으로도 가능하다.

```javascript
var fn;

function foo(){
    var a =2;
    function baz(){
        console.log(a);
    }
    bar(baz);
}

function bar(fn){
    fn();
}

foo();
bar(); // 2
```

어떤 방식으로 내부 함수를 자신이 속한 렉시컬 스코프 밖으로 수송하든 함수는 처음 선언된 곳의 스코프에 대한 참조를 유지한다. 즉, 어디에서 해당 함수를 실행하든 클로저가 작용한다.



### 이제 나는 볼 수 있다.

```javascript
function wait(message){
    setTimeout(function timer(){
        console.log(message);
    }, 1000);
}
wait("Hello, closure!");
```

내부 함수 timer를 setTimeout()에 인자를 넘겼다. timer함수는 wait()함수의 스코프에 대한 스코프 클로저를 가지고 있으므로 변수 message에 대한 참조를 유지하고 사용할 수 있다.

wait( )실행 1초 후, wait의 내부 스코프는 사라져야 하지만 익명의 함수가 여전히 해당 스코프에 대한 클로저를 가지고 있다.

엔진 내부 깊숙한 곳의 내장 함수 setTimeout()에도 아마도 fn이나 func 정도로 불릴 인자의 참조가 존재한다. 엔진은 해당 함수 참조를 호출하여 내장 함수 timer를 호출하므로 timer의 렉시컬 스코프는 여전히 온전하게 남아 있다.



### 반복문과 클로저

```javascript
for( var i = 1; i<= 5; i++){
    setTimeout(function timer() {
        console.log(i);
    }, i * 1000);
}
```

이 코드의 목적은 예상대로, 1, 2 ... 5까지 한 번에 하나식 일 초마다 출력하는 것이다. 그러나 실제로 코드를 돌려보면, 일 초마다 한 번 씩 '6만'5번 출력된다.

왜?

먼저 6이 어떻게 나오는지 알아보자. 반복문이 끝나는 조건은 i가 '<=5'가 아닐 때다. 처음으로 끝나는 조건이 갖춰졌을 때 i의 값은 6이다. 즉, 출력된 값은 반복문이 끝나을 때의 i값을 반영한 것이다. 코드를 다시 보면 이 설명이 당연하게 느껴질 것이다.

timeout 함수 콜백은 반복문이 끝나고 나서야 작동한다. 사실 타이머를 차지하고 반복마다 실행된 것이 setTimeout(...,0)이었다 해도 해당 함수 콜백은 확실히 반복문이 끝나고 나면 동작해서 결과는 매번 6을 출력한다.

여기서 더 심오한 문제가 제기된다. 애초에 문법적으로 기대한 것과 같이 이 코드를 작동시키려면 무엇이 더 필요한가?

그러기 위해 필요한 것은 반복마다 각각의 i복제본을 '잡아'두는 것이다. 그러나 반복문 안 총 5개의 함수들은 반복마다 따로 정의됐음에도 모두 같이 글로벌 스코프 클로저를 공유해 해당 스코프 안에는 오직 하나의 i만이 존재한다. 따라서 모든 함수는 당연하게도 같은 i에 대한 참조를 공유한다. 그냥 5개의 timeout콜백을 쭉 이어서 반복문 없이 선언해도 결과는 똑같다.

자, 이제 다시 질문으로 돌아가보자. 무엇이 더 필요한가? 필요한 것은 더 많은 닫힌(Closured) 스코프다. 구체적으로 말하면 반복마다 하나의 새로운 닫힌 스코프가 필요하다.



다음 예제를 보면

```javascript
for(var i = 1; i<=5; i++){
    (function(){
        setTimeout( function timer(){
            console.log(i);
        }, i * 1000);
    })();
}
```

여전히 6을 6번 호출한다. 왜 그럴까? 분명 많은 렉시컬 스코프를 가지는데 말이다 .각각의 timeout함수 콜백은 확실히 반복마다 각각의 IIFE가 생성한 자신만의 스코프를 가진다. 그러나 따힌 스코프만으로는 부족하다. 이 스코프가 비어있기 떄문이다.

```javascript
for(var i = 1; i<=5; i++){
    (function(){
    var j = i;
        setTimeout( function timer(){
            console.log(j);
        }, j * 1000);
    })(i);
}
```

이런 방식도 존재한다.

```javascript
for(var i = 1; i<=5; i++){
    (function(j){
        setTimeout( function timer(){
            console.log(j);
        }, j * 1000);
    })(i);
}
```

### 다시 보는 블록 스코프

### 모듈

```javascript
function foo(){
    var something = "cool"
    var another = [1,2,3];
    
    function doSomething(){
        console.log(something);
    }
    
    function doAnother(){
        console.log(another.join(" ! "));
    }
}
```

이 코드에는 클로저의 흔적이 보이지 않는다. 우리가 볼 수 있는 것은 몇 가지 비공개 데이터 변수인 something과 another 그리고  내부함수 doSomething()과 doAnother()가 있다. 이들 모두 foo( )의 내부 스코프를 렉시컬 스코프로 가진다.

```javascript
function CoolModule(){
    var something = "cool"
    var another = [1,2,3];
    
    function doSomething(){
        console.log(something);
    }
    
    function doAnother(){
        console.log(another.join(" ! "));
    }
    return {
        doSomething: doSomething,
        doAnother: doAnother
    };
}

var foo = CoolModule();
foo.doSomething(); // cool
foo.doAnother(); // 1 ! 2 ! 3
```

이 코드와 같은 자바스크릅티 패턴을 모듈이라고 부른다. 가장 흔한 모듈 패턴 구현 방법은 모듈 노출이고, 앞의 코드는 이것의 변형



첫째, CoolModule()은 그저 하나의 함수일 뿐이지만, 모듈 인스턴스를 생성하려면 반드시 호출해야 한다. 최외곽 함수가 실행되지 않으면 내부 스코프와 클로저는 생성되지 않는다.

둘째, CoolModule()함수는 객체를 반환한다. 반환되는 객체는 객체-리터럴 문법 {key: value, ...}에 따라 표기된다. 해당 객체는 내장 함수들에 대한 참조를 가지지만, 내장 데이터에 대한 참조는 가지지 않는다. 내장 데이터 변수는 비공개로 숨겨져 있다. 이 객체의 반환값은 본질적으로 모듈의 공개 API라고 생각할 수 있다.

객체의 반환 값은 최종적으로 외부 변수 foo에 대입되고, foo.dooSomething()과 같은 방식으로 API의 속성 메서드에 접근할 수 있다.

함수 doSomething()과 doAnother()는 모듈 인스턴스의 내부 스코프에 포함하는 클로저를 가진다. 반환된 객체에 대한 속성 참조 방식으로 이 함수들을 해당 렉시컬 스코프 밖으로 옮길 때 클로저를 확인하고 이용할 수 있는 조건을 하나 세웠다.

쉽게 말해, 이 모듈 패턴을 사용하려면 두 가지 조건.

1. 하나의 최외곽 함수가 존재하고, 이 함수가 최소 한 번은 호출되어야 한다.
2. 최외곽 함수는 최소 한 번은 하나의 내부 함수를 반환해야 한다. 그래야 해당 내부 함수가 비공개 스코프에 대한 클로저를 가져 비공개 상태에 접근하고 수정할 수 있다.

하나의 함수 속성만을 가지는 객체는 진정한 모듈이 아니다. 함수 실행 결과로 반환된 객체에 데이터 속성들은 있지만 닫힌 함수가 없다면, 당연히 그 객체는 진정한 모듈이 아니다.

앞의 코드는 독립된 모듈 생성자(CoolModule())을 가지고, 생성자는 몇번이고 호출할 수 있고 호출할 때마다 새로운 모듈 인스턴스를 생성한다. 이 패턴에서 약간 변경된 오직 하나의 인스턴스, 싱글톤만 생성하는 모듈을 살펴보자.

```javascript
var foo = (function CoolModule(){
    var something = "cool";
    var another = [1,2,3];
    function doSomething(){
        console.log(something);
    }
    function doAnother(){
        console.log(another.join("!"));
    }
    return {
        doSomething: doSomething,
        doAnother: doAnother
    };
})();

foo.doSomething();
foo.doAnother();
```

모듈은 함수이므로 다음 코드처럼 인자 받을 수 있다.

```javascript
function CoolModule(id){
    function identify(){
        console.log(id);
    }
    
    return {
        identify:identify
    };
}

var foo1 = CoolModule("foo 1");
var foo2 = CoolModule("foo 2");

foo1.identify(); // 'foo 1'
foo2.identify(); // 'foo 2'
```



```javascript
var foo = (function CoolModule(id){
    function change(){
        publicAPI.identify = identify2;
    }
    function identify1(){
        consolelog(id);
    }
     function identify2(){
        consolelog(id.toUpperCase());
    }
    
    var publicAPI;
    
})("foo module");

foo.identify(); // foo module
foo.change();
foo.identify(); // FOO MODULE
```

안에서 수정가능



### 현재의 모듈

```javascript
var MyModules = (function Manager(){
    var modules = {};
    
    function define(name, deps, imple){
        for(var i = 0l i < deps.length; i++){
            deps[i] = modules[deps[i]];
        }
        modules[name] = imple.apply(impl, deps);
    ) 
    function get(name){
        return modules[name];
    }
    return {
        define: define,
        get: get
    }
})();
```

"modules[name] = impl.apply(impl, deps)"다. 이 부분은 모듈에 대한 정의 래퍼 함수를 호출하여 반환 값인 모듈 API를 이름으로 정리된 내부 모듈 리스트에 저장한다.

해당 부분(module[name] = imple.apply(imple, deps))을 이용해 모듈을 정의하는 다음 코드를 보자.

```javascript
MyModules.define("bar", [], function(){
    function hello(who){
        return "Let me introduce: " + who;
    }
    return {
        hello: hello
    };
});

MyModueles.define("foo", ["bar"], function(bar){
    var hungry = "hippo";
    function awesome(){
        console.log(bar.hello(hungry).toUpperCase());
    }
    return {
        awesome: awesome
    };
});

var bar = MyModules.get("bar");
var foo = MyModules.get("foo");

console.log(
	bar.hello("hippo")
); // Let me introduce: hippo

foo.awesome(); // LET ME INTRODUCE: HIPPO
```

천천히 코드를 살펴보면 목적에 따라 사용된 클로저의 힘을 완전히 이해할 수 있다. 모듈 관리자를 만드는 특별한 마법이란 존재하지 않는다는 것을 기억해야 한다. 모든 모듈 관리자는 앞에서 언급한 모듈패턴의 특성을 모두 가진다. 즉, 이들은 함수 정의 래퍼를 호출하여 해당 모듈의 API인 반환값을 저장한다. 좀 더 쓰기 편하게 포장한다고 해도 모듈은 그저 모듈일 뿐이다.

### 미래의 모듈

ES6

```javascript
// bar.js
function hello(who){
    return "Let me introduce: " + who;
 }
 export hello;
 
 //foo.js: import only 'hello()' from the "bar" module
import hello from 'bar'
var hungry = "hippo";
function awesome(){
    console.log(
    hello(hungry).toUpperCase()
    );
}
export awesome;

//baz.js : import the entire "foo" and "bar" modules
module foo from "foo";
module bar from "bar";

console.log(
bar.hello("rhino")
); // Let me introduce: rhino
foo.awesome();
```

### WRAP UP

편겨에 찬 이들은 클로저를 자바스크립ㅌ의 세계에서 홀로 떨어진, 가장 용감한 소수만이 닿을 수 있는 신비의 세계로 생각할 수 있다. 그러나 클로저는 사실 표준이고, 함수를 값으로 마음대로 넘길 수 있는 렉시컬 스코프 환경에서 코드를 작성하는 방법.

클로저는 함수를 렉시컬 스코프밖에서 호출해도 함수는 자신의 렉시컬 스코프를 기억하고 접근할 수 있는 특성을 의미한다.

