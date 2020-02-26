import io.reactivex.Observable;
import io.reactivex.Single;
import org.junit.Test;

public class SingleTest {


    @Test
    public void manyWayToMakeSingle() {
        //1. 기존 Obserable에서 Single 객체로 변환하기
        Observable<String> source = Observable.just("Hello Single");
        Single.fromObservable(source)
                .test()
                .assertResult("Hello Single");

        //2. single()함수를 호출해 Single 객체 생성하기
        Observable.just("Hello Single")
                .single("default item")
                .test()
                .assertResult("Hello Single");

        //3. first()함수를 호출해 Single 객체 생성하기.
        String[] colors = {"RED", "Blue", "Gold"};
        Observable.fromArray(colors)
                .first("default value")
                .test()
                .assertResult("RED");

        //4. empty Observable에서 Single 객체 생성하기.
        Observable.empty()
                .single("default value")
                .subscribe(System.out::println);

        //5. take()함수에서 Single 객체 생성
        Observable.just("하하", "하하2")
                .take(1)
                .single("default order")
                .subscribe(System.out::println);
    }
}
