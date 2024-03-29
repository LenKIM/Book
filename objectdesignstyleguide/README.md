▣ 01장: 객체로 프로그래밍하기 - 기초  

- [x] 1.1 클래스와 객체
- [x] 1.2 상태  
- [x] 1.3 행위  
- [x] 1.4 의존성  
- [x] 1.5상속  
- [x] 1.6 다형성  
- [x] 1.7 구성  
- [x] 1.8 클래스 조직  
- [x] 1.9 반환문과 예외  
- [x] 1.10 단위 테스트  
- [x] 1.11 동적 배열  
요약  

▣ 02장: 서비스 생성하기  
- [x] 2.1 객체의 두 종류  
- [x] 2.2 의존성과 설정값은 생성자 인자로 주입한다  
- [x] 2.2.1 짝 지은 설정값은 함께 둔다  
- [x] 2.3 필요한 것의 위치가 아니라 필요한 것 자체를 주입한다  
- [x] 2.4 모든 생성자 인자는 필수여야 한다  
- [x] 2.5 생성자 주입만 사용한다  
- [x] 2.6 선택적인 의존성 같은 건 없다  
- [x] 2.7 모든 의존성을 명시한다  
- [x] 2.7.1 정적 의존성을 객체 의존성으로 바꾼다  
- [x] 2.7.2 복잡한 함수를 객체 의존성으로 바꾼다  
- [x] 2.7.3 시스템 호출을 명백히 한다  
- [x] 2.8 작업 관련 데이터는 생성자 인자가 아니라 메서드 인자로 전달한다  
- [x] 2.9 서비스의 인스턴스를 만든 후에는 행위를 바꾸지 못하게 한다  
- [x] 2.10 생성자에서는 속성에 할당하는 일만 한다  
- [x] 2.11 인자가 유효하지 않으면 예외를 일으킨다  
- [x] 2.12 서비스는 적은 수의 진입점이 있는 변경 가능 객체 그래프로 정의한다  
요약  
[연습 문제 해답]  

▣ 03장: 다른 객체 생성하기  
- [X] 3.1 일관성 있는 행위에 필요한 최소한의 데이터를 요구한다  
- [X] 3.2 의미 있는 데이터를 요구한다  
- [X] 3.3 유효하지 않은 인자에 대한 예외로 사용자 정의 예외 클래스를 사용하지 않는다  
- [X] 3.4 예외 메시지를 분석해 유효하지 않은 인자에 대한 특정 예외를 테스트한다  
- [X] 3.5 도메인 불변속성을 여러 곳에서 검증하지 않게 새 객체를 추출한다  
- [X] 3.6 복합 값은 새로운 객체로 추출해 나타낸다  
- [X] 3.7 단언으로 생성자 인자 유효성을 확인한다  
- [X] 3.8 의존성을 주입하지 말고 선택적인 메서드 인자로 전달한다.  
- [X] 3.9 명명한 생성자를 사용한다  
- [X] 3.9.1 기본 타입 값으로 생성하기  
- [X] 3.9.2 toString, toInt() 등을 즉시 추가하지 말자  
- [X] 3.9.3 도메인별 개념을 도입한다  
- [X] 3.9.4 선택적으로 비공개 생성자를 사용해 제약을 강제한다  
- [X] 3.10 속성 채움자를 사용하지 않는다  
- [X] 3.11 무엇이든 필요 이상으로 객체에 넣지 않는다  
- [X] 3.12 생성자는 테스트하지 않는다  
- [X] 3.13 예외 규칙: 데이터 전송 객체  
- [X] 3.13.1 공개 속성을 사용한다  
- [X] 3.13.2 예외를 일으키지 말고 유효성 오류를 수집한다  
- [X] 3.13.3 속성 채움자는 필요할 때 사용한다  
요약  
[연습 문제 해답]  

▣ 04장: 객체 다루기  
- [X] 4.1 개체: 변경을 추적하고 이벤트를 기록하는 식별 가능한 객체  
- [X] 4.2 값 객체: 교체할 수 있고 익명이며 변경 불가능한 값  
- [X] 4.3 데이터 전송 객체: 디자인 규칙이 적은 단순한 객체  
- [X] 4.4 변경 불가능 객체가 우선이다  
- [X] 4.4.1 값을 변경하는 대신 교체한다  
- [X] 4.5 변경 불가능 객체의 변경자는 변경한 복사본을 반환해야 한다  
- [X] 4.6 변경 가능 객체의 변경자 메서드는 명령 메서드여야 한다  
- [X] 4.7 변경 불가능 객체의 변경자 메서드 이름은 서술형이어야 한다  
- [X] 4.8 객체 전체를 비교한다  
- [X] 4.9 변경 불가능 객체를 비교할 때는 동일성이 아닌 상등을 확인한다  
- [X] 4.10 변경자 메서드를 호출한 결과는 항상 유효한 객체여야 한다  
- [X] 4.11 변경자 메서드는 상태 변경 요청이 유효한지 확인해야 한다  
- [X] 4.12 내부에 기록한 이벤트를 사용해 변경 가능 객체의 변경을 확인한다  
- [X] 4.13 변경 가능 객체에는 흐름식 인터페이스를 구현하지 않는다  
요약  
[연습 문제 해답]  

