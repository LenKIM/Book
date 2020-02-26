
안드로이드에서는 서로 다른 두 어플리케이션이 통신할 수 있는 강력한 기능을 제공

이 통신 방식은 코드에서 여러 가지 방식으로 설정할 수 있지만 모든 프로세스 간의 통신을 처리하는 내부 매커니즘은 IPC(Intel-Process Communication)
**하나**뿐이다.

 바인더는 안드로이드 어플 간의 통신에 사용될 뿐 아니라 어플이 안드로이드 시스템과 통신하는 데도 핵심적인 역할을 한다. Context.getSystemService() 메서드를 사용해 시스템 Service를 조회하면 내부적으로 바인더가 동작해 어플로 Service 객체에 대한 래퍼를 제공한다.

  안드로이드API에서는 IPC를 손쉽게 수행할 수 있는 멋진 래퍼를 제공해주므로 대게는 바인더를 가지고 저수준 작업을 할 일이 거의 없다. 이 장에서는 바인더가 어떻게 동작하는지 살펴보자.

> ### 바인더 살펴보기

바인더 IPC를 사용해 두 애플리케이션이 통신할 때 어플은 이 커널 트라이버를 사용해 메시지를 전달한다. 메시징 기능 외에도 바인더는 원격 호출자(프로세스ID 및 사용자ID) 식별, 원격 프로세스의 소멸(소멸링크) 알림 같은 부가 기능을 제공한다.

예를 들어 시스템에서는 WindowManager를 통해 안드로이드이 모든 창을 관리하는 시스템 Service가 모든 어플에 대한 Binder참조를 유지하고, 어플의 창이 닫힐 때 소멸 링크 알림을 통해 이를 통보 받을 떄 이와 같은 바인더의 추가 기능을 활용 한다.

바인더를 활용한 통신은 클라이언트-서버 모델을 따른다. 클라이언트는 클라이언트사이드 프록시를 사용해 커널 드라이브와의 통신을 처리한다. 서버사이드에서는 바인더 프레임워크가 여러 개의 바인더 스레드를 보관한다. 커널 드라이브는 서버사이드에 있는 바인더 스레드 중 하나를 사용해 클라이언트사이드 프록시에서 수신 객체로 메시지를 전달한다. 바인더를 통해 Service 호출을 받을 때는 서비스가 애플리케이션의 메인 스레드에서 실행되지 않으므로 이 사실을 기억하는게 중요하다. 이를 통해 원격 Service를 요청한 클라이언트는 Service 어플의 메인 스레드르르 블로킹할 수 없게 된다.

 안드에서는 Binder기저 클래스 및 IBinder인터페이스를 사용해 바인더를 구현, Service에서 원격API를 발행할 경우에는 보통 AIDL파일을 사용해 이 IBinder 구현체를 생성한다. 이 방식 외에 다른 방식도 사용할 수 있다.

#### 바인더 주소

  바인더를 통해 통신하려면 클라이언트가 원격 Binder객체의 주소를 알아야 한다. 하지만 Binder의 설계상 구현체에서는 주소를 알 수 있다. 이로 인해 Intent를 사용해 안드로이드 API에 접근하게 된다. 클라이언트는 액션 String이나 ComponentName을 사용해 Intent 객체를 생성하고, 이 인텐트를 사용해 원격 어플과 통신할 수 있다. 하지만 Intent는 실제 바인더 주소의 추상화일 뿐이며, 통신을 설정하려면 변환돼야 한다.

  안드로이드 시스템 서버 내부에서 실행되는 ServiceManager라고 부르는 특수 Binder노드는 안드로이드의 모든 주소를 관리한다. 이 노드는 전역으로 알려진 주소를 갖고 있는 유일한 Binder노드다. 안드로이드의 모든 컴포넌트가 통신에 바인더를 사용하므로, 컴포넌트에서는 이미 알고 있는 주소를 통해 ServiceManager에 접근해 등록을 해야 한다.

  <서비스 등록과 ServiceManager를 통한 조회 과정을 보여주는 다이어 그램>

   Service나 다른 컴포넌트와 통신하려는 클라이언트는 Intent 해석 과정을 통해 암시적으로 ServiceManager에 문의해 바인더 주소를 가져오게 된다.

#### 바인더 트랜잭션

   안드로이드에서 한 프로세스가 다른 프로세스로 데이터를 전송하면 이를 트랜잭션이라고 부른다. 트랜잭션은 클라이언트에서 바인더를 가지고 IBinder.transact()를 호출해 시작할 수 있으며, 그럼 서비스는 다음의 코드에서 보듯 Binder.onTransact()메서드를 통해 호출을 받는다.

```java
     public class TransactionClient {

     public String performCustomBinderTransacttion(IBinder binder, String arg0,
                                                   int arg1, float arg2)
             throws RemoteException {
         Parcel request = Parcel.obtain();
         Parcel response = Parcel.obtain();

         // Populate request data...
         request.writeString(arg0);
         request.writeInt(arg1);
         request.writeFloat(arg2);

         // Perform
         boolean isOk = binder.transact(IBinder.FIRST_CALL_TRANSACTION, request, response, 0);

         String result = response.readString();

         request.recycle();
         response.recycle();

         return result;
     }

    }
```

이 예제 코드는 유효한 IBinder 참조를 가져온 후 클라이언트사이드 코드에서 Service를 상대로 커스텀 바인더 트랜잭션을 수행하는 법을 잘 보여줌.

```java
    public class CustomBinder extends Binder {

        @Override
        protected boolean onTransact(int code, Parcel request, Parcel response, int flags) throws RemoteException {
            // Read the data in the request
            String arg0 = request.readString();
            int arg1 = request.readInt();
            float arg2 = request.readFloat();

            String result = buildResult(arg0, arg1, arg2);

            // Write the result to the response Parcel
            response.writeString(result);

            // Return true on success
            return true;
        }

        private String buildResult(String arg0, int arg1, float arg2) {
            String result = null;
            // TODO Build the result
            return result;
        }
```

AIDL을 사용하지 않고 Service에서 커스텀 Binder객체를 구현할 때는 위에처럼...

#### Parcel
 앞의 예에서 보듯 바인더 트랜잭션에서는 보통 트랜잭션 데이터를 함께 전달, 이 데이터를 parcel이라고 부르는데, 안드의 parcel은 자바 SE의 Serializable 객채와 비교해볼 수있다. 두 객체의 차이점은 parcelable 인터페이스를 사용할 떄는 객체의 마샬링 및 언마샬링을 직접 구현해야 한다는 점. 이 인터페이스에서는 다음과 같이 객체를 Parcel로 쓰는 두 메서드와 Parcel로부터 객체를 읽는 코드를 구현하는 static final Creator 객체를 정의.

 http://d2.naver.com/helloworld/47656 참고.
