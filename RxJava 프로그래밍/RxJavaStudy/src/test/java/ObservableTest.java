import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;

public class ObservableTest {

    @Test
    public void Observable() {
        Flowable.just("", "", "").subscribe();
    }

    @Test
    public void isDisposedTest() {
        Disposable d = Flowable.just("RED", "GREEN", "YELLOW").subscribe(
                v -> System.out.println("onNext() value : " + v),
                err -> System.err.println("onError() : err : " + err.getMessage()),
                () -> System.out.println("onComplate()")
        );
//        Disposted는 구독이 해제되었다. 라는 의미.
        assertEquals(true, d.isDisposed());
    }

    @Test
    public void createTest() {
        Observable<Integer> source = Observable.create(
                (ObservableEmitter<Integer> emitter) -> {
                    emitter.onNext(100);
                    emitter.onNext(200);
                    emitter.onNext(300);
                    emitter.onComplete();
                });

        source.test()
                .assertSubscribed()
                .assertResult(100, 200, 300);
    }

    @Test
    public void TestFromArray() {
        Integer[] arr = {100, 200, 300};
        Observable<Integer> source = Observable.fromArray(arr);
        source.test()
                .assertSubscribed()
                .assertResult(100, 200, 300);
    }

    @Test
    public void TestIterableMethod() {
        List<String> names = new ArrayList<>();
        names.add("Jerry");
        names.add("Jerry2");
        names.add("Jerry3");

        Observable<String> source2 = Observable.fromIterable(names);
        source2.test()
                .assertSubscribed()
                .assertResult("Jerry", "Jerry2", "Jerry3");
    }

    @Test
    public void testCallable() {
        Callable<String> callable = () -> {
            Thread.sleep(1000);
            return "Hello Callable";
        };

        Observable<String> source = Observable.fromCallable(callable);
        source.test()
                .assertSubscribed()
                .assertResult("Hello Callable");
    }
}