▣ 05장: 객체 사용하기  
- [X] 5.1 메서드를 구현하는 템플릿  
- [X] 5.1.1 사전 조건 확인  
- [X] 5.1.2 실패 시나리오  
- [X] 5.1.3 행복한 경로  
- [X] 5.1.4 사후 조건 확인  
- [X] 5.1.5 반환 값  
- [X] 5.2 예외의 몇 가지 규칙  
- [X] 5.2.1 사용자 정의 예외 클래스는 필요할 때만 사용한다  
- [X] 5.2.2 유효하지 않은 인자나 논리 예외 클래스 명명하기  
- [X] 5.2.3 실행 중 예외 클래스 명명하기  
- [X] 5.2.4 명명한 생성자를 사용해 실패 이유를 나타낸다  
- [X] 5.2.5 상세한 메시지를 추가한다  
요약  
[연습 문제 해답]  

▣ 06장: 정보 가져오기  
- [X] 6.1 질의 메서드를 사용해 정보를 가져온다  
- [X] 6.2 질의 메서드의 반환 값은 단일 타입이어야 한다  
- [X] 6.3 초기 상태를 노출하는 질의 메서드를 피한다  
- [X] 6.4 원하는 질의에 대한 특정 메서드와 반환 타입을 정의한다  
- [X] 6.5 시스템 경계를 넘는 질의에는 추상화를 정의한다  
- [X] 6.6 질의 메서드에 테스트 대역용 스텁을 사용한다  
- [X] 6.7 질의 메서드는 명령 메서드가 아니라 다른 질의 메서드를 사용해야 한다  
요약  
[연습 문제 해답]  

▣ 07장: 작업 수행하기  
- [X] 7.1 이름이 명령형인 명령 메서드를 사용한다  
- [X] 7.2 명령 메서드 유효 범위를 제한하고 이벤트를 사용해 부차적인 작업을 수행한다  
- [X] 7.3 서비스는 안팎으로 변경 불가능하게 한다  
- [X] 7.4 무언가 잘못되면 예외를 일으킨다  
- [X] 7.5 정보 수집에는 질의를, 다음 단계로 진행은 명령을 사용한다  
- [X] 7.6 시스템 경계를 넘는 명령에는 추상화를 정의한다  
- [X] 7.7 명령 메서드 호출은 목으로만 검증한다  
요약  
[연습 문제 해답]  

▣ 08장: 책임 나누기  
- [X] 8.1 읽기와 쓰기 모델을 분리한다  
- [X] 8.2 사용 사례에 맞는 읽기 모델을 생성한다  
- [X] 8.3 읽기 모델은 해당 데이터 근원에서 직접 생성한다  
- [X] 8.4 도메인 이벤트에서 읽기 모델을 만든다  
요약  
[연습 문제 해답]  

▣ 09장: 서비스 행위 변경하기  
- [X] 9.1 생성자 인자를 도입해 행위를 설정할 수 있게 한다  
- [X] 9.2 생성자 인자를 도입해 행위를 교체할 수 있게 한다  
- [X] 9.3 추상화를 구성해 더 복잡한 행위를 이룬다  
- [X] 9.4 기존 행위를 장식한다  
- [X] 9.5 추가 행위에는 통지 객체나 이벤트 수신자를 사용한다  
- [X] 9.6 객체 행위를 변경하는 데 상속을 사용하지 않는다  
- [X] 9.6.1 상속은 언제 사용해도 될까?  
- [X] 9.7 클래스는 기본적으로 최종 상태로 표시한다  
- [X] 9.8 메서드와 속성은 기본적으로 비공개로 표시한다  
요약  
[연습 문제 해답]  

▣ 10장: 객체에 대한 현장 가이드  
- [X] 10.1 제어기  
- [X] 10.2 응용 프로그램 서비스  
- [X] 10.3 쓰기 모델 저장소  
- [X] 10.4 개체  
- [X] 10.5 값 객체  
- [X] 10.6 이벤트 수신자  
- [X] 10.7 읽기 모델과 읽기 모델 저장소  
- [X] 10.8 추상화, 구체화, 계층 그리고 의존성  
요약  

▣ 11장: 끝맺으며  
- [X] 11.1 아키텍처 패턴  
- [X] 11.2 테스트  
- [X] 11.2.1 클래스 테스트 대 객체 테스트  
- [X] 11.2.2 하향식 기능 개발  
- [X] 11.3 도메인 주도 설계  
11.4 결론  
